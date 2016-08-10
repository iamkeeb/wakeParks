package com.example.kissesfrme_20.wakeparks;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class park_search extends Activity {

        EditText editSearch;
        ListView listView;
        private ArrayList<String> mItems;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_park_search2);

            editSearch = (EditText)findViewById(R.id.editText1);
            listView = (ListView)findViewById(R.id.listView1);

            mItems = new ArrayList<String>();
            //--------------------------------------------------------------------------------------

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
                mItems.add(name);
            }

/*
            setContentView(R.layout.park_list_view);
            listView = (ListView) findViewById(R.id.listView);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, names);
            listView.setAdapter(adapter);
*/
            listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mItems));

            editSearch.addTextChangedListener(new TextWatcher() {
                //Event when changed word on EditTex
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ArrayList<String> temp = new ArrayList<String>();
                    int textlength = editSearch.getText().length();
                    temp.clear();
                    for (int i = 0; i < mItems.size(); i++)
                    {
                        if (textlength <= mItems.get(i).length())
                        {
                            if(editSearch.getText().toString().equalsIgnoreCase(
                                    (String)
                                            mItems.get(i).subSequence(0,
                                                    textlength)))
                            {
                                temp.add(mItems.get(i));
                            }
                        }
                    }
                    listView.setAdapter(new ArrayAdapter<String>(park_search.this,android.R.layout.simple_list_item_1, temp));


                }


                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub

                }
                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

                }
            });
            // ListView Item Click Listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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

            //--------------------------------------------------------------------------------------







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

    // Starts the cable park activity and loads the correct park info
    public void parkSelected(String name) {
        Intent intent = new Intent(this, cable_park.class);
        intent.putExtra("park_name", name);
        startActivity(intent);
    }



}

