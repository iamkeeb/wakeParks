package com.example.kissesfrme_20.wakeparks;

import android.app.FragmentManager;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cable_park extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        String park_name = "Hydrous Wake Park - Allen";
        if (extras != null) {
           park_name = extras.getString("park_name");
        }
        Map<String, String> parks;
        try {
            parks = readJsonStream(new FileInputStream("wake_parks.json"), park_name);
        } catch (IOException e) {
            e.printStackTrace();
        }


        setContentView(R.layout.activity_cable_park);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getFragmentManager();
        switch (item.getItemId()) {
            case R.id.information:
                // Information stuff here
                return true;
            case R.id.profile:
                // Profile stuff here
                return true;
            case R.id.add_park:
                // Add a Park stuff here
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Map<String, String> readJsonStream(InputStream in, String park_name) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            Map<String, String> json = new HashMap();
            json.put("park_name", park_name);
            reader.beginArray();
            while (!reader.nextString().equals(park_name)) {
                reader.endArray();
                reader.beginArray();
            }
            while (reader.hasNext()) {
                String key = reader.nextName();
                String value = reader.nextString();
                json.put(key, value);
            }
            reader.endArray();
            return json;
        } finally {
            reader.close();
        }
    }
}
