package com.example.testapk1;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {

    public static final String serverURL = "http://192.168.252.14:5000";

    int MICROPHONE_PERMISSION_CODE=200;

    Button buttonStartListener, buttonStop, buttonStartRecommender;
    EditText editTextTime, editTextWord;

    Intent serviceIntent;

    public static int occr;

    public static Intent serviceIntent2;

    public static String inputWord;

    public boolean recomm=false;

    public static String[] response2;



    boolean isStart= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonStartListener=findViewById(R.id.buttonStartListener);
        editTextTime=findViewById(R.id.editTextTime);
        buttonStop=findViewById(R.id.buttonStop);
        buttonStartRecommender=findViewById(R.id.buttonStartRecommender);
        editTextWord=findViewById(R.id.editTextWord);


        buttonStartListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStartListenerPressed();
            }
        });

        buttonStartRecommender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStartRecommenderPressed();
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStopPressed();
            }
        });


        Intent intent3 = getIntent();
        if (intent3 != null && getIntent().getAction() != null && getIntent().getAction().equals("STOP_FOREGROUND_SERVICE_ACTION")) {
            // Sto
            isStart = false;
            Log.d("main Activity","notification pressed");
            buttonStopPressed();
            if(!recomm) {
                showAlertDialog();
            }
            if(recomm){
                showAlertDialog1();
            }
        }


    }

    private void showAlertDialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        String word = editTextWord.getText().toString();

        // Set the dialog title and message
        builder.setTitle("Done Listening")
                .setMessage(inputWord+" said "+Integer.toString(occr)+" times");

        // Set the positive button (OK) and its click listener
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Perform the desired action when OK is clicked
                // You can leave this method empty if no action is needed
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void buttonStartRecommenderPressed() {
        String inputTime =editTextTime.getText().toString();

        if(isStart){
            Toast.makeText(this, "Already Running", Toast.LENGTH_SHORT).show();
            return;
        }
        if(isMicrophonePresent()){
            getMicrophonePermission();
        }
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && !foregroundServiceRunning()) {
            serviceIntent = new Intent(this, MyService.class);
            serviceIntent.putExtra("Choice","Recommend");
            serviceIntent.putExtra("inputTime", inputTime);
            startService(serviceIntent);
            recomm=true;
            isStart = true;
        }
        else{if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){getMicrophonePermission();}}
    }

    private void buttonStopPressed() {
        if(isStart==true) {
            stopService(serviceIntent);
            isStart=false;
        }
    }

    public boolean foregroundServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if(MyService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void buttonStartListenerPressed() {
        String inputTime =editTextTime.getText().toString();
        inputWord = editTextWord.getText().toString();
        if(isStart){
            Toast.makeText(this, "Already Running", Toast.LENGTH_SHORT).show();
            return;
        }
        if(isMicrophonePresent()){
            getMicrophonePermission();
        }
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && !foregroundServiceRunning()) {
            serviceIntent = new Intent(this, MyService.class);
            serviceIntent.putExtra("Choice","Listener");
            serviceIntent.putExtra("inputTime", inputTime);
            serviceIntent.putExtra("inputWord",inputWord);
            recomm=false;
            startService(serviceIntent);
        Toast.makeText(this, "Started Recording", Toast.LENGTH_LONG).show();
            isStart = true;
        }
        else{if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){getMicrophonePermission();}}
    }

    private boolean isMicrophonePresent() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            return true;
        } else {
            return false;
        }
    }

    private void getMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
        }
    }

    public void showAlertDialog() {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        String word = editTextWord.getText().toString();

        // Set the dialog title and message
        builder.setTitle("Recommendations:")
                .setMessage(response2[1] + " : "+ response2[2]);

        // Set the positive button (OK) and its click listener
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Perform the desired action when OK is clicked
                // You can leave this method empty if no action is needed
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}