package com.example.kissesfrme_20.wakeparks;

import android.os.Bundle;
import android.view.MenuItem;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by kissesfrme_20 on 7/20/16.
 */
public class find_park extends welcome_page {

    Intent intent = getIntent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpark);
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

    public void listParks(View view) {
        Intent intent = new Intent(this, park_list.class);
        startActivity(intent);
    }

    public void parkMaps(View view) {
        Intent intent = new Intent(this, park_map.class);
        startActivity(intent);
    }
}
