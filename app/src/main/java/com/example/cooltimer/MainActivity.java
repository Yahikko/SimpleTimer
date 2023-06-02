package com.example.cooltimer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SeekBar seekBar;
    private TextView timeTextView;
    private TextView buttonTextView;
    private CountDownTimer timer;
    private MediaPlayer mediaPlayer;
    private SharedPreferences sharedPreferences;
    private Boolean isWorking = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonTextView = findViewById(R.id.buttonView);
        timeTextView = findViewById(R.id.timeTextView);
        seekBar = findViewById(R.id.seekBar);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        seekBar.setMax(1800);
        seekBar.setMin(1);
        setIntervalFromSharedPreferences(sharedPreferences);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @SuppressLint("SetTextI18n")
    public void startTimer(View view) {
        if (isWorking) {
            buttonTextView.setText("START");
            timer.cancel();
            seekBar.setEnabled(true);
            isWorking = false;
        } else {
            timer = new CountDownTimer(seekBar.getProgress() * 1000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int timeNow = (int) (millisUntilFinished / 1000);
                    updateTimer(timeNow);
                    seekBar.setProgress(timeNow);
                }

                @Override
                public void onFinish() {
                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    if (sharedPreferences.getBoolean("enable_sound", true)) {
                        String melodyName = sharedPreferences
                                .getString("timer_melody", "bell");
                        switch (melodyName) {
                            case "bell":
                                mediaPlayer = MediaPlayer.create(getApplicationContext(),
                                        R.raw.bell_sound);
                                mediaPlayer.start();
                                break;
                            case "alarm_siren":
                                mediaPlayer = MediaPlayer.create(getApplicationContext(),
                                        R.raw.alarm_siren_sound);
                                mediaPlayer.start();
                                break;
                            case "bip":
                                mediaPlayer = MediaPlayer.create(getApplicationContext(),
                                        R.raw.bip_sound);
                                mediaPlayer.start();
                                break;
                        }
                    }
                    timer.cancel();
                    setIntervalFromSharedPreferences(sharedPreferences);
                    buttonTextView.setText("START");
                    seekBar.setEnabled(true);
                    isWorking = false;
                }
            }.start();
            buttonTextView.setText("PAUSE");
            seekBar.setEnabled(false);
            isWorking = true;
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateTimer(int time) {
        int minutes = time / 60;
        int seconds = time - (minutes * 60);

        String minutesString;
        String secondsString;

        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = String.valueOf(minutes);
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = String.valueOf(seconds);
        }
        timeTextView.setText(minutesString + ":" + secondsString);
    }

    //Создаем меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.timer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent openSettings = new Intent(this, SettingsActivity.class);
            startActivity(openSettings);
            return true;
        } else {
            Intent openAbout = new Intent(this, AboutActivity.class);
            startActivity(openAbout);
            return true;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setIntervalFromSharedPreferences(SharedPreferences sharedPreferences) {
        int defaultInterval = Integer.parseInt(sharedPreferences
                .getString("default_interval", "30"));
        updateTimer(defaultInterval);
        seekBar.setProgress(defaultInterval);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("default_interval")) {
            setIntervalFromSharedPreferences(sharedPreferences);
        }
    }
}