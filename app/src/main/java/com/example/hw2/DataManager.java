package com.example.hw2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DataManager {

    private List<Player> players ;
    private int playersCounter;

    public DataManager() {
        this.players = new ArrayList<>();
        this.playersCounter = 0;
    }

    public void setPlayer(Player p){
        this.players.add(p);
        playersCounter++;
    }

    public void setPlayers(List<Player> players){
        for(Player p : players ){
            this.players.add(p);
        }
    }
    public List<Player> getPlayers(){
        return this.players;
    }

    public int getPlayersNumber(){
        return this.playersCounter;
    }
}
