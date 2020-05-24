package com.example.workoutroutine;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class WorkoutPlanActivity extends AppCompatActivity {

    private Global global;

    private String selectedDay;
    private String imgLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        global = (Global) getApplicationContext();
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_workout_plan);

        setSelectedDay();
        setImage();
    }

    private void setSelectedDay() {
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        selectedDay = "_"+(dayOfWeek-1);
        TextView lblDay = (TextView) findViewById(R.id.lblDay);
        Button btnWorkout = (Button) findViewById(R.id.btnWorkout);
        switch (dayOfWeek) {
            case 1:
                lblDay.setText(R.string.sunday);
                selectedDay = "_7";
                btnWorkout.setText(R.string.legDay);
                break;
            case 2:
                lblDay.setText(R.string.monday);
                selectedDay = "_1";
                btnWorkout.setText(R.string.fullBody);
                break;
            case 3:
                lblDay.setText(R.string.tuesday);
                selectedDay = "_2";
                btnWorkout.setText(R.string.back);
                break;
            case 4:
                lblDay.setText(R.string.wednesday);
                selectedDay = "_3";
                btnWorkout.setText(R.string.upperBody);
                break;
            case 5:
                lblDay.setText(R.string.thursday);
                selectedDay = "_4";
                btnWorkout.setText(R.string.abs);
                break;
            case 6:
                lblDay.setText(R.string.friday);
                selectedDay = "_5";
                btnWorkout.setText(R.string.triceps);
                break;
            case 7:
                lblDay.setText(R.string.saturday);
                selectedDay = "_6";
                btnWorkout.setText(R.string.fullBody);
                break;
        }
    }

    private void setImage() {
        imgLabel = "routine" + selectedDay + "_" + global.getLevel();
        ((ImageView)findViewById(R.id.myZoomageView)).setImageResource(getApplicationContext().getResources().getIdentifier("mipmap/"+imgLabel, null, this.getPackageName()));
    }

//  *** Clicks ***
    public void showDayChooser(View v) {
        buttonTouchEffect(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutPlanActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_alert_dialog_day, null);

        builder.setCancelable(true);
        builder.setView(dialogView);

        TextView[] days = { dialogView.findViewById(R.id.lblMonday),
                            dialogView.findViewById(R.id.lblTuesday),
                            dialogView.findViewById(R.id.lblWednesday),
                            dialogView.findViewById(R.id.lblThursday),
                            dialogView.findViewById(R.id.lblFriday),
                            dialogView.findViewById(R.id.lblSaturday),
                            dialogView.findViewById(R.id.lblSunday),
        };

        final AlertDialog dialog = builder.create();
        if(dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;

        Button btnWorkout = (Button) findViewById(R.id.btnWorkout);

        for(final TextView day : days) {
            day.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tmp = (String) day.getText();
                    ((TextView)findViewById(R.id.lblDay)).setText(tmp);
                    switch (tmp) {
                        case "Monday":
                        case "Ponedeljak":
                            selectedDay = "_1";
                            btnWorkout.setText(R.string.fullBody);
                            break;
                        case "Tuesday":
                        case "Utorak":
                            selectedDay = "_2";
                            btnWorkout.setText(R.string.back);
                            break;
                        case "Wednesday":
                        case "Sreda":
                            selectedDay = "_3";
                            btnWorkout.setText(R.string.upperBody);
                            break;
                        case "Thursday":
                        case "Četvrtak":
                            selectedDay = "_4";
                            btnWorkout.setText(R.string.abs);
                            break;
                        case "Friday":
                        case "Petak":
                            selectedDay = "_5";
                            btnWorkout.setText(R.string.triceps);
                            break;
                        case "Saturday":
                        case "Subota":
                            selectedDay = "_6";
                            btnWorkout.setText(R.string.fullBody);
                            break;
                        case "Sunday":
                        case "Nedelja":
                            selectedDay = "_7";
                            btnWorkout.setText(R.string.legDay);
                            break;
                    }
                    setImage();
                    alertButtonTouchEffect(dialog, v);
                }
            });
        }

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

    public void openWorkout(View v) {
        buttonTouchEffect(v);
        if(!global.isDone()) {
            Intent intent = new Intent(this, WorkoutActivity.class);
            intent.putExtra("imgLabel", imgLabel);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            Toast.makeText(getApplicationContext(), R.string.alert, Toast.LENGTH_LONG).show();
            global.markAsUndone();
        }
    }

    public void goBack(View v) {
        buttonTouchEffect(v);
        finish();
    }

//  *** Touch Effects ***
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


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}
