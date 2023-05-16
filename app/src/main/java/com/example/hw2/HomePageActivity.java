package com.example.hw2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomePageActivity extends AppCompatActivity {

    private Button buttonsGame;
    private Button sensorsGame;
    private Button gameScores;
    FusedLocationProviderClient client;
    Bundle playerBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        playerBundle = new Bundle();
        buttonsGame = findViewById(R.id.buttons_game);
        sensorsGame = findViewById(R.id.sensor_game);
        gameScores = findViewById(R.id.game_scores);
        client = LocationServices.getFusedLocationProviderClient(HomePageActivity.this);


        if (ActivityCompat.checkSelfPermission(HomePageActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(HomePageActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        pressed();
    }

    private void pressed() {
        buttonsGame.setOnClickListener(e -> {
            Intent intent1 = new Intent(this, ButtonsGameActivity.class);
            intent1.putExtra("playerBundle", playerBundle);
            startActivity(intent1);

        });

        sensorsGame.setOnClickListener(e -> {
            Intent intent2 = new Intent(this, SensorsGameActivity.class);
            intent2.putExtra("playerBundle", playerBundle);
            startActivity(intent2);
        });

        gameScores.setOnClickListener(e -> {
            Intent intent3 = new Intent(this, ScoresActivity.class);
            intent3.putExtra("playerBundle", playerBundle);
            startActivity(intent3);
        });
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(HomePageActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Log.d("message2",Double.toString(location.getLongitude()));
                        playerBundle.putFloat("LONGITUDE", (float)addresses.get(0).getLatitude());
                        playerBundle.putFloat("LATITUDE", (float)addresses.get(0).getLongitude());
                        playerBundle.putString("LOCATION_NAME", addresses.get(0).getLocality());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44){
            getCurrentLocation();
        }
        else{
            Toast.makeText(getApplicationContext(),"Permission denied.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }

}