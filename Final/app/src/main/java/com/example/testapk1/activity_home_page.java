package com.example.testapk1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class activity_home_page extends AppCompatActivity {

    private Button dictionaryButton;
    private Button listenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        dictionaryButton = findViewById(R.id.dictionary_to_listen);
        listenButton = findViewById(R.id.listen_to_dictionary);

        dictionaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_home_page.this, Dictionary_main.class);
                startActivity(intent);
            }
        });

        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_home_page.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

