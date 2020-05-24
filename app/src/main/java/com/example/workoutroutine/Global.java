package com.example.workoutroutine;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Global extends Application {

    private static final String FILE_NAME = "data.txt";

    private String rank = "Cadet";
    private String alarm = "18:00";
    private int points = 0;
    private boolean running = false;
    private long startSteps = 0;
    private int level = 1;
    private boolean isDone = false;
    private boolean isReminderStarted = false;


    @Override
    public void onCreate() {
        super.onCreate();
        loadData();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        loadData();
    }

    private void loadData() {
        String data = "";
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);

            InputStreamReader reader = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(reader);
            StringBuilder stringBuilder = new StringBuilder();

            String text;
            while ((text = br.readLine()) != null) {
                stringBuilder.append(text).append("\n");
            }

            data = stringBuilder.toString();

        } catch (FileNotFoundException e) {
            //Application started first time on pone, let's create file and save the initial data
            saveData();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        parsingLoadedData(data.split("\n"));
    }

    private void parsingLoadedData(String[] rows) {
        if(rows.length != 8)
            return;
        this.rank = rows[0].split("=")[1];
        this.alarm = rows[1].split("=")[1];
        System.out.println(rows[2].split("=")[1]);
        this.points = Integer.parseInt(rows[2].split("=")[1]);
        this.running = ((rows[3].split("=")[1]).equals("true"))? true : false;
        this.startSteps = Integer.parseInt(rows[4].split("=")[1]);
        this.level = Integer.parseInt(rows[5].split("=")[1]);
        this.isDone = ((rows[6].split("=")[1]).equals("true"))? true : false;
        this.isReminderStarted = ((rows[7].split("=")[1]).equals("true"))? true : false;
    }

    public void saveData() {
        String data = "" +
                "rank="+ rank +"\n" +
                "alarm="+ alarm +"\n" +
                "points="+ points +"\n" +
                "running="+running +"\n" +
                "startSteps="+startSteps +"\n" +
                "level="+level +"\n" +
                "isDone="+ isDone +"\n" +
                "isReminderStarted="+ isReminderStarted;

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getRank() {
        return this.rank;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
        saveData();
    }
    public String getAlarm() {
        return this.alarm;
    }

    public void setPoints(int points) {
        this.points = points;
        if(points >= 2000)
            this.rank = "Master";
        else if(points >=1000)
            this.rank = "General";
        else if(points >= 500)
            this.rank = "Governor";
        else if(points >= 150)
            this.rank = "Warrior";
        else
            this.rank = "Cadet";
        saveData();
    }
    public int getPoints() {
        return this.points;
    }

    public void setRunning(boolean r) {
        this.running = r;
        saveData();
    }
    public boolean isRunning() {
        return this.running;
    }

    public void setStartSteps(long steps) {
        this.startSteps = steps;
        saveData();
    }
    public long getStartSteps() {
        return this.startSteps;
    }

    public void setLevel(int level) {
        this.level = level;
        saveData();
    }
    public int getLevel() {
        return this.level;
    }

    public void markAsDone() {
        isDone = true;
        saveData();
    }
    public void markAsUndone() {
        isDone = false;
        saveData();
    }
    public boolean isDone() {
        return isDone;
    }

    public boolean isReminderStarted() {
        return isReminderStarted;
    }
    public void setIsReminderStarted(boolean isStarted) {
        isReminderStarted = isStarted;
        saveData();
    }
}
