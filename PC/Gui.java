import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.util.ArrayList;
import java.io.*;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import java.net.*;
import java.util.Scanner;
import java.lang.Integer;

public class Gui {
    private JFrame frame = new JFrame("DCD - Dirt Cheap Dishes");
    private JList jList1 = new JList();
    private JList jListDialog = new JList();
    private JTextArea jTextArea1 = new JTextArea();
    private JTextArea jTextAreaDialog = new JTextArea();
    private JScrollPane scrollPane = new JScrollPane();
    private JScrollPane areaPane = new JScrollPane(jTextArea1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File"), viewMenu = new JMenu("View"), helpMenu = new JMenu("Help");
<<<<<<< HEAD
=======
    private JLabel nameLabel = new JLabel("Name:");
    private JLabel mainDescLabel = new JLabel("Description:");
>>>>>>> new repo

    Font defaultAreaFont = new Font(jTextArea1.getFont().getFontName(), jTextArea1.getFont().getStyle(), jTextArea1.getFont().getSize());

    ArrayList<Recipe> data = new ArrayList<Recipe>();
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<Recipe> localData = new ArrayList<Recipe>();
    ArrayList<String> namesDialog = new ArrayList<String>();

    JTextField nameField = new JTextField();
    JTextField difficultyField = new JTextField(5);
    JTextArea descField = new JTextArea();
    JScrollPane scrollDesc = new JScrollPane(descField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    int selected;

    Font defaultFont = (new JLabel()).getFont();

    public Gui() {
        if (!checkVersion()) createAndShowGUI();
    }

    public void loadData() {
        //clear data in ArrayLists
        data.clear();
        localData.clear();
        names.clear();
        namesDialog.clear();

        //get data from the html database
        String content = null;
        URLConnection connection = null;
        try {
          connection =  new URL("https://dirtcheapdishes.github.io/DCD/resources/data_raw.html").openConnection();
          Scanner scanner = new Scanner(connection.getInputStream());
          scanner.useDelimiter("\\Z");
          content = scanner.next();
          scanner.close();
        } catch ( Exception ex ) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error - UnknownHostException: Check your internet connection and try again.","Error", JOptionPane.ERROR_MESSAGE);
        }

        String[] lines;
        String[] elements;
        //split raw data into individual recipes
        if (content != null) {
            lines = content.split("\\|##\\|");

            //split every recipe into it's elements and add it to data ArrayList
            for (String line: lines) {
                elements = line.split("\\|\\|##");
                data.add(new Recipe(elements[0], Integer.parseInt(elements[1]), elements[2]));
            }
        }

        //read local recipes from file
        try {
            lines = getFileContent(new FileInputStream(new File("localRecipes.config")), "UTF-8").split("\\|##\\|");
            //split local recipes into their elements and add them to ArrayLists
            for (int i = 0; i < lines.length; i++) {
                elements = lines[i].split("\\|\\|##");
                try {
                    data.add(new Recipe(elements[0], Integer.parseInt(elements[1]), elements[2]));
                    localData.add(new Recipe(elements[0], Integer.parseInt(elements[1]), elements[2]));
                } catch (ArrayIndexOutOfBoundsException e) {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //populate list for "Name:" field
        for (Recipe r: data) names.add(r.getName());
        for (Recipe r: localData) namesDialog.add(r.getName());

        frame.revalidate();
        frame.repaint();
    }

    public void addComponentsToPane(Container pane) {
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //initial list population
        loadData();
        jList1.setCellRenderer(getRenderer());
<<<<<<< HEAD
        jList1.setFixedCellHeight(40);
=======

        setCellSize();
>>>>>>> new repo

        //populate list with recipe names
        jList1.setModel(new AbstractListModel() {
                static final long serialVersionUID = 1L;

                @Override
                public int getSize() {
                    return names.size();
                }

                @Override
                public Object getElementAt(int i) {
                    return "<html>" + names.get(i) + "<br/>" + "Difficulty: " + data.get(i).getDifficulty() + "/5</html>";
                }
            });
        //add listener that calls a function when item is selected
        jList1.addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent evt) {
                    jList1ValueChanged(evt);
                }
            });
        scrollPane.setViewportView(jList1);

        c.weightx = 0.25;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 0.01;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 10, 0, 10);
<<<<<<< HEAD
        pane.add(new JLabel("Name:"), c);
=======
        nameLabel.setFont(defaultFont);
        pane.add(nameLabel, c);
>>>>>>> new repo

        c.gridy = 1;
        c.weighty = 0.9;
        c.insets = new Insets(5, 10, 10, 10);
        pane.add(scrollPane, c);

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(Color.WHITE);
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);

