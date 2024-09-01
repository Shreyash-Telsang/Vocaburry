package com.example.testapk1;


import static com.example.testapk1.MainActivity.inputWord;
import static com.example.testapk1.MainActivity.occr;
import static com.example.testapk1.MainActivity.serviceIntent2;

//import  com.example.testapk1.MainActivity.occr;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class MessageUpload {
    private String message;

    private String serverURL;

    String  responseMessage;

    Context context;





    private Handler uiHandler= new Handler(Looper.getMainLooper());
    public MessageUpload(Context context, String serverUrl, String message){
        this.serverURL = serverUrl;
        this.message=message;
        this.context=context;
    }

    public void execute(){
        ExecutorService executor = Executors.newSingleThreadExecutor();


        executor.execute(new Runnable() {
            @Override
            public void run() {
                if(message.equals("done11")) {

//                        Thread.sleep(3000);
                    try {
                        Log.d("upload","sleeping..");
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                }
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
//                        Log.d("Response",response.toString());
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        responseMessage = jsonResponse.getString("response");

                        // Log or use the extracted response message
                        Log.d("ResponseMessage", responseMessage);
//                        if(message.equals("done11")){
//                            Log.d("fileupload","done11");
//                            serviceIntent2 =new Intent(context, MyServiceB.class);
//                            serviceIntent2.putExtra("Count",responseMessage);
//                            context.startService(serviceIntent2);
//                            occr = parseInt(responseMessage);
//                        }
                    }

                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
//                            if(message.equals("done11")){
//                                Log.d("dialog","start");
//                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
////        String word = editTextWord.getText().toString();
//
//                                // Set the dialog title and message
//                                builder.setTitle("Done Listening")
//                                        .setMessage(inputWord+" said "+String.valueOf(occr)+" times");
//
//                                // Set the positive button (OK) and its click listener
//                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        // Perform the desired action when OK is clicked
//                                        // You can leave this method empty if no action is needed
//                                    }
//                                });
//
//                                // Create and show the AlertDialog
//                                AlertDialog alertDialog = builder.create();
//                                alertDialog.show();
//                            }
//                            Toast.makeText(context,"Sent successfully",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
//                    return "Error: " + e.getMessage();
                    responseMessage=e.getMessage();
                    Log.e("Error",e.getMessage());
                }
            }
        });

    }


}
