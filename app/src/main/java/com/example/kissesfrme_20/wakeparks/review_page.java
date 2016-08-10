package com.example.kissesfrme_20.wakeparks;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

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

        ArrayList<Review> review_list = new ArrayList<Review>();

        Review test1 = new Review("This feature works!!!", 5);
        review_list.add(test1);
        Review test2 = new Review(park_name, 5);
        review_list.add(test2);
        Review test3 = new Review("Now for a really really really long test review because people will be writing a ton of useless information in their reviews and we want to make sure it doesnt look like crap in our activity page because we are such good coders and want a good grade on this assignment. Kthxbai.", 1.5f);
        review_list.add(test3);

        listView = (ListView) findViewById(R.id.listView);

        RatingAdapter adapter = new RatingAdapter(this, review_list);

        listView.setAdapter(adapter);
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
        String text;
        float rating;

        Review(String t, float r) {
            text = t;
            rating = r;
        }

        Review(String t, String r) {
            text = t;
            rating = Float.parseFloat(r);
        }

        public String toString() {
            return text;
        }
    }
}