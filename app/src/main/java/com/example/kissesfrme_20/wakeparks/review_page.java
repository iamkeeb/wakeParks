package com.example.kissesfrme_20.wakeparks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

public class review_page extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    ListView listView;
    String park_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_page);

        Button add_review = (Button) findViewById(R.id.button10);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null) {
            ((ViewGroup) add_review.getParent()).removeView(add_review);
        }


        Bundle extras = getIntent().getExtras();
        park_name = "Hydrous Wake Park - Allen";
        if (extras != null) {
            park_name = extras.getString("park_name");
        }

        ArrayList<Review> review_list = new  ArrayList<Review>();
        try {
            try {
                review_list = JsonToReview(readJsonAll(openFileInput("reviews.json")));
            } catch (FileNotFoundException e) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        Review test1 = new Review(park_name, "This feature works!!!", 5);
        review_list.add(test1);
        Review test2 = new Review(park_name, park_name, 5);
        review_list.add(test2);
        Review test3 = new Review(park_name, "Now for a really really really long test review because people will be writing a ton of useless information in their reviews.json and we want to make sure it doesnt look like crap in our activity page because we are such good coders and want a good grade on this assignment. Kthxbai.", 1.5f);
        review_list.add(test3);
        */

        listView = (ListView) findViewById(R.id.listView);

        RatingAdapter adapter = new RatingAdapter(this, review_list);

        listView.setAdapter(adapter);
    }

    public void addReview(View view) {
        View reviewView = LayoutInflater.from(this).inflate(R.layout.review_element, null);

        final RatingBar r = (RatingBar) reviewView.findViewById(R.id.ratingBar3);
        final EditText t = (EditText) reviewView.findViewById(R.id.editText3);

        new AlertDialog.Builder(this)
                .setTitle(park_name)
                .setMessage("Rate and Review the Park!")
                .setView(reviewView)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        saveReview("" + r.getRating(), "" + t.getText());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    public void saveReview(String rating, String review){
        ArrayList<HashMap<String, String>> reviews = new ArrayList<HashMap<String,String>>();
        try {
            try {
                reviews = readJsonAll(openFileInput("reviews.json"));
            } catch (FileNotFoundException e) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<String, String> r = new HashMap<String,String>();
        r.put("park", park_name);
        r.put("rating", rating);
        r.put("review", review);
        reviews.add(r);

        try {
            writeJson(openFileOutput("reviews.json", 0) , reviews);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Reads in a Json file and outputs a list of maps for each review
    public ArrayList<HashMap<String,String>> readJsonAll (InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        try {
            HashMap<String, String> json = new HashMap<String,String>();
            reader.beginArray();
            // Get each review
            while (reader.hasNext()) {
                reader.beginObject();
                //Iterate through each review
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

    public void writeJson (OutputStream out, ArrayList<HashMap<String, String>> list) throws IOException  {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writer.beginArray();
        for (HashMap<String, String> m : list) {
            writer.beginObject();
            for (String k : m.keySet()) {
                writer.name(k).value(m.get(k));
            }
            writer.endObject();
        }
        writer.endArray();
        writer.close();
    }

    public ArrayList<Review> JsonToReview (ArrayList<HashMap<String,String>> j) {
        ArrayList<Review> reviews = new ArrayList<Review>();
        for (HashMap<String, String> m : j) {
            reviews.add(new Review(m.get("park"), m.get("review"), m.get("rating")));
        }
        return reviews;
    }

    /* A modified ArrayAdapter that can update ratings as well as text*/

    class RatingAdapter extends ArrayAdapter<Review> {
        RatingAdapter(Context context, ArrayList<Review> list) {
            super(context, R.layout.list_element, list);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Review r = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_element, parent, false);
            }

            TextView n = (TextView) convertView.findViewById(R.id.label);
            n.setText(r.text);

            RatingBar rb = (RatingBar) convertView.findViewById(R.id.ratingBar2);
            rb.setRating(r.rating);

            return(convertView);
        }
    }

    /* Review object to be used in the RatingAdapter */
    class Review {
        String park;
        String text;
        float rating;

        Review(String p, String t, float r) {
            park = p;
            text = t;
            rating = r;
        }

        Review(String p, String t, String r) {
            park = p;
            text = t;
            rating = Float.parseFloat(r);
        }

        public String toString() {
            return text;
        }
    }
}
