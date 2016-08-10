package com.example.kissesfrme_20.wakeparks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;

    Intent intent = getIntent();
    private Button logout;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, login_page.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        logout = (Button) findViewById(R.id.logoutbutton);
        logout.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        if(view == logout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, login_page.class));
        }
    }

    public void listParks(View view) {
        Intent intent = new Intent(this, park_list.class);
        startActivity(intent);
    }

    public void parkMaps(View view) {
        Intent intent = new Intent(this, park_map.class);
        startActivity(intent);
    }

    public void searchParks(View view) {
        Intent intent = new Intent(this, park_search.class);
        startActivity(intent);
    }
}
