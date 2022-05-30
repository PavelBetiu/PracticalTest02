package com.example.colocviu2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    Button startService;
    private ServerThread serverThread;
    Button clientStartRequest;

    TextView responseTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseTextView = findViewById(R.id.resultEditText);
        startService = findViewById(R.id.StarServerButton);

        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                serverThread = new ServerThread(10386);
                serverThread.start();

            }
        });

        clientStartRequest = findViewById(R.id.SendRequestButton);

        clientStartRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientThread clientThread = new ClientThread(10386, "127.0.0.1", "hello", responseTextView);
                clientThread.start();
            }
        });
    }
}