package com.example.kissesfrme_20.wakeparks;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class cable_park extends AppCompatActivity {
    private static final String TAG = "Cable Park";
    private String lat, lon, park_name;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        park_name = "Hydrous Wake Park - Allen";
        if (extras != null) {
            park_name = extras.getString("park_name");
        }
        HashMap<String, String> park = new HashMap<String, String>();
        ;
        try {
            park = readJsonStream(getAssets().open("wake_parks.json"), park_name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_cable_park);

        TextView n = (TextView) findViewById(R.id.textView4);

        n.setText(park_name);

        RatingBar r = (RatingBar) findViewById(R.id.ratingBar);

        r.setRating(Float.parseFloat(park.get("rating")));

        TextView t = (TextView) findViewById(R.id.textView);

        t.setText(ParkOutput(park));

        lat = park.get("latitude");
        lon = park.get("longitude");

        Bitmap bmp = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(park.get("pic"));
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            ((ImageView) findViewById(R.id.imageView2)).setImageBitmap(bmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Done");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

    // Reads in a Json file and outputs a map of the selected park
    public HashMap<String, String> readJsonStream(InputStream in, String park_name) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        HashMap<String, String> json = new HashMap<String, String>();
        try {
            reader.beginArray();
            boolean done = false;
            while (reader.hasNext()) {
                reader.beginObject();
                if (reader.hasNext()) {
                    String key = reader.nextName();
                    String value = reader.nextString();
                    if (!done && value.equals(park_name)) {
                        done = true;
                        json.put(key, value);
                        while (reader.hasNext()) {
                            key = reader.nextName();
                            value = reader.nextString();
                            json.put(key, value);
                        }
                    } else
                        while (reader.hasNext()) {
                            reader.nextName();
                            reader.nextString();
                        }
                }
                reader.endObject();
            }
            reader.endArray();
        } finally {
            reader.close();
        }
        return json;
    }

    //Reads in a Json file and outputs a list of maps for each park
    public ArrayList<HashMap<String, String>> readJsonAll(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            HashMap<String, String> json = new HashMap<String, String>();
            reader.beginArray();
            // Get each park
            while (reader.hasNext()) {
                reader.beginObject();
                //Iterate through each park
                if (reader.hasNext()) {
                    json = new HashMap<String, String>();
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

    // Takes in a park map and outputs a nicer format
    public String ParkOutput(HashMap<String, String> map) {
        String output = "";
        output += map.get("website") + "\n";
        output += map.get("phone") + "\n";
        output += map.get("address") + "\n";
        output += "M - F: " + map.get("m-f") + "\n";
        output += "Saturday: " + map.get("sat") + "\n";
        output += "Sunday: " + map.get("sun") + "\n";
        output += "2 Hours: " + map.get("2hr") + "\n";
        output += "4 Hours: " + map.get("4hr") + "\n";
        output += "Helmet: " + map.get("helmet") + "\n";
        output += "Vest: " + map.get("vest") + "\n";
        output += "Beginner Board: " + map.get("beg brd") + "\n";
        output += "CBL Board: " + map.get("cbl brd") + "\n";
        output += "Camps: " + map.get("camps") + "\n";
        output += "Lessons: " + map.get("lessons") + "\n";
        return output;
    }

    // Opens the park's latitude and longitude in Google Maps
    public void Route(View view) {
        String loc = lat + "," + lon;
        String address = "geo:" + loc + "?q=" + loc + "(" + park_name + ")";
        Uri gmmIntentUri = Uri.parse(address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void Review(View view) {
        Intent intent = new Intent(this, review_page.class);
        intent.putExtra("park_name", park_name);
        startActivity(intent);
    }
}
