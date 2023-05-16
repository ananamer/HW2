package com.example.hw2;

import androidx.appcompat.app.AppCompatActivity;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class ButtonsGameActivity extends AppCompatActivity {

    Game buttonsGame = new Game();
    public ImageButton rightBtn;
    public ImageButton leftBtn;
    public RelativeLayout bottomBar;
    public int wastedLives;
    int coinPlace, stonePlace, carPlace;
    Timer timer = new Timer();
    Bundle playerBundle;
    Intent playerIntent;
    // moving the car right
    public void moveRight(){
        rightBtn.setOnClickListener(e->{
            if(buttonsGame.getTracks()[4].findViewById(buttonsGame.getCar().getId()) == null){ // If the car isn't the most right track then you can move it right
                for(int i=0; i<buttonsGame.getTracks().length;i++){ // Find the car and move it right
                    if(buttonsGame.getTracks()[i].findViewById(buttonsGame.getCar().getId()) != null){
                        buttonsGame.getTracks()[i].removeView(buttonsGame.getCar());
                        buttonsGame.getTracks()[i+1].addView(buttonsGame.getCar());
                        break;
                    }
                }
            }
        });
    }
    // moving the car left
    public void moveLeft(){
        leftBtn.setOnClickListener(e -> {
            if(buttonsGame.getTracks()[0].findViewById(buttonsGame.getCar().getId()) == null){ // If the car isn't the most left track then you can move it left
                for(int i=0; i<buttonsGame.getTracks().length;i++){ // Find the car and move it right
                    if(buttonsGame.getTracks()[i].findViewById(buttonsGame.getCar().getId()) != null){
                        buttonsGame.getTracks()[i].removeView(buttonsGame.getCar());
                        buttonsGame.getTracks()[i-1].addView(buttonsGame.getCar());
                        break;
                    }
                }
            }
        });
    }

    // moving the stone the tracks
    public void startButtonsGame(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        coinPlace = (int)buttonsGame.getCoin().getY();
                        stonePlace = (int)buttonsGame.getStone().getY();
                        carPlace = (int)buttonsGame.getCar().getY();

                        if(buttonsGame.getStone().getParent() == buttonsGame.getCar().getParent() && (stonePlace > carPlace-50 && stonePlace < carPlace+50)){ // If the stone touches the car then there is a crash
                            vibrate();
                            crashSound();

                            if(wastedLives == 2){
                                // If failed 3 times then start again
                                Toast.makeText(ButtonsGameActivity.this,"Game Over!",Toast.LENGTH_SHORT).show();
                                finishButtonsGame();
                            }

                            else {
                                for(int i = 0; i<buttonsGame.getHearts().length; i++) {
                                    if (buttonsGame.getHearts()[i].getVisibility() == VISIBLE) {
                                        buttonsGame.getHearts()[i].setVisibility(INVISIBLE);
                                        wastedLives++;
                                        break;
                                    }
                                }
                                Toast.makeText(ButtonsGameActivity.this,"oops! watch out!",Toast.LENGTH_SHORT).show();
                            }
                            buttonsGame.moveStone();
                        }
                        else if(stonePlace != 0.0 && stonePlace == bottomBar.getY()-100){
                            buttonsGame.moveStone();
                            buttonsGame.moveCoin();
                        }
                        else{
                            buttonsGame.getStone().setY(stonePlace + 1); // Move the stone in y axis
                            buttonsGame.getCoin().setY(coinPlace + 1); // Move the coin in y axis
                            buttonsGame.setDistance(2);
                        }
                        if(buttonsGame.getCoin().getParent() == buttonsGame.getCar().getParent() && (coinPlace > carPlace-50 && coinPlace < carPlace+50)) { // If the coin touches the car then the car won a coin
                            buttonsGame.setCoins(1);
                            buttonsGame.moveCoin();
                            coinSound();
                        }
                    }
                });
            }
        },0,3);
    }

    private void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    private void crashSound(){
        MediaPlayer crashSound= MediaPlayer.create(ButtonsGameActivity.this,R.raw.crash_sound);
        crashSound.start();
    }

    private void coinSound(){
        MediaPlayer coinSound= MediaPlayer.create(ButtonsGameActivity.this,R.raw.coin_sound);
        coinSound.start();
    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        Intent intent = new Intent(this,HomePageActivity.class);
        startActivity(intent);
        finish();
    }

    public void finishButtonsGame(){
        timer.cancel();
        Intent intent = new Intent(this,ScoresActivity.class);
        playerBundle.putInt("COINS",buttonsGame.getCoins());
        playerBundle.putInt("DISTANCE",buttonsGame.getDistance());
        intent.putExtra("playerBundle",playerBundle);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons_game);

        bottomBar = findViewById(R.id.buttons_container);
        leftBtn = findViewById(R.id.left_btn);
        rightBtn = findViewById(R.id.right_btn);

        buttonsGame.setCar(findViewById(R.id.racing_car));
        buttonsGame.setStone(findViewById(R.id.stone));
        buttonsGame.setCoin(findViewById(R.id.coin));
        buttonsGame.setShowCoins(findViewById(R.id.text_coins));
        buttonsGame.setShowDistance(findViewById(R.id.text_distance));

        buttonsGame.setTracks(findViewById(R.id.track1_inner_container));
        buttonsGame.setTracks(findViewById(R.id.track2_inner_container));
        buttonsGame.setTracks(findViewById(R.id.track3_inner_container));
        buttonsGame.setTracks(findViewById(R.id.track4_inner_container));
        buttonsGame.setTracks(findViewById(R.id.track5_inner_container));

        buttonsGame.setHearts(findViewById(R.id.heart1));
        buttonsGame.setHearts(findViewById(R.id.heart2));
        buttonsGame.setHearts(findViewById(R.id.heart3));

        wastedLives = 0;

        playerIntent = getIntent();
        playerBundle = playerIntent.getBundleExtra("playerBundle");

        moveRight();
        moveLeft();
        startButtonsGame();
    }
}