package com.example.colocviu2;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {


    int port;
    String address;
    String request_data;
    TextView responseTextView;

    ClientThread(int port, String address, String request_data, TextView responseTextView) {
        this.port = port;
        this.address = address;
        this.request_data = request_data;
        this.responseTextView = responseTextView;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(address, port);

            BufferedReader bufferedReader = Utils.getReader(socket);
            PrintWriter printWriter = Utils.getWriter(socket);

            /* data;data2*/
            printWriter.println(request_data);

            String response = bufferedReader.readLine();
            Log.d("MyApp", response);


            responseTextView.post(new Runnable() {
                @Override
                public void run() {
                    responseTextView.setText(response);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
