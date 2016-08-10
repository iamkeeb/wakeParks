package com.example.kissesfrme_20.wakeparks;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.JsonReader;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class park_map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        ArrayList<HashMap<String, String>> parks = new ArrayList<HashMap<String, String>>();
        try {
            parks = readJsonAll(getAssets().open("wake_parks.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < parks.size(); i++) {
            String name = "";
            name += parks.get(i).get("name");
            String address = "";
            address += parks.get(i).get("address");
            Log.v("TAG", address.toString());
            LatLng park = new LatLng(Double.parseDouble(parks.get(i).get("latitude")), Double.parseDouble(parks.get(i).get("longitude")));
            Log.v("TAG", park.toString());
            mMap.addMarker(new MarkerOptions().position(park).title(name));
        }


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }

        LatLng austin = new LatLng(30.2849, -97.7341);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(austin));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(6));

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker m) {
                parkSelected(m.getTitle());
            }
        });
    }

    // Starts the cable park activity and loads the correct park info
    public void parkSelected(String name) {
        Intent intent = new Intent(this, cable_park.class);
        intent.putExtra("park_name", name);
        startActivity(intent);
    }

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