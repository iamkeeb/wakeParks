package com.example.kissesfrme_20.wakeparks;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class favorites_list extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.park_list_view);

        //Bundle extras = getIntent().getExtras();
        ArrayList<HashMap<String, String>> parks = new ArrayList<HashMap<String,String>>();
        try {
            parks = readJsonAll(getAssets().open("wake_parks.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Park> park_list = new ArrayList<Park>();
        for (HashMap<String,String> h : parks) {
            park_list.add(new Park(h.get("name"), h.get("rating")));
        }

        listView = (ListView) findViewById(R.id.listView);

        RatingAdapter adapter = new RatingAdapter(this, park_list);

        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = listView.getItemAtPosition(position).toString();

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

        /* A modified ArrayAdapter that can update ratings as well as text*/

    class RatingAdapter extends ArrayAdapter<Park> {
        RatingAdapter(Context context, ArrayList<Park> list) {
            super(context, R.layout.list_element, list);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Park p = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_element, parent, false);
            }

            TextView n = (TextView) convertView.findViewById(R.id.label);
            n.setText(p.name);

            RatingBar r = (RatingBar) convertView.findViewById(R.id.ratingBar2);
            r.setRating(p.rating);

            return(convertView);
        }
    }

    /* Park object to be used in the RatingAdapter */
    class Park {
        String name;
        float rating;

        Park(String n, float r) {
            name = n;
            rating = r;
        }

        Park(String n, String r) {
            name = n;
            rating = Float.parseFloat(r);
        }

        public String toString() {
            return name;
        }
    }
}
