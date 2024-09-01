package com.example.testapk1;



import static com.example.testapk1.MainActivity.inputWord;
import static com.example.testapk1.MainActivity.occr;
import static com.example.testapk1.MainActivity.serverURL;
import static com.example.testapk1.MainActivity.response2;

import static java.lang.Integer.parseInt;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Provider;

public class MyService extends Service {

    MediaRecorder mediaRecorder;

//    private Handler handler = new Handler();

    private final int RECORDING_DURATION = 60*1000 + 1000;

    private boolean isRecording = false;

    private int numb = 1;

    String Choice;

    String response = "";

    boolean isSent = false;

    int totalSent = 0;

    int number;

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";

    int bytesRead, bytesAvailable, bufferSize, bytesUploaded = 0;
    byte[] buffer;
    int maxBufferSize = 2*1024*1024;

//    String[] response2;






    int ch;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("My Service","Starts");
        Choice = intent.getStringExtra("Choice");
        Log.d("My Service",Choice);

        String inputTime = intent.getStringExtra("inputTime");
        number = parseInt(inputTime);
        Log.d("My Service",inputTime);

        if(Choice.equals("Listener"))
        {
            Log.e("akdsjflksd","choice 1");
            ch = 1;
            final int TIME_DURATION = parseInt(inputTime) * 60 * 1000;
            String inputText = intent.getStringExtra("inputWord");
            Log.d("My Service",inputText);
            sendMessage(inputText);



            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            long rStart;
                            long tStart = System.currentTimeMillis();
                            while (System.currentTimeMillis() + 10 - tStart <= TIME_DURATION) {
                                rStart = System.currentTimeMillis();
                                startRecording();
                                while (System.currentTimeMillis() - rStart <= RECORDING_DURATION) {

                                }
                                if (System.currentTimeMillis() - rStart > RECORDING_DURATION) {
                                    stopRecording();
                                    if(number!=1){
                                        if(totalSent < number) {
                                            if (numb == 1) {
                                                uploadFile(getRecordingFilePath2());
                                            }
                                            if (numb == 2) {
                                                uploadFile(getRecordingFilePath1());
                                            }
                                        }
                                    }
                                }


                            }
                            if (System.currentTimeMillis() + 10 - tStart > TIME_DURATION) {
                                //here i want to stop the service
//                                    if (isRecording) {
//                                        stopRecording();
//                                        if(totalSent < number) {
//                                            if (numb == 1) {
//                                                uploadFile(getRecordingFilePath2());
//                                            }
//                                            if (numb == 2) {
//                                                uploadFile(getRecordingFilePath1());
//                                            }
//                                        }
//                                    }
//                                    isSent=false;
//                                    while(!isSent){
//                                        try {
//                                            Log.d("sleeping","waiting for upload message");
//                                            Thread.sleep(50);
//                                        }catch (InterruptedException e){
//                                            Log.d("sleep","error");
//                                        }
//
////                                    stopSelf();
//                                    }
//                                    if(isSent){

                                if (numb == 1) {
                                    fileUpload1(getRecordingFilePath2());
                                }
                                if (numb == 2) {
                                    fileUpload1(getRecordingFilePath1());
                                }

                                response = sendMessage1("done11");
                                changNot();
//                                    }
                            }

                        }


                    }
            ).start();


            Log.d("Service", "onStart");
            createNotificationChannel();


            Intent intent1 = new Intent(this, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_IMMUTABLE);
            Notification notification = new NotificationCompat.Builder(this, "1")
                    .setContentText("Listening..")
                    .setContentTitle("Listener")
                    .setSmallIcon(R.drawable.ic_android)
                    .setContentIntent(pendingIntent)
                    .build();


            startForeground(1, notification);

            Log.d("Service", "Service should start");



        }

        if(Choice.equals("Recommend")){
            Log.d("reccommends","choice hopefully 2");
            ch = 2;
            final int TIME_DURATION = parseInt(inputTime) * 60 * 1000;
            sendMessage("rec23");
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            long rStart;

                            long tStart = System.currentTimeMillis();
                            while (System.currentTimeMillis() + 10 - tStart <= TIME_DURATION) {
                                rStart = System.currentTimeMillis();
                                startRecording();
                                while (System.currentTimeMillis() - rStart <= RECORDING_DURATION) {

                                }
                                if (System.currentTimeMillis() - rStart > RECORDING_DURATION) {
                                    stopRecording();
                                    if(number!=1) {
                                        if (totalSent < number) {
                                            if (numb == 1) {
                                                uploadFile(getRecordingFilePath2());
                                            }
                                            if (numb == 2) {
                                                uploadFile(getRecordingFilePath1());
                                            }
                                        }
                                    }
                                }


                            }
                            if (System.currentTimeMillis() + 10 - tStart > TIME_DURATION) {
                                //here i want to stop the service
//                                if (isRecording) {
//                                    Log.d("isRecording","false");
//                                    stopRecording();
//                                    if(totalSent < number) {
//                                        if (numb == 1) {
//                                            uploadFile(getRecordingFilePath2());
//                                        }
//                                        if (numb == 2) {
//                                            uploadFile(getRecordingFilePath1());
//                                        }
//                                    }
//                                }
//                                    isSent=false;
//                                    while(!isSent){
//                                        try {
//                                            Log.d("sleeping","waiting for upload message");
//                                            Thread.sleep(50);
//                                        }catch (InterruptedException e){
//                                            Log.d("sleep","error");
//                                        }
//
////                                    stopSelf();
//                                    }
//                                    if(isSent){

                                if (numb == 1) {
                                    fileUpload2(getRecordingFilePath2());
                                }
                                if (numb == 2) {
                                    fileUpload2(getRecordingFilePath1());
                                }
                                response2 = getSynons("done22");
                                changNot2();
                            }

                        }


                    }
            ).start();


            Log.d("Service", "onStart");
            createNotificationChannel();


            Intent intent1 = new Intent(this, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_IMMUTABLE);
            Notification notification = new NotificationCompat.Builder(this, "1")
                    .setContentText("Listening...")
                    .setContentTitle("Recommender")
                    .setSmallIcon(R.drawable.ic_android)
                    .setContentIntent(pendingIntent)
                    .build();


            startForeground(1, notification);

            Log.d("Service", "Service should start");
        }
        return START_NOT_STICKY;
