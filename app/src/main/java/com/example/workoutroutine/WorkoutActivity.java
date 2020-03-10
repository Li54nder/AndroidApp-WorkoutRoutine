package com.example.workoutroutine;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class WorkoutActivity extends AppCompatActivity {

    private Global global;

    private String imgLabel;
    private int day;
    private int level;
    private int numOfLoops = 0;

    private final String[] videos = {"zkaUEMppvMY", "WfbMHKwupAQ", "JXQN7W9y_Tw", "Thj5jI2E_Yg", "P44AOYDUszo",
                                     "aSMQRP_I3WE", "m5kagPUZvH4", "XMEy5lv8aG0", "3ntyYNtHs_Y", "Qh6FTveg2tU"};

    private Chronometer lblStopwatch;
    private Button btnStopwatch;

    private boolean workoutStarted = false;
    private int loopCounter = 0;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        global = (Global) getApplicationContext();

        this.context = getApplicationContext();
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_workout);

        Intent intent = getIntent();
        imgLabel = intent.getStringExtra("imgLabel");

        initialisation();
        setImage();
    }

    private void initialisation() {
        btnStopwatch = findViewById(R.id.btnStopwatch);
        lblStopwatch = findViewById(R.id.lblStopwatch);
        lblStopwatch.setFormat("%s");

        day = Integer.parseInt(imgLabel.split("_")[1]);
        level = Integer.parseInt(imgLabel.split("_")[2]);

        if(level==1) {
            numOfLoops = 2;
        }
        else if(level==2) {
            numOfLoops = 3;
            if(day==7)
                numOfLoops--;
        }
        else {
            numOfLoops = 4;
            if(day==7)
                numOfLoops--;
        }
        if(day==3)
            numOfLoops++;
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
                    try {
                        Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "You have not any music player instaled!", Toast.LENGTH_LONG).show();
                    }
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
                    String video = videos[(new Random()).nextInt(videos.length)];
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+video));
                    appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+video));
                    webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        context.startActivity(appIntent);
                    } catch (Exception e) {
                        context.startActivity(webIntent);
                    }
                }
            }
        }).start();
    }

    public void startWorkout(View v) {
        buttonTouchEffect(v);
        workoutStarted = true;
        loopCounter++;
        if(loopCounter < numOfLoops)
            ((Button)v).setText("Next Cycle \n (cycle: "+loopCounter+" of "+numOfLoops+")");
        else if(loopCounter == numOfLoops)
            ((Button) v).setText("Finish \n (cycle: " +loopCounter+ " of "+numOfLoops+")");
        else {
            ((Button) v).setBackgroundResource(R.drawable.button_solid_green);

            AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutActivity.this);

            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_alert_dialog_done, null);

            builder.setCancelable(false);
            builder.setView(dialogView);

            Button btnOkay = dialogView.findViewById(R.id.btnOkay);

            final AlertDialog dialog = builder.create();
            if(dialog.getWindow() != null)
                dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;

            btnOkay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    global.setPoints(global.getPoints() + 30);
                    global.markAsDone(); //FOR TODAY... IN MIDNIGHT THIS WILL BE MARKED AS UNDONE
                    alertButtonTouchEffect(dialog, v);
                    finish();
                }
            });

            //Or show() only..?
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        //***Main Action***//
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                            }
                        });
                    }
                }
            }).start();
        }
    }

    public void giveUp(View v) {
        buttonTouchEffect(v);
        Button GIVE_UP = (Button) v;
        GIVE_UP.setBackgroundResource(R.drawable.button_solid_red);

        AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog_give_up, null);

        builder.setCancelable(false);
        builder.setView(dialogView);

        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnGiveUp = dialogView.findViewById(R.id.btnGiveUp);

        final AlertDialog dialog = builder.create();
        if(dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertButtonTouchEffect(dialog, v);
                GIVE_UP.setBackgroundResource(R.drawable.button_solid);
            }
        });
        btnGiveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                global.setPoints(global.getPoints() - 10);
                alertButtonTouchEffect(dialog, v);
                finish();
            }
        });

        //Or show() only..?
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //***Main Action***//
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.show();
                        }
                    });
                }
            }
        }).start();
    }

    public void buttonTouchEffect(View v) {
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(AnimationUtils.loadAnimation(this, R.anim.click_anim_scale));
        set.addAnimation(AnimationUtils.loadAnimation(this, R.anim.click_anim_alpha));
        v.startAnimation(set);

    }

    private void alertButtonTouchEffect(AlertDialog dialog, View v) {
        buttonTouchEffect(v);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    dialog.dismiss();
                }
            }
        }).start();
    }



    //**********************
    //STOPWATCH code: start
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
    //STOPWATCH code: end
    //**********************



    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
