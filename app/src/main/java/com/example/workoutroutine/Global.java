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
    private String alarm = "09:00";
    private int points = 0;
    private int steps = 16;
    private int level = 1;
    private boolean isDone = false;



    @Override
    public void onCreate() {
        super.onCreate();
        loadDataOnce();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        loadDataOnce();
    }

    private void loadDataOnce() {
        System.err.println("************************************************************** : LOADING START");
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
            System.err.println("************************************************************** : LOADING DONE");
        } catch (FileNotFoundException e) {
            System.err.println("************************************************************** : LOADING CRASHED");
            System.err.println("************************************************************** : File created");
            saveData();
        } catch (IOException e) {
            System.err.println("************************************************************** : LOADING BROCKEN");
            System.err.println("************************************************************** : FILEN NOT CRATED");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String[] rows = data.split("\n");
        if(rows.length == 3) {
            rank = rows[0].split(":")[1];
            points = Integer.parseInt(rows[1].split(":")[1]);
            isDone = (rows[2].split(":")[1].equals("true"))? true : false;
        }
    }

    public void loadData() {
        String data = "";

        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader reader = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(reader);
            StringBuilder stringBuilder = new StringBuilder();
            String text;
            while((text = br.readLine()) != null) {
                stringBuilder.append(text).append("\n");
            }
            data = stringBuilder.toString();
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
        //***Parsing data***//
        String[] rows = data.split("\n");
        if(rows.length == 3) {
            rank = rows[0].split(":")[1];
            points = Integer.parseInt(rows[1].split(":")[1]);
            isDone = (rows[2].split(":")[1].equals("true"))? true : false;
        }
    }

    public void saveData() {
        String data = "" +
                "rank:"+ rank +"\n"+
                "points:"+ points +"\n"+
                "isDone:"+ isDone;

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

    public void setRank(String rank) {
        this.rank = rank;
        saveData();
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
        saveData();
    }
    public int getPoints() {
        return this.points;
    }

    public void setSteps(int steps) {
        this.steps = steps;
        saveData();
    }
    public int getSteps() {
        return this.steps;
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
}
