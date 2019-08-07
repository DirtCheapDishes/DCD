package com.dcd.recipes.stemapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.security.ProviderInstaller;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class RecipeActivity extends AppCompatActivity {
    String result;

    ArrayList<Recipe> data = new ArrayList<>();
    List<HashMap<String, Object>> fillMaps = new ArrayList<>();

    static private class RetrieveData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urlStr){
            // do stuff on non-UI thread
            //get data from the html database
            String content = null;
            URLConnection connection;
            try {
                connection =  new URL("https://bmorgan17.github.io/DCD/resources/data_raw.html").openConnection();
                Scanner scanner = new Scanner(connection.getInputStream());
                scanner.useDelimiter("\\Z");
                content = scanner.next();
                scanner.close();
            } catch ( Exception ex ) {
                ex.printStackTrace();
            }

            return content;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_recipe);
        getSupportActionBar().setTitle("DCD - Dirt Cheap Recipes");
        ListView out = findViewById(R.id.listHttp);

        ProviderInstaller.installIfNeededAsync(this, new ProviderInstaller.ProviderInstallListener() {
            @Override
            public void onProviderInstalled() {

            }

            @Override
            public void onProviderInstallFailed(int errorCode, Intent recoveryIntent) {
                GoogleApiAvailability.getInstance().showErrorNotification(RecipeActivity.this, errorCode);
            }
        });

        try {
            result = new RetrieveData().execute("https://bmorgan17.github.io/DCD/resources/data_raw.html").get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] lines = result.split("\\|##\\|");
        String[] elements;

        //split every recipe into it's elements and add it to data ArrayList
        HashMap<String, Object> map;
        for (String line: lines) {
            elements = line.split("\\|\\|##");
            data.add(new Recipe(elements[0], Integer.parseInt(elements[1]), elements[2]));
            map = new HashMap<>();
            map.put("title", elements[0]); // This will be shown in R.id.title
            map.put("description", new String(new char[Integer.parseInt(elements[1])]).replace("\0", "\u2605")); // And this in R.id.description
            fillMaps.add(map);
        }

        String[] from = new String[] { "title", "description" };
        int[] to = new int[] { R.id.title, R.id.description };
        SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.row, from, to);

        out.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                Intent descIntent = new Intent(RecipeActivity.this, DescActivity.class);
                descIntent.putExtra("title", data.get(position).getName() + " - " + new String(new char[data.get(position).getDifficulty()]).replace("\0", "\u2605"));
                descIntent.putExtra("desc", data.get(position).getDesc());

                startActivity(descIntent);
            }
        });
        out.setAdapter(adapter);

        Log.wtf("TEXT", "Posted");
    }
}
