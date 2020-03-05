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
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private String level = "_1";
    private TextView lblLevel;

    public String getLevel() {
        return level;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        initialisation();
        animation();
//        getQuoteOfDay(); // call from QUOTE button
    }

    private void initialisation() {
        lblLevel = findViewById(R.id.lblLevel);
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



    public void translateClick(View v) {
        buttonTouchEffect(v);
        //TODO:...
    }

    public void quoteClick(View v) {
        buttonTouchEffect(v);
        //TODO:...
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
                dialog.dismiss();
                lblLevel.setText("Beginner");
                level = "_1";
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                lblLevel.setText("Medium");
                level = "_2";
            }
        });

        expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                lblLevel.setText("Expert");
                level = "_3";
            }
        });

        dialog.show();
    }

    public void timeChooserClick(View v) {
        buttonTouchEffect(v);
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    public void openWorkoutPlan(View v) {
        buttonTouchEffect(v);
        Intent intent = new Intent(this, WorkoutPlanActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void buttonTouchEffect(View v) {
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(AnimationUtils.loadAnimation(this, R.anim.click_anim_scale));
        set.addAnimation(AnimationUtils.loadAnimation(this, R.anim.click_anim_alpha));
        v.startAnimation(set);

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
