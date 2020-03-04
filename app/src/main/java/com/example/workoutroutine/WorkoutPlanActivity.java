package com.example.workoutroutine;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class WorkoutPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_workout_plan);

    }

    public void goBack(View v) {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    private void showDayChooser(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutPlanActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog_day, null);

        builder.setCancelable(false);
        builder.setView(dialogView);

        TextView[] days = {dialogView.findViewById(R.id.lblSunday), };

//        TextView beginner = dialogView.findViewById(R.id.lblBeginner);
//        TextView medium = dialogView.findViewById(R.id.lblMedium);
//        TextView expert = dialogView.findViewById(R.id.lblExpert);

        final AlertDialog dialog = builder.create();

//        beginner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                lblLevel.setText("Beginner");
//                level = "_1";
//            }
//        });
//
//        medium.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                lblLevel.setText("Medium");
//                level = "_2";
//            }
//        });
//
//        expert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                lblLevel.setText("Expert");
//                level = "_3";
//            }
//        });

        dialog.show();
    }
}
