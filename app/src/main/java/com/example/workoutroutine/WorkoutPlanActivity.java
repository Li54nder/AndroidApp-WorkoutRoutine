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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class WorkoutPlanActivity extends AppCompatActivity {

    private String selectedDay;
    private String level;
    private String imgLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_workout_plan);

        Intent intent = getIntent();
        level = intent.getStringExtra("level");

        setSelectedDay();
        setImage();
    }

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

        for(final TextView day : days) {
            day.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    String tmp = (String) day.getText();
                    ((TextView)findViewById(R.id.lblLevel)).setText(tmp);
                    switch (tmp) {
                        case "Monday":
                            selectedDay = "_1";
                            break;
                        case "Tuesday":
                            selectedDay = "_2";
                            break;
                        case "Wednesday":
                            selectedDay = "_3";
                            break;
                        case "Thursday":
                            selectedDay = "_4";
                            break;
                        case "Friday":
                            selectedDay = "_5";
                            break;
                        case "Saturday":
                            selectedDay = "_6";
                            break;
                        case "Sunday":
                            selectedDay = "_7";
                            break;
                    }
                    setImage();
                }
            });
        }

        dialog.show();
    }

    public void openWorkout(View v) {
        buttonTouchEffect(v);
        Intent intent = new Intent(this, WorkoutActivity.class);
        intent.putExtra("imgLabel", imgLabel);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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




    private void setSelectedDay() {
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        selectedDay = "_"+(dayOfWeek-1);
        TextView lblDay = (TextView) findViewById(R.id.lblDay);
        switch (dayOfWeek) {
            case 1:
                lblDay.setText("Sunday");
                break;
            case 2:
                lblDay.setText("Monday");
                break;
            case 3:
                lblDay.setText("Tuesday");
                break;
            case 4:
                lblDay.setText("Wednesday");
                break;
            case 5:
                lblDay.setText("Thursday");
                break;
            case 6:
                lblDay.setText("Friday");
                break;
            case 7:
                lblDay.setText("Saturday");
                break;
        }
    }

    private void setImage() {
        imgLabel = "routine" + selectedDay + level;
        ((ImageView)findViewById(R.id.myZoomageView)).setImageResource(getApplicationContext().getResources().getIdentifier("drawable/"+imgLabel, null, this.getPackageName()));
    }



    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}
