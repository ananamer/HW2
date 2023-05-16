package com.example.hw2;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class SensorsGameActivity extends AppCompatActivity {

    Game sensorsGame = new Game();
    Timer timer = new Timer();
    Bundle playerBundle;
    Intent playerIntent;
    int roadLength, coinPlace, stonePlace, carPlace;
    int y=1;
    public int wastedLives;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEvent = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float z = event.values[1];
            if(x < -3){
                if(sensorsGame.getTracks()[4].findViewById(sensorsGame.getCar().getId()) == null){
                    for(int i=0; i<sensorsGame.getTracks().length;i++){ // Find the car and move it right
                        if(sensorsGame.getTracks()[i].findViewById(sensorsGame.getCar().getId()) != null){
                            sensorsGame.getTracks()[i].removeView(sensorsGame.getCar());
                            sensorsGame.getTracks()[i+1].addView(sensorsGame.getCar());
                            break;
                        }
                    }
                }
            }
            if(x > 3) {
                if(sensorsGame.getTracks()[0].findViewById(sensorsGame.getCar().getId()) == null){
                    for(int i=0; i<sensorsGame.getTracks().length;i++){ // Find the car and move it right
                        if(sensorsGame.getTracks()[i].findViewById(sensorsGame.getCar().getId()) != null){
                            sensorsGame.getTracks()[i].removeView(sensorsGame.getCar());
                            sensorsGame.getTracks()[i-1].addView(sensorsGame.getCar());
                            break;
                        }
                    }
                }
            }

//            try moving the phone in z axis - move the phone back and forward
//            if z>0  -> moving backward -> move slower
//            if z<0  -> moving forward  -> move faster
            if(z>2)
                y=1;

            if(z < -2)
                y=2;
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(sensorEvent,sensor,sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(sensorEvent);
    }

    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        Intent intent = new Intent(this,HomePageActivity.class);
        startActivity(intent);
        finish();
    }

    public void finishSensorsGame(int coins,int distance){
        Toast.makeText(SensorsGameActivity.this,"Game Over",Toast.LENGTH_SHORT).show();
        timer.cancel();
        Intent intent = new Intent(this,ScoresActivity.class);
        playerBundle.putInt("COINS",sensorsGame.getCoins());
        playerBundle.putInt("DISTANCE",sensorsGame.getDistance());
        intent.putExtra("playerBundle",playerBundle);
        startActivity(intent);
        finish();
    }
    // moving the stone in the tracks
    public void startSensorsGame(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        roadLength = sensorsGame.getTracks()[0].getMeasuredHeight();
                        coinPlace = (int)sensorsGame.getCoin().getY();
                        stonePlace = (int)sensorsGame.getStone().getY();
                        carPlace = (int)sensorsGame.getCar().getY();

                        if(sensorsGame.getStone().getParent() == sensorsGame.getCar().getParent() && (stonePlace > carPlace-50 && stonePlace < carPlace+50)){ // If the stone touches the car then there is a crash
                            vibrate();
                            crashSound();

                            if(wastedLives == 2){ // If 3 times then start again
                                Toast.makeText(SensorsGameActivity.this,"Game Over!",Toast.LENGTH_SHORT).show();
                                finishSensorsGame(sensorsGame.getCoins(),sensorsGame.getDistance());
                            }

                            else {
                                for(int i = 0; i<sensorsGame.getHearts().length; i++) {
                                    if (sensorsGame.getHearts()[i].getVisibility() == VISIBLE) {
                                        sensorsGame.getHearts()[i].setVisibility(INVISIBLE);
                                        wastedLives++;
                                        break;
                                    }
                                }
                                Toast.makeText(SensorsGameActivity.this,"oops! watch out!",Toast.LENGTH_SHORT).show();
                            }
                            sensorsGame.moveStone();
                        }

                        else if(stonePlace != 0.0 && (roadLength-100 == sensorsGame.getStone().getY() || roadLength-99 == sensorsGame.getStone().getY())){
                            sensorsGame.moveStone();
                            sensorsGame.moveCoin();
                        }
                        else{
                            sensorsGame.getStone().setY(stonePlace + y); // Move the stone in y axis
                            sensorsGame.getCoin().setY(coinPlace + y); // Move the coin in y axis
                            sensorsGame.setDistance(2);
                        }
                        if(sensorsGame.getCoin().getParent() == sensorsGame.getCar().getParent() && (coinPlace > carPlace-50 && coinPlace < carPlace+50)) { // If the coin touches the car then the car won a coin
                            sensorsGame.setCoins(1);
                            sensorsGame.moveCoin();
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
        MediaPlayer crashSound= MediaPlayer.create(SensorsGameActivity.this,R.raw.crash_sound);
        crashSound.start();
    }

    private void coinSound(){
        MediaPlayer coinSound= MediaPlayer.create(SensorsGameActivity.this,R.raw.coin_sound);
        coinSound.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors_game);

        sensorsGame.setCar(findViewById(R.id.racing_car));
        sensorsGame.setStone(findViewById(R.id.stone));
        sensorsGame.setCoin(findViewById(R.id.coin));
        sensorsGame.setShowCoins(findViewById(R.id.text_coins));
        sensorsGame.setShowDistance(findViewById(R.id.text_distance));

        sensorsGame.setTracks(findViewById(R.id.track1_inner_container));
        sensorsGame.setTracks(findViewById(R.id.track2_inner_container));
        sensorsGame.setTracks(findViewById(R.id.track3_inner_container));
        sensorsGame.setTracks(findViewById(R.id.track4_inner_container));
        sensorsGame.setTracks(findViewById(R.id.track5_inner_container));

        sensorsGame.setHearts(findViewById(R.id.heart1));
        sensorsGame.setHearts(findViewById(R.id.heart2));
        sensorsGame.setHearts(findViewById(R.id.heart3));

        wastedLives = 0;

        playerIntent = getIntent();
        playerBundle = playerIntent.getBundleExtra("playerBundle");

        initSensor();
        startSensorsGame();
    }
}