//        return super.onStartCommand(intent, flags, startId);
    }

    private void changNot2() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this, MainActivity.class);

        intent1.setAction("STOP_FOREGROUND_SERVICE_ACTION");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_IMMUTABLE);

// Build an updated notification
        NotificationCompat.Builder updatedBuilder = new NotificationCompat.Builder(this, "1")
                .setContentTitle("Recommendations:")
                .setContentText(response2[1] + " : " + response2[2])
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_android);

// Issue the updated notification
        notificationManager.notify(5, updatedBuilder.build());
    }

    private void changNot() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this, MainActivity.class);

        intent1.setAction("STOP_FOREGROUND_SERVICE_ACTION");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_IMMUTABLE);

// Build an updated notification
        NotificationCompat.Builder updatedBuilder = new NotificationCompat.Builder(this, "1")
                .setContentTitle("Done Listening")
                .setContentText(inputWord+" said "+Integer.toString(occr)+" times. Press Here")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_android);

// Issue the updated notification
        notificationManager.notify(3, updatedBuilder.build());
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            Log.e("service","createNotififcationChannel");
            NotificationChannel notificationChannel = new NotificationChannel(
                    "1","Vocabury Running", NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
            Log.e("Service","createdNot Channel");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private String getRecordingFilePath1() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "trial1" + ".aac");
        return file.getPath();
    }

    private String getRecordingFilePath2() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "trial2" + ".aac");
        return file.getPath();
    }


    private void startRecording() {

        if(mediaRecorder == null)
        {mediaRecorder =new MediaRecorder();}
        if(mediaRecorder!=null){
            mediaRecorder.reset();
        }
        try {

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            if(numb==1){mediaRecorder.setOutputFile(getRecordingFilePath1()); numb =2;}
            else {mediaRecorder.setOutputFile(getRecordingFilePath2()); numb =1;}
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            Log.d("Recorder","RecordingStarted");

//            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadFile(String filePath) {
//        Toast.makeText(this, "Send Pressed", Toast.LENGTH_SHORT).show();
        isSent = false;
        totalSent = totalSent + 1;
        FileUpload fileUpload = new FileUpload(this ,serverURL,filePath,ch);
        isSent = fileUpload.execute();
    }

    private void stopRecording() {
//        mediaRecorder.stop();
//        mediaRecorder.release();
//        mediaRecorder = null;
//
//        uploadFile(getRecordingFilePath());
////        Toast.makeText(this, "Recording Stopped", Toast.LENGTH_SHORT).show();
//
//        Log.d("TAG",getRecordingFilePath());
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (RuntimeException e) {
                // Handle the exception, e.g., log an error message
                e.printStackTrace();
            } finally {
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
                isRecording=false;
                Log.d("Recorder","Stopped Recording");
            }

            Log.d("TAG", "started uploading");
        }
    }

    private void sendMessage(String message){
        String feed;
        Log.d("sendMessage","called");
        MessageUpload messageUpload = new MessageUpload(this,serverURL,message);
        messageUpload.execute();

    }

    private String sendMessage1(String message){
        String responseMessage = null;

        try {
            URL url = new URL(serverURL+"/receive_message");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            // Send the message in JSON format
            String jsonInputString = "{\"message\": \"" + message + "\"}";

            try (OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
//                        return response.toString();
//
                JSONObject jsonResponse = new JSONObject(response.toString());
                responseMessage = jsonResponse.getString("response");
                occr = parseInt(responseMessage);
                Log.d("Count",responseMessage);
                return responseMessage;

                // Log or use the extracted response message

//                Log.d("ResponseMessage", responseMessage);
//                Log.d("time",String.valueOf(tsp));
            }


        } catch (Exception e) {
            e.printStackTrace();
//                    return "Error: " + e.getMessage();
            Log.e("Error",e.getMessage());
            return null;
        }
    }

    private void fileUpload1(String filePath){
        try {
            String uploadname = filePath.substring(23);
            FileInputStream fileInputStream = new FileInputStream(filePath);

            URL url = new URL(serverURL + "/upload_file_listener");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setUseCaches(false);
            connection.connect();

            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(twoHyphens + boundary + lineEnd);
            os.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + uploadname + "\"" + lineEnd);
            os.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            System.out.println("available: " + String.valueOf(bytesAvailable));
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            bytesUploaded += bytesRead;
            while (bytesRead > 0) {
                os.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                bytesUploaded += bytesRead;
            }
            System.out.println("uploaded: " + String.valueOf(bytesUploaded));
            os.writeBytes(lineEnd);
            os.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


            connection.setConnectTimeout(2000); // allow 2 seconds timeout.
            int rcode = connection.getResponseCode();

            fileInputStream.close();
            os.flush();
            os.close();


            // Update the UI thread with the response

        } catch (Exception e) {
            e.printStackTrace();
            final String errorMessage = "File upload failed: " + e.getMessage();

            // Update the UI thread with the error message

        }
    }

    public String[] getSynons(String message){
        String[] responseMessage = new String[3];

        try {
            URL url = new URL(serverURL+"/receive_message");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            // Send the message in JSON format
            String jsonInputString = "{\"message\": \"" + message + "\"}";

            try (OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
//                        return response.toString();
//
                JSONObject jsonResponse = new JSONObject(response.toString());
                responseMessage[1] = jsonResponse.getString("word");
                responseMessage[2] = jsonResponse.getString("synonyms");


                return responseMessage;

                // Log or use the extracted response message

//                Log.d("ResponseMessage", responseMessage);
//                Log.d("time",String.valueOf(tsp));
            }


        } catch (Exception e) {
            e.printStackTrace();
//                    return "Error: " + e.getMessage();
            Log.e("Error",e.getMessage());
            return null;
        }
    }

    private void fileUpload2(String filePath){
        try {
            String uploadname = filePath.substring(23);
            FileInputStream fileInputStream = new FileInputStream(filePath);

            URL url = new URL(serverURL + "/upload_file_listener");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setUseCaches(false);
            connection.connect();

            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(twoHyphens + boundary + lineEnd);
            os.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + uploadname + "\"" + lineEnd);
            os.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            System.out.println("available: " + String.valueOf(bytesAvailable));
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            bytesUploaded += bytesRead;
            while (bytesRead > 0) {
                os.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                bytesUploaded += bytesRead;
            }
            System.out.println("uploaded: " + String.valueOf(bytesUploaded));
            os.writeBytes(lineEnd);
            os.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


            connection.setConnectTimeout(2000); // allow 2 seconds timeout.
            int rcode = connection.getResponseCode();

            fileInputStream.close();
            os.flush();
            os.close();


            // Update the UI thread with the response

        } catch (Exception e) {
            e.printStackTrace();
            final String errorMessage = "File upload failed: " + e.getMessage();

            // Update the UI thread with the error message

        }
    }



    @Override
    public void onDestroy() {

        if(mediaRecorder!=null){
            try {
                mediaRecorder.stop();
            } catch (RuntimeException e) {
                // Handle the exception, e.g., log an error message
                e.printStackTrace();
            } finally {
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
                isRecording=false;
                Log.d("Recorder","Stopped Recording");
            }
            if(numb ==1){uploadFile(getRecordingFilePath2());}
            if(numb ==2){uploadFile(getRecordingFilePath1());}
            Log.d("TAG", "started uploading");
        }
        stopForeground(true);


        super.onDestroy();
    }
}
