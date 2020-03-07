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

    private String rank = "Cadet"; //Cadet
    private String alarm = "08:00"; //08:00
    private int points = 0; //0
    private int steps = 0; //0
    private int level = 1; //1
    private boolean isDone = false; //false



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
        //***Parsing data***// //WORKS !!!
        String[] rows = data.split("\n");
        if(rows.length == 6) {
            this.rank = rows[0].split("=")[1];
            this.alarm = rows[1].split("=")[1];
            System.out.println(rows[2].split("=")[1]);
            this.points = Integer.parseInt(rows[2].split("=")[1]);
            this.steps = Integer.parseInt(rows[3].split("=")[1]);
            this.level = Integer.parseInt(rows[4].split("=")[1]);
            this.isDone = ((rows[5].split("=")[1]).equals("true"))? true : false;
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
        System.out.println("*********************************************************************** : " + rows.length);
        if(rows.length == 6) {
            this.rank = rows[0].split("=")[1];
            this.alarm = rows[1].split("=")[1];
            this.points = Integer.parseInt(rows[2].split("=")[1]);
            this.steps = Integer.parseInt(rows[3].split("=")[1]);
            this.level = Integer.parseInt(rows[4].split("=")[1]);
            this.isDone = (rows[2].split("=")[5].equals("true"))? true : false;
        }
    }

    public void saveData() {
        String data = "" +
                "rank="+ rank +"\n" +
                "alarm="+ alarm +"\n" +
                "points="+ points +"\n" +
                "steps="+steps +"\n" +
                "level="+level +"\n" +
                "isDone="+ isDone;

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
