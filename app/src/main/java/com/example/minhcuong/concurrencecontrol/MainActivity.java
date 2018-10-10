package com.example.minhcuong.concurrencecontrol;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

    ProgressBar myBar;

    EditText txtInput;
    Button btnDoItAgain;
    TextView txtPercent;

    int progressStep = 1;
    final int MAX_PROGRESS = 100;

    int accum = 0;
    int temp;

    Handler myHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myBar = findViewById(R.id.progressBar);
        txtInput = findViewById(R.id.txtInput);
        btnDoItAgain = findViewById(R.id.btnDoItAgain);
        txtPercent = findViewById(R.id.lbPercent);

        btnDoItAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyClick();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        accum = 0;
        myBar.setMax(MAX_PROGRESS);
        myBar.setProgress(0);
        myBar.setVisibility(View.VISIBLE);

        Thread myBackgroundThread = new Thread(backgroundTask, "backAlias1");
        myBackgroundThread.start();
    }

    private void xuLyClick() {
        onStart();
    }

    private Runnable foregroundRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                txtPercent.setText(accum + "%");
                myBar.incrementProgressBy(progressStep);
                accum += progressStep;
            } catch (Exception e) {
                Log.e("<<foregroundTask>>", e.getMessage());
            }
        }
    }; // foregroundTask

    private Runnable backgroundTask = new Runnable() {
        @Override
        public void run() {
            try {
                temp = Integer.parseInt(txtInput.getText().toString());
                for (int n = 0; n <= temp; n++) {
                    Thread.sleep(100);
                    myHandler.post(foregroundRunnable);
                }
            } catch (Exception e) {
                Log.e("<<foregroundTask>>", e.getMessage());
            }
        }// run
    };// backgroundTask
}
