package com.example.workoutroutine;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.security.NetworkSecurityPolicy;
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
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class HomeActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, SensorEventListener {

    private Global global;
    private TextView lblLevel;
    private TextView lblStepCount;
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
    private long steps;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION},1);
        }

        global = (Global) getApplicationContext();
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        initialisation();
        animation();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startAlarmService(String time) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SET_ALARM) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.SET_ALARM}, 1);
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split(":")[0]));
        c.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1])-1);
        c.set(Calendar.SECOND, 30);

        NotificationReceiver.AppContext = getApplicationContext();

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void initialisation() {
        lblLevel = findViewById(R.id.lblLevel);
        lblLevel.setText((global.getLevel()==1)? R.string.begginer : ((global.getLevel()==2)? R.string.medium : R.string.expert));
        lblStepCount = (TextView)findViewById(R.id.lblStepCounter);
        TextView lblTime = findViewById(R.id.lblTime);
        this.rank = global.getRank();
        this.points = global.getPoints();
        Button btnCounting = findViewById(R.id.btnServiceControl);


        lblTime.setText(global.getAlarm());
        lblTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                global.setAlarm(s.toString());
                startAlarmService(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        if(global.isReminderStarted()) {
            ((Button) findViewById(R.id.btnStartReminder)).setText(R.string.restart_reminder);
        } else {
            ((Button) findViewById(R.id.btnStartReminder)).setText(R.string.start_reminder);
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

//        if(global.getStartSteps() == 0) { //global.isRunning() ??????????????????????????????????????????????????????????
//            lblStepCount.setText("0");
//            btnCounting.setText("Start counting");
//        } else {
//            if(global.isRunning()) {
//                //nastavlja
//                startCounter();
//            } else {
//                //iz pocetka
//
//            }
//            btnCounting.setText("Stop counting");
//        }

        intRunning = global.isRunning()? 1 : 0;

        if(global.isRunning()) {
            btnCounting.setText(R.string.stopcounting);
            startCounter();
        } else {
            btnCounting.setText(R.string.start_counting);
            lblStepCount.setText("0");
        }
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
        findViewById(R.id.textView3).setAnimation(a1);
        findViewById(R.id.lblStepCounter).setAnimation(a1);
        findViewById(R.id.btnServiceControl).setAnimation(a1);
        //second showing
        findViewById(R.id.textView5).setAnimation(a2);
        findViewById(R.id.lblLevel).setAnimation(a2);
        findViewById(R.id.btnLvlChooser).setAnimation(a2);
        //third showing
        findViewById(R.id.lblTime).setAnimation(a3);
        findViewById(R.id.textView11).setAnimation(a3);
        findViewById(R.id.btnStartReminder).setAnimation(a3);
        findViewById(R.id.btnTimeChooser).setAnimation(a3);
        //fourth showing
        findViewById(R.id.btnWorkoutPlan).setAnimation(a4);
    }


//  *** Clicks ***

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void reminderClick(View v) {
        buttonTouchEffect(v);
        Button tmp = (Button) v;
        tmp.setText(R.string.restart_reminder);
        startAlarmService(global.getAlarm());
        global.setIsReminderStarted(true);

        //TODO: handle restarting service
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
        if(dialog.getWindow() != null) {
            dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
        }

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

                quoteText.setText(getQuoteOfDay());
                final AlertDialog dialog = builder.create();
                if(dialog.getWindow() != null)
                    dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertButtonTouchEffect(dialog, v);
                    }
                });
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

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    text.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
                }

                final AlertDialog dialog = builder.create();
                if(dialog.getWindow() != null) {
                    dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
                }

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertButtonTouchEffect(dialog, v);
                    }
                });

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
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                switchLang(new Locale("sr", "RS"));
                buttonTouchEffect(v);
            }
        });
        btnEng.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                switchLang(new Locale("en"));
                buttonTouchEffect(v);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertButtonTouchEffect(dialog, v);
            }
        });

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
        if(dialog.getWindow() != null) {
            dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
        }

        beginner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblLevel.setText(R.string.begginer);
                global.setLevel(1);
                alertButtonTouchEffect(dialog, v);
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblLevel.setText(R.string.medium);
                global.setLevel(2);
                alertButtonTouchEffect(dialog, v);
            }
        });
        expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblLevel.setText(R.string.expert);
                global.setLevel(3);
                alertButtonTouchEffect(dialog, v);
            }
        });

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

    //  *** Next activity ***
    public void openWorkoutPlan(View v) {
        buttonTouchEffect(v);
        Intent intent = new Intent(this, WorkoutPlanActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

//  *** SWITCH LANGUAGE ***
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void switchLang(Locale locale) {
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }


    public void controlService(View v) {
        buttonTouchEffect(v);
        if(!running) { // || global.running()
            ((Button)v).setText(R.string.stopcounting);
            startCounter();
        } else {
            ((Button)v).setText(R.string.start_counting);
            stopCounter();
        }
    }


//  *** TOUCH EFFECTS ***
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


//  *** QUOTE OF THE DAY ***
    private String getQuoteOfDay() {
        String quote = fetchData("http://quotes.rest/qod.json?category=inspire");
        if (quote == null) {
            System.out.println("**************************** -> Nema rezultata sa API-ja!");
            return quotes[new Random().nextInt(quotes.length)];
        } else
            System.out.println("**************************** -> RADI!");
            return quote;
    }
    private String fetchData(String url) {
        String quote = null;
        try {
            URL tmp = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) tmp.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            String results = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
            quote = parseResults(results);
            is.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return quote;
    }
    private String parseResults(String results) throws JSONException {
        JSONObject mainObj = new JSONObject(results);
        return mainObj.getJSONObject("contents").getJSONArray("quotes").getJSONObject(0).getString("quote");
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView lblTime = findViewById(R.id.lblTime);
        lblTime.setText((hourOfDay<10? "0"+hourOfDay : hourOfDay) + ":" + (minute<10? "0"+minute : minute));
    }



//  *** STEP COUNTER ***
    private SensorManager sensorManager;
    private boolean running = false;
    private int intRunning = 0;
    private long realSteps = 0;
    private long startSteps = 0;

    private void startCounter() {
        super.onResume();
        running = true;
//        global.setRunning(running);
        lblStepCount.setText("0");
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor for Step Counter not found!", Toast.LENGTH_SHORT).show();
        }
    }
    private void stopCounter() {
        super.onPause();
        running = false;
        global.setRunning(running);
        global.setStartSteps(0);
        intRunning = 0;
//        global.setSteps(0);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(running) {
            if(intRunning++ == 0) {
                // Tek pokrece stepCounter i za pocetno stanje koraka postavljamo trenutno
                startSteps = (long) event.values[0];
                global.setStartSteps(startSteps);
            }
            else {
                if(global.isRunning())  {
                    //RESUME...
                    intRunning = 1;
                    steps = (long) event.values[0] - global.getStartSteps();
                    lblStepCount.setText(String.valueOf((long) (event.values[0] - global.getStartSteps())));

                } else {
                    //RESTART...
                    intRunning = 1;
                    steps = (long) event.values[0] - startSteps;
                    lblStepCount.setText(String.valueOf((long) (event.values[0] - startSteps)));

                }
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onDestroy() {
        System.err.println("********************************* DESTROY ********************************************");
        super.onDestroy();
        global.setRunning(running);
//        global.setSteps(steps);
    }
}
