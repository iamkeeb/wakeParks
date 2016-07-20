package com.example.kissesfrme_20.wakeparks;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class welcome_page extends AppCompatActivity {

    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        button2 = (Button) findViewById(R.id.button2);
    }

    /*Called when user clicks Not Now*/
    public void notNow(View view) {
        Intent intent = new Intent(this, find_park.class);
        startActivity(intent);
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

}
