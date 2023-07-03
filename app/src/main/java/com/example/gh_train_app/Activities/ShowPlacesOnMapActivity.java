package com.example.gh_train_app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.gh_train_app.OthersFiles.PlacesConstant;
import com.example.gh_train_app.R;
import com.example.gh_train_app.models.Results;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ShowPlacesOnMapActivity extends FragmentActivity implements OnMapReadyCallback {

    List<Results> results = new ArrayList<Results>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_places_on_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        results = PlacesConstant.results;
        Toast.makeText(this, String.valueOf(results.size()), Toast.LENGTH_LONG).show();
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
        for (int i = 0; i < results.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            Results googlePlace = results.get(i);
            double lat = Double.parseDouble(googlePlace.getGeometry().getLocation().getLat());
            double lng = Double.parseDouble(googlePlace.getGeometry().getLocation().getLng());
            String placeName = googlePlace.getName();
            String vicinity = googlePlace.getVicinity();
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            // add marker to map
            googleMap.addMarker(markerOptions).showInfoWindow();
            ;
            // move camera
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }
}

