package com.example.workoutroutine;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private Global global;
//    private String level = "_1";
    private TextView lblLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        global = (Global) getApplicationContext();
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        initialisation();
        animation();
    }

    private void initialisation() {
        lblLevel = findViewById(R.id.lblLevel);
        ((TextView)findViewById(R.id.lblStepCounter)).setText(""+global.getSteps());
    }

    private void animation() {
        //Animations for showing
        Animation a1 = AnimationUtils.loadAnimation(this, R.anim.show_1);
        Animation a2 = AnimationUtils.loadAnimation(this, R.anim.show_2);
        Animation a3 = AnimationUtils.loadAnimation(this, R.anim.show_3);
        Animation a4 = AnimationUtils.loadAnimation(this, R.anim.show_4);
        //first showing
        findViewById(R.id.divider1).setAnimation(a1);
        findViewById(R.id.divider2).setAnimation(a1);
        findViewById(R.id.textView3).setAnimation(a1);
        findViewById(R.id.lblStepCounter).setAnimation(a1);
        //second showing
        findViewById(R.id.divider3).setAnimation(a2);
        findViewById(R.id.textView5).setAnimation(a2);
        findViewById(R.id.lblLevel).setAnimation(a2);
        findViewById(R.id.btnLvlChooser).setAnimation(a2);
        //third showing
        findViewById(R.id.divider4).setAnimation(a3);
        findViewById(R.id.lblTime).setAnimation(a3);
        findViewById(R.id.textView11).setAnimation(a3);
        findViewById(R.id.btnTimeChooser).setAnimation(a3);
        findViewById(R.id.btnWorkoutPlan).setAnimation(a4);
    }



    public void rankClick(View v) {
        buttonTouchEffect(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog_rank, null);

        builder.setCancelable(true);
        builder.setView(dialogView);

        Button btnCloseRank = dialogView.findViewById(R.id.btnCloseRank);
        ImageView imgRank = dialogView.findViewById(R.id.imgRank);

        final AlertDialog dialog = builder.create();

        btnCloseRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertButtonTouchEffect(dialog, v);
            }
        });

        imgRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonTouchEffect(v);
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

    public void moreClick(View v) {
        buttonTouchEffect(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog_more, null);

        builder.setCancelable(true);
        builder.setView(dialogView);

        LinearLayout btnQuote = dialogView.findViewById(R.id.btnQuote);
        LinearLayout btnInfo = dialogView.findViewById(R.id.btnInfo);
        ImageView btnSrb = dialogView.findViewById(R.id.btnSrb);
        ImageView btnEng = dialogView.findViewById(R.id.btnEng);
        Button btnClose = dialogView.findViewById(R.id.btnCloseMore);

        final AlertDialog dialog = builder.create();

        btnQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:Quote...
                //getQuoteOfDay(); // call from QUOTE button
                alertButtonTouchEffect(dialog, v);
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:PopUp info...
                alertButtonTouchEffect(dialog, v);
            }
        });

        btnSrb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:Translate...
                buttonTouchEffect(v);
            }
        });

        btnEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:Translate...
                buttonTouchEffect(v);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertButtonTouchEffect(dialog, v);
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

    public void lvlChooserClick(View v) {
        buttonTouchEffect(v);

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog_level, null);

        builder.setCancelable(true);
        builder.setView(dialogView);

        TextView beginner = dialogView.findViewById(R.id.lblBeginner);
        TextView medium = dialogView.findViewById(R.id.lblMedium);
        TextView expert = dialogView.findViewById(R.id.lblExpert);

        final AlertDialog dialog = builder.create();

        beginner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblLevel.setText("Beginner");
                global.setLevel(1);
//                level = "_1";
                alertButtonTouchEffect(dialog, v);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblLevel.setText("Medium");
                global.setLevel(2);
//                level = "_2";
                alertButtonTouchEffect(dialog, v);
            }
        });

        expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblLevel.setText("Expert");
                global.setLevel(3);
//                level = "_3";
                alertButtonTouchEffect(dialog, v);
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

    public void timeChooserClick(View v) {
        buttonTouchEffect(v);
        DialogFragment timePicker = new TimePickerFragment();

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
                    timePicker.show(getSupportFragmentManager(), "time picker");
                }
            }
        }).start();
    }

    public void openWorkoutPlan(View v) {
        buttonTouchEffect(v);
        Intent intent = new Intent(this, WorkoutPlanActivity.class);
        intent.putExtra("level", "_"+global.getLevel());

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void buttonTouchEffect(View v) {
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

    private void getQuoteOfDay() {
        try {
            URL url = new URL("http://quotes.rest/qod.json?category=inspire&language=en");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if(conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String output;
            System.out.println("Output from Server: \n");
            while((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();
        } catch (Exception e) {
            // getSomePreDefinedQuotes
            e.printStackTrace();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView lblTime = findViewById(R.id.lblTime);
        lblTime.setText((hourOfDay<10? "0"+hourOfDay : hourOfDay) + ":" + (minute<10? "0"+minute : minute));
    }
}
