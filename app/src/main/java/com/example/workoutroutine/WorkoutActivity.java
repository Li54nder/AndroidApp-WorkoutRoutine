package com.example.workoutroutine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorkoutActivity extends AppCompatActivity {

    private String imgLabel;
    private TextView lblMilisec;
    private Thread milisecThread;
    private Chronometer chronometer;
    private Button btnStopwatch;

    private boolean workoutStarted = false;
    private int loopCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_workout);

        Intent intent = getIntent();
        imgLabel = intent.getStringExtra("imgLabel");

        ((ImageView)findViewById(R.id.myZoomageView)).setImageResource(getApplicationContext().getResources().getIdentifier("drawable/"+imgLabel, null, this.getPackageName()));

        lblMilisec = findViewById(R.id.lblMilisec);
        btnStopwatch = findViewById(R.id.btnStopwatch);
        chronometer = findViewById(R.id.lblStopwatch);
        chronometer.setFormat("%s");
    }

    public void goBack(View v) {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    private boolean running;
    private long pauseOffset;
    int br = 0;
    public void btnStopwatch(View v) {
        if(!workoutStarted) {
            Toast.makeText(getApplicationContext(), "You must start training first!", Toast.LENGTH_SHORT).show();
            return;
        }
        if((br = (br+1) % 2) != 0) {
            startStopwatch();
            btnStopwatch.setText("Pause Stopwatch");
        } else {
            pauseStopwatch();
            btnStopwatch.setText("Resume Stopwatch");
        }
    }
//    class Brojac {
//        int i;
//        Brojac() {
//            i = 0;
//        }
//    }
//    private Object a = new Object();
//    private AtomicBoolean go = new AtomicBoolean(false);
    private void startStopwatch() {
//        synchronized (a) {
//            go.set(true);
//        }
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
//            milisecThread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    String mils[] = {".05", ".10", ".15", ".20", ".25", ".30", ".35", ".40", ".45", ".50", ".55", ".00"};
//                    Brojac br = new Brojac();
//                    while(go.get()) {
//                        try {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    lblMilisec.setText(mils[br.i]);
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        br.i = (br.i + 1) % 12;
//                    }
//                }
//            });
//            milisecThread.start();
        }
    }

    private void pauseStopwatch() {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
//            synchronized (a) {
//                go.set(false);
//            }
        }
    }

    public void resetStopwatch(View v) {
        btnStopwatch.setText("Start Stopwatch");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.stop();
        pauseOffset = 0;
        running = false;
    }

    public void startWorkout(View v) {
        workoutStarted = true;
        if(loopCounter < 1) //LIMIT HARD CODED !!!
            ((Button)v).setText("Next Cycle \n (cycle: "+(++loopCounter)+")");
        else if(loopCounter == 1)
            ((Button) v).setText("Finish \n (cycle: " + (++loopCounter) + ")");
        else {
            findViewById(R.id.workoutBackground).setBackgroundColor(Color.GREEN);
            finish();
        }
    }

    public void giveUp(View v) {
        findViewById(R.id.workoutBackground).setBackgroundColor(Color.RED);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                WorkoutActivity.this.finish();
//            }
//        }).start();
        finish();
    }
}
