package com.softwarica.googlemap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SearchEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AutoCompleteTextView etCity;
    private Button btnSearch;
    private List<LatitudeLongitude> latitudeLongitudeList;
    Marker markerName;
    CameraUpdate center, zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        etCity = findViewById(R.id.etCity);
        btnSearch = findViewById(R.id.btnSearch);
        fillArrayListAndSetAdapter();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etCity.getText().toString())) {
                    etCity.setError("Please enter a place name");
                    return;
                }
                //Get the current location of the place
                int position = SearchArrayList(etCity.getText().toString());
                Log.d("Position is :",""+position) ;
                if (position > -1)
                    loadMap(position);
                else
                    Toast.makeText(MainActivity.this, "Location not found by name :" + etCity.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadMap(int position) {
        //Remove old marker from map
        if(markerName!=null){
            markerName.remove();
        }
        double latitude=latitudeLongitudeList.get(position).getLat();
        double longititude=latitudeLongitudeList.get(position).getLon();
        String marker=latitudeLongitudeList.get(position).getMarker();
        center= CameraUpdateFactory.newLatLng(new LatLng(latitude,longititude));
        zoom=CameraUpdateFactory.zoomTo(17);
        Log.d("Latitude is: ",""+latitude);
        Log.d("Longitude is: ",""+longititude);
        Log.d("Marker is: ",""+marker);
        markerName=mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longititude)).title(marker));
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    private void fillArrayListAndSetAdapter() {
        latitudeLongitudeList = new ArrayList<>();
        latitudeLongitudeList.add(new LatitudeLongitude(27.7062581, 85.3300012, "Softwarica College"));
        latitudeLongitudeList.add(new LatitudeLongitude(27.7049381, 85.3288331, "Special Center"));
        latitudeLongitudeList.add(new LatitudeLongitude(27.7041718, 85.3066523, "Kathmandu Durbar Square"));

        String[] data = new String[latitudeLongitudeList.size()];

        for (int i = 0; i < data.length; i++) {
            data[i] = latitudeLongitudeList.get(i).getMarker();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_list_item_1, data
        );
        etCity.setAdapter(adapter);
        etCity.setThreshold(1);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
    }

    public int SearchArrayList(String name) {
        for (int i = 0; i < latitudeLongitudeList.size(); i++) {
            if (latitudeLongitudeList.get(i).getMarker().contains(name)) {
                return i;
            }
        }
        return -1;
    }
}