package com.example.testapk1;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileUpload {
    private String serverUrl;  // URL of your Flask server
    private String filePath;   // File path of the file you want to upload
    private Context context;
    private Handler uiHandler = new Handler(Looper.getMainLooper());


    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";

    int bytesRead, bytesAvailable, bufferSize, bytesUploaded = 0;
    byte[] buffer;
    int maxBufferSize = 2*1024*1024;

    int choice;

    boolean isSent;

    public FileUpload(Context context,String serverUrl, String filePath, int Choice) {
        this.serverUrl = serverUrl;
        this.filePath = filePath;
        this.context = context;
        this.choice = Choice;


    }




    public boolean execute() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable() {
            @Override

            public void run() {


                try {
                    String uploadname = filePath.substring(23);
                    FileInputStream fileInputStream = new FileInputStream(filePath);
                    URL url = null;
//                        if(choice == 1){
                    url = new URL(serverUrl + "/upload_file_listener");
                    Log.d("fileupload","1 listener");


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
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("FileUploadTask", "Response: " + rcode);

                            // Handle the server response here, e.g., show a toast message.

//                                if (rcode == 200)
//                                    Toast.makeText(context, "Success!!", Toast.LENGTH_LONG).show();
//                                else Toast.makeText(context, "Failed!!", Toast.LENGTH_LONG).show();
//                            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    final String errorMessage = "File upload failed: " + e.getMessage();

                    // Update the UI thread with the error message
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("FileUploadTask", errorMessage);
                            Toast.makeText(context, "Uploading Failed: "+errorMessage, Toast.LENGTH_SHORT).show();
                            // Handle the error, e.g., show a toast message.

                        }
                    });
                }

            }
        });return true;
    }
}
