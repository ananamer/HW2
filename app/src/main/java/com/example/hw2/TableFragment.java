package com.example.hw2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TableFragment extends Fragment {

    View view;
    Player newPlayer;
    Bundle allPlayersBundle;
    DataManager data = new DataManager();
    TextView[] coinsTableData = new TextView[10];
    TextView[] distanceTableData = new TextView[10];


    private void createPlayersList(){
        for(int i=0; i< allPlayersBundle.getInt("playersNumber"); i++){
            newPlayer = new Player();
            newPlayer.setCoins(allPlayersBundle.getInt("player_" + Integer.toString(i) + "_coins"));
            newPlayer.setDistance(allPlayersBundle.getInt("player_" + Integer.toString(i) + "_distance"));
            newPlayer.setLocationLatitude(allPlayersBundle.getFloat("player_" + Integer.toString(i) + "_latitude"));
            newPlayer.setLocationLongitude(allPlayersBundle.getFloat("player_" + Integer.toString(i) + "_longitude"));
            newPlayer.setLocationName(allPlayersBundle.getString("player_" + Integer.toString(i) + "_locationName"));
            data.setPlayer(newPlayer);
            Collections.sort(data.getPlayers());
            printCoinsTableData();
        }
    }

    private void printCoinsTableData() {
        String coinsTextViewName = "coins";
        String distanceTextViewName = "distance";
        for(int i=0; i<10; i++){ // Loop that defines the 10 textViews and assign them to the textViews array
            coinsTextViewName += Integer.toString(i+1);
            distanceTextViewName += Integer.toString(i+1);
            coinsTableData[i] = view.findViewById(getResources().getIdentifier(coinsTextViewName,"id", getActivity().getPackageName()));
            distanceTableData[i] = view.findViewById(getResources().getIdentifier(distanceTextViewName,"id", getActivity().getPackageName()));
            if(data.getPlayers().size() > i){
                coinsTableData[i].setText(Integer.toString(data.getPlayers().get(i).getCoins()));
                distanceTableData[i].setText(Integer.toString(data.getPlayers().get(i).getDistance()));
            }
            coinsTextViewName = "coins";
            distanceTextViewName = "distance";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_table, container, false);
        allPlayersBundle = getArguments();
        if(allPlayersBundle != null){
            createPlayersList();
        }
        return view;
    }
}