        c.fill = GridBagConstraints.BOTH;
        c.weighty = 0.01;
        c.weightx = 0.75;
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 10, 0, 10);
<<<<<<< HEAD
        pane.add(new JLabel("Description:"), c);
=======
        mainDescLabel.setFont(defaultFont);
        pane.add(mainDescLabel, c);
>>>>>>> new repo

        c.gridy = 1;
        c.weighty = 0.9;
        c.insets = new Insets(5, 10, 10, 10);
        pane.add(areaPane, c);

        //menu bar
        menuBar.add(fileMenu);

        //file tab
        JMenuItem settingsItem = new JMenuItem(new AbstractAction("Settings") {
                static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    settings();
                }
            });
        fileMenu.add(settingsItem);

        JMenuItem reloadDataItem = new JMenuItem(new AbstractAction("Reload Recipes") {
                static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    loadData();
                }
            });
        fileMenu.addSeparator();
        fileMenu.add(reloadDataItem);

        //recipe subtab
        JMenu recipeItem = new JMenu("Recipe");
        fileMenu.add(recipeItem);

        JMenuItem addRecipeItem = new JMenuItem(new AbstractAction("Add Recipe") {
                static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    addLocalRecipe();
                    restart();
                }
            });
        recipeItem.add(addRecipeItem);

        JMenuItem viewRecipesItem = new JMenuItem(new AbstractAction("View Recipes") {
                static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    viewLocalRecipes();
                }
            });
        recipeItem.add(viewRecipesItem);

        JMenuItem rawRecipesItem = new JMenuItem(new AbstractAction("View Raw Recipe Data") {
                static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    try {
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                            Desktop.getDesktop().browse(new URI("https://dirtcheapdishes.github.io/DCD/resources/data_raw.html"));
                        }
                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                }
            });
        recipeItem.addSeparator();
        recipeItem.add(rawRecipesItem);

        JMenuItem suggestRecipesItem = new JMenuItem(new AbstractAction("Suggest New Global Recipe") {
                static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    try {
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                            Desktop.getDesktop().browse(new URI("https://github.com/DirtCheapDishes/DCD/issues/new?assignees=DirtCheapDishes&labels=recipe+suggestion&template=suggested-recipe-addition.md&title=%5BRecipe+Title%5D"));
                        }
                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                }
            });
        recipeItem.add(suggestRecipesItem);

        JMenuItem closeItem = new JMenuItem(new AbstractAction("Close") {
                static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                }
            });
        fileMenu.addSeparator();
        fileMenu.add(closeItem);

        //view menu
        menuBar.add(viewMenu);

        JMenuItem guiRestartItem = new JMenuItem(new AbstractAction("Restart GUI") {
                static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    restart();
                }
            });
        viewMenu.add(guiRestartItem);

        JMenuItem fontUpItem = new JMenuItem(new AbstractAction("Increase Font Size") {
                static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    fontUp();
                }
            });
        viewMenu.addSeparator();
        viewMenu.add(fontUpItem);

        JMenuItem fontDownItem = new JMenuItem(new AbstractAction("Decrease Font Size") {
                static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    fontDown();
                }
            });
        viewMenu.add(fontDownItem);

        JMenuItem fontResetItem = new JMenuItem(new AbstractAction("Reset Font Size") {
                static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    resetFont();
                }
            });
        viewMenu.add(fontResetItem);

        //help menu
        menuBar.add(helpMenu);

        JMenuItem reportItem = new JMenuItem(new AbstractAction("Report Issue") {
                static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    try {
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                            Desktop.getDesktop().browse(new URI("https://github.com/DirtCheapDishes/DCD/issues/new"));
                        }
                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                }
            });
        helpMenu.add(reportItem);

        JMenuItem searchIssueItem = new JMenuItem(new AbstractAction("Search Issues") {
                static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    try {
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                            Desktop.getDesktop().browse(new URI("https://github.com/DirtCheapDishes/DCD/issues"));
                        }
                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                }
            });
        helpMenu.add(searchIssueItem);

        JMenuItem webItem = new JMenuItem(new AbstractAction("Website") {
                static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    try {
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                            Desktop.getDesktop().browse(new URI("https://dirtcheapdishes.github.io/DCD/"));
                        }
                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                }
            });
        helpMenu.addSeparator();
        helpMenu.add(webItem);

        JMenuItem aboutItem = new JMenuItem("About App");
        helpMenu.add(aboutItem);

        JMenuItem repoItem = new JMenuItem(new AbstractAction("Github Repo") {
                static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e) {
                    try {
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                            Desktop.getDesktop().browse(new URI("https://github.com/DirtCheapDishes/DCD"));
                        }
                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                }
            });
        helpMenu.addSeparator();
        helpMenu.add(repoItem);

        //jListDialog setup
        //populate list with local recipe names
        jListDialog.setModel(new AbstractListModel() {
                static final long serialVersionUID = 1L;

                @Override
                public int getSize() {
                    return namesDialog.size();
                }

                @Override
                public Object getElementAt(int i) {
                    return namesDialog.get(i);
                }
            });
        //add listener that calls function when item is selected
        jListDialog.addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent evt) {
                    jListDialogValueChanged(evt);
                }
            });
    }

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {
        //show description of selected recipe name
        String s = (String) jList1.getSelectedValue();
        for (int i = 0; i < names.size(); i++) {
            if (s.equals("<html>" + names.get(i) + "<br/>" + "Difficulty: " + data.get(i).getDifficulty() + "/5</html>")) {
                jTextArea1.setText(data.get(i).getDesc());
            }
        }
    }

    public void settings() {
<<<<<<< HEAD

=======
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JButton decrement = new JButton("-");
        JButton increment = new JButton("+");
        JTextField exFont = new JTextField("This is example text.");
        JButton resetButton = new JButton("Reset");

        JButton areaDecrement = new JButton("-");
        JButton areaIncrement = new JButton("+");
        JTextField areaExFont = new JTextField("This is example text.");
        JButton areaResetButton = new JButton("Reset");

        exFont.setMinimumSize(new Dimension(115, 25));
        exFont.setPreferredSize(new Dimension(115, 25));
        exFont.setMaximumSize(new Dimension(115, 25));
        exFont.setSize(new Dimension(115, 25));
        exFont.setFont(defaultFont);

        areaExFont.setMinimumSize(new Dimension(115, 25));
        areaExFont.setPreferredSize(new Dimension(115, 25));
        areaExFont.setMaximumSize(new Dimension(115, 25));
        areaExFont.setSize(new Dimension(115, 25));
        areaExFont.setFont(jTextArea1.getFont());

        decrement.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fontDown();
                exFont.setFont(defaultFont);

                settingsPanel.revalidate();
                settingsPanel.repaint();
            }
        });

        //increment button
        increment.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fontUp();
                exFont.setFont(defaultFont);

                settingsPanel.revalidate();
                settingsPanel.repaint();
            }
        });

        areaDecrement.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                areaFontDown();
                areaExFont.setFont(jTextArea1.getFont());

                settingsPanel.revalidate();
                settingsPanel.repaint();
            }
        });

        //increment button
        areaIncrement.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                areaFontUp();
                areaExFont.setFont(jTextArea1.getFont());

                settingsPanel.revalidate();
                settingsPanel.repaint();
            }
        });

        resetButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                resetFont();
                exFont.setFont(defaultFont);

                settingsPanel.revalidate();
                settingsPanel.repaint();
            }
        });

        areaResetButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                resetAreaFont();
                areaExFont.setFont(jTextArea1.getFont());

                settingsPanel.revalidate();
                settingsPanel.repaint();
            }
        });

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 0;
        c.gridwidth = 3;
        c.insets = new Insets(2, 0, 2, 0);

        c.gridy = 0;
        settingsPanel.add(new JLabel("Titles Font Size:"), c);
        c.gridy = 1;
        c.gridwidth = 1;
        settingsPanel.add(decrement, c);
        c.gridx = 1;
        c.insets = new Insets(0, 5, 0, 5);
        settingsPanel.add(exFont, c);
        c.gridx = 2;
        c.insets = new Insets(0, 0, 0, 0);
        settingsPanel.add(increment, c);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        settingsPanel.add(resetButton, c);

        c.insets = new Insets(2, 0, 2, 0);
        c.gridy = 3;
        settingsPanel.add(new JLabel("Body Font Size:"), c);
        c.gridy = 4;
        c.gridwidth = 1;
        settingsPanel.add(areaDecrement, c);
        c.gridx = 1;
        c.insets = new Insets(0, 5, 0, 5);
        settingsPanel.add(areaExFont, c);
        c.gridx = 2;
        c.insets = new Insets(0, 0, 0, 0);
        settingsPanel.add(areaIncrement, c);
        c.gridy = 5;
        c.gridx = 0;
        c.gridwidth = 3;
        settingsPanel.add(areaResetButton, c);

        JOptionPane.showConfirmDialog(null, settingsPanel, "Settings", JOptionPane.OK_CANCEL_OPTION);
