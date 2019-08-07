import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.*;


/**
 *
 * @author Thomas Otero (H3R3T1C)
 */
public class AutoUp extends JFrame{

    private Thread worker;
    private final String root = "update/";

    private JTextArea outText;
    private JButton cancle;
    private JScrollPane sp;
    private JPanel pan1;
    private JPanel pan2;

     public AutoUp() {
        initComponents();
        outText.setText("Contacting Download Server...");
        download();
    }
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);



        pan1 = new JPanel();
        pan1.setLayout(new BorderLayout());

        pan2 = new JPanel();
        pan2.setLayout(new FlowLayout());

        outText = new JTextArea();
        sp = new JScrollPane();
        sp.setViewportView(outText);

        cancle = new JButton("Cancel Update");
        cancle.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        pan2.add(cancle);
        pan1.add(sp,BorderLayout.CENTER);
        pan1.add(pan2,BorderLayout.SOUTH);

        add(pan1);
        pack();
        this.setSize(500, 400);
    }

    private void download()
    {
        worker = new Thread(
        new Runnable(){
            public void run()
            {
                try {
                    downloadFile(getDownloadLinkFromHost());
                    unzip();
                    copyFiles(new File(root),new File("").getAbsolutePath());
                    cleanup();
                    outText.setText(outText.getText()+"\nUpdate Finished!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An error occured while preforming update!");
                }
            }
        });
        worker.start();
    }

    private void cleanup()
    {
        outText.setText(outText.getText()+"\nPreforming clean up...");
        File f = new File("update.zip");
        f.delete();
        remove(new File(root));
        new File(root).delete();
        outText.setText(outText.getText()+"\nRemoving old version...");
        remove(new File(System.getProperty("user.dir") + "\\DCD-master\\Android"));
        new File(System.getProperty("user.dir") + "\\DCD-master\\Android").delete();
        remove(new File(System.getProperty("user.dir") + "\\DCD-master\\resources"));
        new File(System.getProperty("user.dir") + "\\DCD-master\\resources").delete();
        File[] files = new File(System.getProperty("user.dir") + "\\DCD-master").listFiles();
        for (File ff: files) {
            if (!ff.getPath().equals(System.getProperty("user.dir") + "\\DCD-master\\PC")) {
                ff.delete();
                outText.setText(outText.getText()+"\nDeleting: " + ff.getPath());
            }
        }

        files = new File(System.getProperty("user.dir") + "\\DCD-master\\PC").listFiles();
        for (File ff: files) {
            ff.renameTo(new File(System.getProperty("user.dir") + "\\DCD-master\\" + ff.getName()));
            outText.setText(outText.getText()+"\nMoving: " + ff.getPath());
        }
        new File(System.getProperty("user.dir") + "\\DCD-master\\PC").delete();

        try {
            Runtime.getRuntime().exec("cmd /c ping localhost -n 2 > nul && del /s /q");
            Runtime.getRuntime().exec("cmd /c ping localhost -n 3 > nul && robocopy " + System.getProperty("user.dir") + "\\DCD-master\\PC " + System.getProperty("user.dir") + " /COPYALL /E");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
    private void remove(File f)
    {
        File[]files = f.listFiles();
        for(File ff:files)
        {
            if(ff.isDirectory())
            {
                remove(ff);
                ff.delete();
            }
            else
            {
                ff.delete();
            }
            outText.setText(outText.getText()+"\nDeleting: " + ff.getPath());
        }
    }
    private void copyFiles(File f,String dir) throws IOException
    {
        File[]files = f.listFiles();
        for(File ff:files)
        {
            if(ff.isDirectory()){
                new File(dir+"/"+ff.getName()).mkdir();
                copyFiles(ff,dir+"/"+ff.getName());
            }
            else
            {
                copy(ff.getAbsolutePath(),dir+"/"+ff.getName());
            }

        }
    }
    public void copy(String srFile, String dtFile) throws FileNotFoundException, IOException{

          File f1 = new File(srFile);
          File f2 = new File(dtFile);

          InputStream in = new FileInputStream(f1);

          OutputStream out = new FileOutputStream(f2);

          byte[] buf = new byte[1024];
          int len;
          while ((len = in.read(buf)) > 0){
            out.write(buf, 0, len);
          }
          in.close();
          out.close();
      }
    private void unzip() throws IOException
    {
         int BUFFER = 2048;
         BufferedOutputStream dest = null;
         BufferedInputStream is = null;
         ZipEntry entry;
         ZipFile zipfile = new ZipFile("update.zip");
         Enumeration e = zipfile.entries();
         (new File(root)).mkdir();
         while(e.hasMoreElements()) {
            entry = (ZipEntry) e.nextElement();
            outText.setText(outText.getText()+"\nExtracting: " +entry);
            if(entry.isDirectory())
                (new File(root+entry.getName())).mkdir();
            else{
                (new File(root+entry.getName())).createNewFile();
                is = new BufferedInputStream
                  (zipfile.getInputStream(entry));
                int count;
                byte data[] = new byte[BUFFER];
                FileOutputStream fos = new
                  FileOutputStream(root+entry.getName());
                dest = new
                  BufferedOutputStream(fos, BUFFER);
                while ((count = is.read(data, 0, BUFFER))
                  != -1) {
                   dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
                fos.flush();
                fos.close();
                is.close();
            }
         }

        zipfile.close();
    }
    private void downloadFile(String link) throws MalformedURLException, IOException
    {
        URL url = new URL(link);
        URLConnection conn = url.openConnection();
        InputStream is = conn.getInputStream();
        long max = conn.getContentLength();
        outText.setText(outText.getText()+"\n"+"Downloding file...\nUpdate Size(compressed): "+max+" Bytes");
        BufferedOutputStream fOut = new BufferedOutputStream(new FileOutputStream(new File("update.zip")));
        byte[] buffer = new byte[32 * 1024];
        int bytesRead = 0;
        int in = 0;
        while ((bytesRead = is.read(buffer)) != -1) {
            in += bytesRead;
            fOut.write(buffer, 0, bytesRead);
        }
        fOut.flush();
        fOut.close();
        is.close();
        outText.setText(outText.getText()+"\nDownload Complete!");

    }
    private String getDownloadLinkFromHost() throws MalformedURLException, IOException
    {
        String path = "https://bmorgan17.github.io/DCD/PC/download.html";
        URL url = new URL(path);

        InputStream html = null;

        html = url.openStream();

        int c = 0;
        StringBuilder buffer = new StringBuilder("");

        while(c != -1) {
            c = html.read();
        buffer.append((char)c);

        }
        html.close();
        return buffer.substring(buffer.indexOf("[url]")+5,buffer.indexOf("[/url]"));
    }
}
