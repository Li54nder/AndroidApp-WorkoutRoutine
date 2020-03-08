package com.example.workoutroutine;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
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
import java.util.Random;

public class HomeActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private Global global;
    private TextView lblLevel;
    private String[] quotes = {
            "Push yourself, because no one else is going to do it for you.",
            "Sometimes later becomes never. Do it now.",
            "Great things never come from comfort zones.",
            "Don’t stop when you’re tired. Stop when you’re done.",
            "It’s going to be hard, but hard does not mean impossible.",
            "The secret of getting ahead is getting started."
    };
    private String rank;
    private int points;

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
        lblLevel.setText((global.getLevel()==1)? "BEGINNER" : ((global.getLevel()==2)? "MEDIUM" : "EXPERT"));
        ((TextView)findViewById(R.id.lblStepCounter)).setText(""+global.getSteps());
        TextView lblTime = findViewById(R.id.lblTime);
        this.rank = global.getRank();
        this.points = global.getPoints();

        lblTime.setText(global.getAlarm());
        lblTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                global.setAlarm(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void updateData() {
        this.rank = global.getRank();
        this.points = global.getPoints();
    }

    private void animation() {
        //Animations for showing
        Animation a1 = AnimationUtils.loadAnimation(this, R.anim.show_1);
        Animation a2 = AnimationUtils.loadAnimation(this, R.anim.show_2);
        Animation a3 = AnimationUtils.loadAnimation(this, R.anim.show_3);
        Animation a4 = AnimationUtils.loadAnimation(this, R.anim.show_4);
        //first showing
        findViewById(R.id.divider1).setAnimation(a1);
//        findViewById(R.id.divider2).setAnimation(a1);
        findViewById(R.id.textView3).setAnimation(a1);
        findViewById(R.id.lblStepCounter).setAnimation(a1);
        //second showing
//        findViewById(R.id.divider3).setAnimation(a2);
        findViewById(R.id.textView5).setAnimation(a2);
        findViewById(R.id.lblLevel).setAnimation(a2);
        findViewById(R.id.btnLvlChooser).setAnimation(a2);
        //third showing
//        findViewById(R.id.divider4).setAnimation(a3);
        findViewById(R.id.lblTime).setAnimation(a3);
        findViewById(R.id.textView11).setAnimation(a3);
        findViewById(R.id.btnTimeChooser).setAnimation(a3);
        findViewById(R.id.btnWorkoutPlan).setAnimation(a4);
    }



    public void rankClick(View v) {
        buttonTouchEffect(v);
        updateData();

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog_rank, null);

        builder.setCancelable(true);
        builder.setView(dialogView);

        Button btnCloseRank = dialogView.findViewById(R.id.btnCloseRank);
        ImageView imgRank = dialogView.findViewById(R.id.imgRank);
        TextView points = dialogView.findViewById(R.id.lblPoints);
        TextView lblRank = dialogView.findViewById(R.id.lblRank);

        lblRank.setText(this.rank);
        imgRank.setImageResource(getApplicationContext().getResources().getIdentifier("drawable/r_"+rank.toLowerCase(), null, this.getPackageName()));
        points.setText(this.points + (this.points < 150? "/150" :
                                        (this.points < 500? "/500" :
                                            (this.points < 1000? "/1000" :
                                                (this.points < 2000? "/2000" : "")))));

        final AlertDialog dialog = builder.create();
        if(dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;

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
        if(dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;

        btnQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertButtonTouchEffect(dialog, v);

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_alert_dialog_quote, null);

                builder.setCancelable(true);
                builder.setView(dialogView);

                TextView quoteText = dialogView.findViewById(R.id.lblQuoteText);
                Button btnClose = dialogView.findViewById(R.id.btnCloseQuote);

                quoteText.setText(getQuoteOfDay()); //REST NE RADI!!!
                final AlertDialog dialog = builder.create();
                if(dialog.getWindow() != null)
                    dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;

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
                            Thread.sleep(200);
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
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(26)
            public void onClick(View v) {
                alertButtonTouchEffect(dialog, v);

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_alert_dialog_info, null);

                builder.setCancelable(true);
                builder.setView(dialogView);

                Button btnClose = dialogView.findViewById(R.id.btnCloseInfo);
                TextView text = dialogView.findViewById(R.id.lblInfoText);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    text.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

                final AlertDialog dialog = builder.create();
                if(dialog.getWindow() != null)
                    dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;

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
                            Thread.sleep(200);
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
        if(dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;

        beginner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblLevel.setText("Beginner");
                global.setLevel(1);
                alertButtonTouchEffect(dialog, v);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblLevel.setText("Medium");
                global.setLevel(2);
                alertButtonTouchEffect(dialog, v);
            }
        });

        expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblLevel.setText("Expert");
                global.setLevel(3);
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

    private String getQuoteOfDay() {
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

            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quotes[new Random().nextInt(quotes.length)];
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView lblTime = findViewById(R.id.lblTime);
        lblTime.setText((hourOfDay<10? "0"+hourOfDay : hourOfDay) + ":" + (minute<10? "0"+minute : minute));
    }
}
