package com.example.cooltimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView timeTextView;
    private TextView buttonTextView;
    private CountDownTimer timer;
    private MediaPlayer mediaPlayer;
    private Boolean isWorking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonTextView = findViewById(R.id.buttonView);
        timeTextView = findViewById(R.id.timeTextView);
        seekBar = findViewById(R.id.seekBar);

        seekBar.setMax(600);
        seekBar.setProgress(30);
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
    }

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
                    mediaPlayer = MediaPlayer.create(getApplicationContext(),
                            R.raw.bell_sound);
                    mediaPlayer.start();
                    timer.cancel();
                    timeTextView.setText("00:30");
                    seekBar.setProgress(30);
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

    private void updateTimer(int time) {
        int minutes = time / 60;
        int seconds = time - (minutes * 60);

        String minutesString = "";
        String secondsString = "";

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
}