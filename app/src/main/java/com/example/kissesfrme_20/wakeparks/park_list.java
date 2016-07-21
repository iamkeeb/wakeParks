package com.example.kissesfrme_20.wakeparks;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import java.util.Map;
import java.util.List;
import android.widget.SimpleAdapter;

/**
 * Created by kissesfrme_20 on 7/20/16.
 */
public class park_list extends find_park{

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Bundle extras = getIntent().getExtras();
        ArrayList<HashMap<String, String>> parks = new ArrayList<HashMap<String,String>>();
        try {
            parks = readJsonAll(getAssets().open("wake_parks.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] names = new String[10];
        String[] addresses = new String[10];
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        for(int i = 0; i < parks.size(); i++) {
            String name = "";
            name += parks.get(i).get("name");
            names[i] = name;
            String address = "";
            address += parks.get(i).get("address");
            addresses[i] = address;

            Map<String, String> allthings = new HashMap<String, String>(2);
            allthings.put("name", name);
            allthings.put("address", address);
            data.add(allthings);
        }

        setContentView(R.layout.park_list_view);
        listView = (ListView) findViewById(R.id.listView);

        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[] {"name", "address"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        //        android.R.layout.simple_list_item_1, android.R.id.text1, names);
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();

                parkSelected(itemValue);
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle app bar item clicks here. The app bar
        // automatically handles clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Starts the cable park activity and loads the correct park info
    public void parkSelected(String name) {
        Intent intent = new Intent(this, cable_park.class);
        intent.putExtra("park_name", name);
        startActivity(intent);
    }

    //Reads in a Json file and outputs a list of maps for each park
    public ArrayList<HashMap<String,String>> readJsonAll (InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        try {
            HashMap<String, String> json = new HashMap<String,String>();
            reader.beginArray();
            // Get each park
            while (reader.hasNext()) {
                reader.beginObject();
                //Iterate through each park
                if (reader.hasNext()) {
                    json = new HashMap<String,String>();
                    while (reader.hasNext()) {
                        String key = reader.nextName();
                        String value = reader.nextString();
                        json.put(key, value);
                    }
                    list.add(json);
                }
                reader.endObject();
            }
            reader.endArray();
        } finally {
            reader.close();
        }
        return list;
    }
}