>>>>>>> new repo
    }

    public boolean addLocalRecipe() {
        descField.setBackground(Color.WHITE);
        descField.setLineWrap(true);
        descField.setWrapStyleWord(true);
        descField.setRows(5);

        //increment decrement field
        difficultyField.setBackground(Color.LIGHT_GRAY);
        difficultyField.setHorizontalAlignment(JTextField.CENTER);
        if (difficultyField.getText().equals("")) {
            difficultyField.setText("1");
        }
        difficultyField.setFont(difficultyField.getFont().deriveFont(Font.BOLD, 18f));
        //listen for keys other than 1-5 and backspace or delete, ignore them
        difficultyField.addKeyListener(new KeyAdapter() {
           public void keyTyped(KeyEvent e) {
              char ch = e.getKeyChar();
              if ((((ch < '1') || (ch > '5')) && (ch != KeyEvent.VK_BACK_SPACE || ch != KeyEvent.VK_DELETE)) || (difficultyField.getText().length() > 0)) {
                 e.consume();  // ignore event
              }
           }
        });

        JPanel dialogPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JButton decrement = new JButton("-");
        JButton increment = new JButton("+");

        //decrement button
        decrement.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                char ch = difficultyField.getText().charAt(0);

                if (ch > '1') {
                    ch--;
                    difficultyField.setText(Character.toString(ch));
                }
            }
        });

        //increment button
        increment.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                char ch = difficultyField.getText().charAt(0);

                if (ch < '5') {
                    ch++;
                    difficultyField.setText(Character.toString(ch));
                }
            }
        });

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 0;
        c.gridwidth = 3;
        c.insets = new Insets(2, 0, 2, 0);

        c.gridy = 0;
        dialogPanel.add(new JLabel("Recipe Name:"), c);
        c.gridy = 1;
        dialogPanel.add(nameField, c);
        c.gridy = 2;
        dialogPanel.add(new JLabel("Difficulty:"), c);
        c.gridy = 3;
        c.gridx = 0;
        c.gridwidth = 1;
        dialogPanel.add(decrement, c);
        c.gridx = 1;
        c.insets = new Insets(0, 5, 0, 5);
        dialogPanel.add(difficultyField, c);
        c.gridx = 2;
        c.insets = new Insets(0, 0, 0, 0);
        dialogPanel.add(increment, c);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 3;
        dialogPanel.add(new JLabel("Description:"), c);
        c.gridy = 5;
        dialogPanel.add(scrollDesc, c);

        //show add recipe dialog
        int result = JOptionPane.showConfirmDialog(null, dialogPanel, "Please enter recipe information.", JOptionPane.OK_CANCEL_OPTION);
        //add recipe to local file
        if (result == JOptionPane.OK_OPTION) {
            try {
                File dataFile = new File("localRecipes.config");
                dataFile.createNewFile(); // if file already exists will do nothing
                FileOutputStream oFile = new FileOutputStream(dataFile, true);

                byte[] bytes = (nameField.getText() + "||##").getBytes();
                oFile.write(bytes);
                bytes = (difficultyField.getText() + "||##").getBytes();
                oFile.write(bytes);
                bytes = (descField.getText() + "|##|").getBytes();
                oFile.write(bytes);
                oFile.flush();

                oFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //reset input fields
            nameField.setText("");
            difficultyField.setText("1");
            descField.setText("");

            return true;
        } else {
            return false;
        }
    }

    public void viewLocalRecipes() {
        GridBagConstraints c = new GridBagConstraints();
        final int ADD = 0, DELETE = 1, EDIT = 2, EXPORT = 3, CLEAR = 4;

        JPanel dialogPanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPaneDialog = new JScrollPane();
        JScrollPane areaPaneDialog = new JScrollPane(jTextAreaDialog, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        loadData();

        scrollPaneDialog.setViewportView(jListDialog);
        jListDialog.clearSelection();

        c.weightx = 0.25;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        dialogPanel.add(scrollPaneDialog, c);

        jTextAreaDialog.setEditable(false);
        jTextAreaDialog.setBackground(Color.WHITE);
        jTextAreaDialog.setLineWrap(true);
        jTextAreaDialog.setWrapStyleWord(true);

        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 0.75;
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        jTextAreaDialog.setText("");
        dialogPanel.add(areaPaneDialog, c);

        //show dialog with 5 options
        String[] options = new String[] {"Add", "Delete", "Edit", "Export", "Clear Recipes", "Cancel"};
        int response = JOptionPane.showOptionDialog(null, dialogPanel, "Local Recipes", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        switch (response) {
            case ADD:
                addLocalRecipe();
                break;
            case DELETE:
                deleteLocalLine(selected);
                break;
            case EDIT:
                editLocalRecipe(selected);
                restart();
                break;
            case EXPORT:
                copyRecipe(selected);
                break;
            case CLEAR:
                clearRecipes();
                break;
        }
    }

    public void copyRecipe(int s) {
        File in = new File("localRecipes.config");
        String[] elements;

        try {
            try {
                String[] lines = getFileContent(new FileInputStream(in), "UTF-8").split("\\|##\\|");

                //find selected item, fill input fields, and open dialog
                for (int i = 0; i < lines.length; i++) {
                    if (i == s) {
                        elements = lines[i].split("\\|\\|##");

                        String myString = "Recipe Title: " + elements[0] + "\n\n" +
                                          "Difficulty: " + elements[1] + "\n\n" +
                                          "Instructions and Ingredients: " + elements[2];

                        StringSelection stringSelection = new StringSelection(myString);
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(stringSelection, null);

                        JOptionPane.showMessageDialog(null, "Exported recipe copied to clipboard.");
                    }
                }
            } catch (FileNotFoundException f) {
                f.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteLocalLine(int s) {
        try {
            try {
                File out = new File("localRecipes.config");
                File in = new File("localRecipes.config");

                String[] lines = getFileContent(new FileInputStream(in), "UTF-8").split("\\|##\\|");
                in.setWritable(true);
                FileOutputStream oFile = new FileOutputStream(out, false);

                //write every recipe to local file except for delete request
                for (int i = 0; i < lines.length; i++) {
                    if (i != s) {
                        if (!(lines[i].trim().equals(""))) {
                            byte[] bytes = (lines[i] + "|##|").getBytes();
                            oFile.write(bytes);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException f) {
            f.printStackTrace();
        }

        loadData();
    }

    public void editLocalRecipe(int s) {
        File in = new File("localRecipes.config");
        String[] elements;

        try {
            try {
                String[] lines = getFileContent(new FileInputStream(in), "UTF-8").split("\\|##\\|");

                //find selected item, fill input fields, and open dialog
                for (int i = 0; i < lines.length; i++) {
                    if (i == s) {
                        elements = lines[i].split("\\|\\|##");

                        nameField.setText(elements[0]);
                        difficultyField.setText(elements[1]);
                        descField.setText(elements[2]);

                        if (addLocalRecipe() == false) return;
                    }
                }

                deleteLocalLine(s);
            } catch (FileNotFoundException f) {
                f.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearRecipes() {
        File delFile = new File("localRecipes.config");

        if(delFile.exists()){
        	delFile.delete();
        	try {
        		delFile.createNewFile();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }

        loadData();
    }

    private void jListDialogValueChanged(javax.swing.event.ListSelectionEvent evt) {
        try {
            for (int i = 0; i < namesDialog.size(); i++) {
                if (jListDialog.getSelectedValue().equals(namesDialog.get(i))) {
                    selected = i;
                    jTextAreaDialog.setText(localData.get(i).getDesc());
                }
            }
        } catch (NullPointerException e) {
            return;
        }
    }

    public void fontUp() {
        defaultFont = new Font(defaultFont.getFontName(), defaultFont.getStyle(), defaultFont.getSize() + 1);

<<<<<<< HEAD
        Font textFont = jTextArea1.getFont();
        Font textFont2 = new Font(textFont.getFontName(), textFont.getStyle(), textFont.getSize() + 1);
        jTextArea1.setFont(textFont2);
=======
        nameLabel.setFont(defaultFont);
        mainDescLabel.setFont(defaultFont);

        setCellSize();
>>>>>>> new repo

        frame.revalidate();
        frame.repaint();
    }

<<<<<<< HEAD
    public void fontDown() {
        defaultFont = new Font(defaultFont.getFontName(), defaultFont.getStyle(), defaultFont.getSize() - 1);

        Font textFont = jTextArea1.getFont();
        Font textFont2 = new Font(textFont.getFontName(), textFont.getStyle(), textFont.getSize() - 1);
        jTextArea1.setFont(textFont2);
=======
    public void areaFontUp() {
        Font textFont = jTextArea1.getFont();
        Font textFont2 = new Font(textFont.getFontName(), textFont.getStyle(), textFont.getSize() + 1);
        jTextArea1.setFont(textFont2);
    }

    public void fontDown() {
        defaultFont = new Font(defaultFont.getFontName(), defaultFont.getStyle(), defaultFont.getSize() - 1);

        nameLabel.setFont(defaultFont);
        mainDescLabel.setFont(defaultFont);

        setCellSize();

        frame.revalidate();
        frame.repaint();
    }

    public void areaFontDown() {
        Font textFont = jTextArea1.getFont();
        Font textFont2 = new Font(textFont.getFontName(), textFont.getStyle(), textFont.getSize() - 1);
        jTextArea1.setFont(textFont2);
    }

    public void resetFont() {
        defaultFont = (new JLabel()).getFont();
        nameLabel.setFont(defaultFont);
        mainDescLabel.setFont(defaultFont);

        setCellSize();
>>>>>>> new repo

        frame.revalidate();
        frame.repaint();
    }

<<<<<<< HEAD
    public void resetFont() {
        jTextArea1.setFont(defaultAreaFont);

        defaultFont = (new JLabel()).getFont();

=======
    public void resetAreaFont() {
        jTextArea1.setFont(defaultAreaFont);

>>>>>>> new repo
        frame.revalidate();
        frame.repaint();
    }

    private boolean checkVersion() {
        String line = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader("version.html"));
            line = reader.readLine();
            line = line.substring(line.indexOf("[version]")+9,line.indexOf("[/version]"));
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(line);
        try {
            System.out.println(Updater.getLatestVersion());
            if (Double.parseDouble(line) != Double.parseDouble(Updater.getLatestVersion())) {
                new UpdateInfo(Updater.getWhatsNew());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowGUI() {
        //Create and set up the window.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(900, 500));
        frame.setJMenuBar(menuBar);

        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());

        //Display the window.
        try {
            frame.setIconImage(ImageIO.read(new File("Sandwich.png")));
        } catch (IOException e) {

        }
        frame.pack();
        frame.setVisible(true);
    }

    public static String getFileContent(FileInputStream fis, String encoding ) throws IOException {
        try(BufferedReader br = new BufferedReader( new InputStreamReader(fis, encoding ))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while(( line = br.readLine()) != null ) {
                sb.append( line );
                sb.append( '\n' );
            }
            return sb.toString();
        }
    }

    public static String getLongestString(ArrayList<String> array) {
        int maxLength = 0;
        String longestString = null;
        for (String s : array) {
            if (s.length() > maxLength) {
                maxLength = s.length();
                longestString = s;
            }
        }
        return longestString;
    }

    private ListCellRenderer<? super String> getRenderer() {
        return new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                    Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                JPanel cellPanel = new JPanel();
                JLabel name = new JLabel(names.get(index)), diff = new JLabel("Difficulty: " + data.get(index).getDifficulty());

                cellPanel.setLayout(new BoxLayout(cellPanel, BoxLayout.Y_AXIS));
                cellPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,Color.BLACK));
                if (isSelected) {
                    cellPanel.setBackground(Color.LIGHT_GRAY);
                } else {
                    cellPanel.setBackground(Color.WHITE);
                }

                name.setFont(defaultFont);
                diff.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, defaultFont.getSize() - 2));

                cellPanel.add(name);
                cellPanel.add(diff);

                return cellPanel;
            }
        };
    }

    public void append(JTextPane c, Style style, String s) {
        try {
            StyledDocument doc = c.getStyledDocument();
            doc.insertString(doc.getLength(), s, style);
        } catch (BadLocationException exc) {}
    }

<<<<<<< HEAD
=======
    public void setCellSize() {
        int longestWord = 0;
        for (int i = 0; i < names.size(); i++) {
            if (new JLabel().getFontMetrics(defaultFont).stringWidth(names.get(i)) > longestWord) longestWord = new JLabel().getFontMetrics(defaultFont).stringWidth(names.get(i));
            if (new JLabel().getFontMetrics(defaultFont).stringWidth("Difficulty: " + data.get(i).getDifficulty() + "/5") > longestWord) longestWord = new JLabel().getFontMetrics(defaultFont).stringWidth("Difficulty: " + data.get(i).getDifficulty() + "/5");
        }
        jList1.setFixedCellWidth(longestWord);

        jList1.setFixedCellHeight(defaultFont.getSize() * 3);
    }

>>>>>>> new repo
    public void restart() {
        frame.dispose();
        new Gui();
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Gui();
            }
        });
    }
}
