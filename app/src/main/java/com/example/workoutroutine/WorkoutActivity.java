package com.example.workoutroutine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WorkoutActivity extends AppCompatActivity {

    private TextView lblMilisec;
    private Thread milisecThread;

    private String imgLabel;

    private Chronometer lblStopwatch;
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

        initialisation();
        setImage();
    }

    private void initialisation() {
        lblMilisec = findViewById(R.id.lblMilisec); //NEVER USED!!!
        btnStopwatch = findViewById(R.id.btnStopwatch);
        lblStopwatch = findViewById(R.id.lblStopwatch);
        lblStopwatch.setFormat("%s");
    }

    private void setImage() {
        ((ImageView)findViewById(R.id.myZoomageView)).setImageResource(getApplicationContext().getResources().getIdentifier("mipmap/"+imgLabel, null, this.getPackageName()));
    }


    public void openMusic(View v) {
        buttonTouchEffect(v);

        //Or finally only..?
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //***All logic goes here***//

                }
            }
        }).start();
    }

    public void openYoutube(View v) {
        buttonTouchEffect(v);

        //Or finally only..?
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //***All logic goes here***//

                }
            }
        }).start();
    }

    public void startWorkout(View v) {
        buttonTouchEffect(v);
        workoutStarted = true;
        if(loopCounter < 1) //LIMIT HARD CODED !!!
            ((Button)v).setText("Next Cycle \n (cycle: "+(++loopCounter)+")");
        else if(loopCounter == 1)
            ((Button) v).setText("Finish \n (cycle: " + (++loopCounter) + ")");
        else {
            ((Button) v).setBackgroundResource(R.drawable.button_solid_green);
            finish();
        }
    }

    public void giveUp(View v) {
        buttonTouchEffect(v);
        ((Button) v).setBackgroundResource(R.drawable.button_solid_red);
        finish();
    }

    public void goBack(View v) {
        finish();
    }

    public void buttonTouchEffect(View v) {
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(AnimationUtils.loadAnimation(this, R.anim.click_anim_scale));
        set.addAnimation(AnimationUtils.loadAnimation(this, R.anim.click_anim_alpha));
        v.startAnimation(set);

    }



    //**********************
    //STOPWATCH Process start
    private boolean running;
    private long pauseOffset;
    int br = 0; //start or pause stopwatch

    public void btnStopwatch(View v) {
        buttonTouchEffect(v);
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
    private void startStopwatch() {
        if (!running) {
            lblStopwatch.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            lblStopwatch.start();
            running = true;
        }
    }
    private void pauseStopwatch() {
        if (running) {
            lblStopwatch.stop();
            pauseOffset = SystemClock.elapsedRealtime() - lblStopwatch.getBase();
            running = false;
        }
    }

    public void resetStopwatch(View v) {
        buttonTouchEffect(v);
        btnStopwatch.setText("Start Stopwatch");
        lblStopwatch.setBase(SystemClock.elapsedRealtime());
        lblStopwatch.stop();
        pauseOffset = 0;
        running = false;
    }
    //STOPWATCH Process end
    //**********************



    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
