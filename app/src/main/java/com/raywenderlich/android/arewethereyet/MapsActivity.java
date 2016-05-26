package com.raywenderlich.android.arewethereyet;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity {

    @Bind(R.id.btndata)
    Button btndata;
    @OnClick(R.id.btndata) void click(){
        Intent i = new Intent(this,AllGeofencesActivity.class);
        startActivity(i);
    }
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        setUpMapIfNeeded();
        Log.d("Oncreate", "Oncreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        Log.d("OnResume", "OnResume");
    }


    public void onSearch(View view) {
        Log.d("OnSearch", "OnSearch");
        EditText location_tf = (EditText) findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        }
    }

    public void onZoom(View view) {
        Log.d("Onzoom", "Onzoom");
        if (view.getId() == R.id.Bzoomin) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if (view.getId() == R.id.Bzoomout) {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    public void changeType(View view) {
        Log.d("changeType", "changeType");
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        Log.d("setUpMap", "setUpMap");
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(13.698948,100.537306) , 6.0f));
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(13.698948, 100.537306))
                .radius(2000)
                .strokeColor(Color.BLACK)
                .fillColor(Color.LTGRAY));
        // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        LatLng Bangkok = new LatLng(13.698948, 100.537306);
        mMap.addMarker(new MarkerOptions().position(Bangkok)
                .title("Central Rama3")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.h))
                .snippet("Central Rama3"));

        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(13.698948, 100.537306))
                .width(5)
                .color(Color.RED));


        mMap.setMyLocationEnabled(true);


    }
}
