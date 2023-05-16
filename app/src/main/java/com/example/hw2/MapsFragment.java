package com.example.hw2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MapsFragment extends Fragment {

    SupportMapFragment supportMapFragment;
    Bundle allPlayersBundle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        supportMapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        allPlayersBundle = getArguments();

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                for(int i=0; i<allPlayersBundle.getInt("playersNumber"); i++){
                    LatLng latLng = new LatLng(allPlayersBundle.getFloat("player_" + Integer.toString(i) + "_latitude"),allPlayersBundle.getFloat("player_" + Integer.toString(i) + "_longitude"));
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                    markerOptions.title("player in: " + allPlayersBundle.getString("player_" + Integer.toString(i) + "_locationName"));

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));

                    googleMap.addMarker(markerOptions);
                }
            }
        });

        return view;
    }


}