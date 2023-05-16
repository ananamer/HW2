package com.example.hw2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.style.UpdateLayout;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ScoresActivity extends AppCompatActivity {

    Intent playerIntent;
    Bundle playerBundle;
    Bundle allPlayersBundle;
    SharedPreferences pref;
    int playersNumber;

    public void saveData(int playersNumber){
        pref = getSharedPreferences("Players Data",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = pref.edit();
        myEdit.putInt("player_" + Integer.toString(playersNumber) + "_coins",playerBundle.getInt("COINS"));
        myEdit.putInt("player_" + Integer.toString(playersNumber) + "_distance",playerBundle.getInt("DISTANCE"));
        myEdit.putFloat("player_" + Integer.toString(playersNumber) + "_longitude",playerBundle.getFloat("LONGITUDE"));
        myEdit.putFloat("player_" + Integer.toString(playersNumber) + "_latitude",playerBundle.getFloat("LATITUDE"));
        myEdit.putString("player_" + Integer.toString(playersNumber) + "_locationName",playerBundle.getString("LOCATION_NAME"));
        myEdit.apply();
    }

    public void loadData(int playersNumber) {
        pref = getSharedPreferences("Players Data",MODE_PRIVATE);
        allPlayersBundle = new Bundle();
        for(int i=0; i<playersNumber; i++){
            allPlayersBundle.putInt("player_" + Integer.toString(i) + "_coins",pref.getInt("player_" + Integer.toString(i) + "_coins",0));
            allPlayersBundle.putInt("player_" + Integer.toString(i) + "_distance",pref.getInt("player_" + Integer.toString(i) + "_distance",0));
            allPlayersBundle.putFloat("player_" + Integer.toString(i) + "_longitude",pref.getFloat("player_" + Integer.toString(i) + "_longitude",0));
            allPlayersBundle.putFloat("player_" + Integer.toString(i) + "_latitude",pref.getFloat("player_" + Integer.toString(i) + "_latitude",0));
            allPlayersBundle.putString("player_" + Integer.toString(i) + "_locationName",pref.getString("player_" + Integer.toString(i) + "_locationName",""));
            Log.d("playerCoins",Integer.toString( allPlayersBundle.getInt("player_" + Integer.toString(i) + "_coins")));
        }
        allPlayersBundle.putInt("playersNumber",playersNumber);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        playerIntent = getIntent();
        pref = getSharedPreferences("Players Data",MODE_PRIVATE);
        SharedPreferences.Editor playerEdit = pref.edit();

        playersNumber= pref.getInt("playersNumber",0);
        loadData(playersNumber);

        if(playerIntent.getBundleExtra("playerBundle") != null)
        {
            playerBundle = playerIntent.getBundleExtra("playerBundle");
            saveData(playersNumber);
            playersNumber++;
            playerEdit.putInt("playersNumber",playersNumber);
            playerEdit.apply();
            loadData(playersNumber);

            Fragment fragment1 = new TableFragment();
            Fragment fragment2 = new MapsFragment();

            fragment1.setArguments(allPlayersBundle);
            fragment2.setArguments(allPlayersBundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.table_data_frame,fragment1).commit();

            getSupportFragmentManager().beginTransaction().replace(R.id.google_maps_frame,fragment2).commit();
        }
    }
}