package com.example.hw2;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Game {

    public final int TRACKS_NUMBER = 5;
    public final int HEARTS_NUMBER = 3;

    private ImageView hearts[];
    private RelativeLayout tracks[];
    private ImageView car;
    private ImageView stone;
    private ImageView coin;
    private TextView showCoins;
    private TextView showDistance;
    private int heartsCounter;
    private int tracksCounter;
    private int distance;
    private int coinsCounter;

    public Game() {
        this.hearts = new ImageView[HEARTS_NUMBER];
        this.tracks = new RelativeLayout[TRACKS_NUMBER];
        this.heartsCounter = 0;
        this.tracksCounter = 0;
        this.distance = 0;
        this.coinsCounter = 0;
    }

    public void setHearts(ImageView heart) {
        this.hearts[heartsCounter] = heart;
        heartsCounter++;
    }
    public ImageView[] getHearts() {
        return hearts;
    }

    public void setTracks(RelativeLayout track) {
        this.tracks[tracksCounter] = track;
        tracksCounter++;
    }
    public RelativeLayout[] getTracks() {
        return tracks;
    }

    public void setCar(ImageView car) {
        this.car = car;
    }
    public ImageView getCar() {
        return car;
    }

    public void setStone(ImageView stone) {
        this.stone = stone;
    }
    public ImageView getStone() {
        return stone;
    }

    public void setCoin(ImageView coin) {
        this.coin = coin;
    }
    public ImageView getCoin() {
        return coin;
    }

    public void setShowCoins(TextView showCoin) {
        this.showCoins = showCoin;
        updateShowCoins();
    }
    public void updateShowCoins() {
        this.showCoins.setText("Coins: " + Integer.toString(this.coinsCounter));
    }

    public void setShowDistance(TextView showDistance) {
        this.showDistance = showDistance;
        if(this.distance%10 == 0)
            updateShowDistance();
    }
    public void updateShowDistance() {
        this.showDistance.setText("Distance: " + Integer.toString(this.distance)+"m");
    }

    public void setDistance(int d) {
        this.distance += d;
        updateShowDistance();
    }
    public int getDistance() {
        return distance;
    }

    public void setCoins(int coin) {
        this.coinsCounter += coin;
        updateShowCoins();
    }
    public int getCoins() {
        return coinsCounter;
    }

    public void moveStone(){
        for(int i=0; i<tracks.length;i++){
            if(i == tracks.length-1 && tracks[tracks.length-1].findViewById(stone.getId()) != null){ // If the stone reached the last track then move it to the first track
                tracks[tracks.length-1].removeView(stone);
                tracks[0].addView(stone);
                break;
            }
            else if(i != 4 && tracks[i].findViewById(stone.getId()) != null){
                tracks[i].removeView(stone);
                tracks[i+1].addView(stone);
                break;
            }
        }
        stone.setY(0);
    }

    public void moveCoin(){
        for(int i=0; i<tracks.length;i++){
            if(i == tracks.length-1 && tracks[tracks.length-1].findViewById(coin.getId()) != null){ // If the coin reached the last track then move it to the first track
                tracks[tracks.length-1].removeView(coin);
                tracks[0].addView(coin);
                break;
            }
            else if(i != 4 && tracks[i].findViewById(coin.getId()) != null){
                tracks[i].removeView(coin);
                tracks[i+1].addView(coin);
                break;
            }
        }
        coin.setY(tracks[0].getMeasuredHeight()/3);
    }
}
