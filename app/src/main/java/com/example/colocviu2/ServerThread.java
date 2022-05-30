package com.example.colocviu2;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread{

    private int port;
    private ServerSocket serverSocket;
    private HashMap<String, GenericResults> data;

    public ServerThread(int port) {
        this.port = port;
        try {

            this.serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            Log.e("Eroare", "E busit serverul " + port);
            e.printStackTrace();
        }

        this.data = new HashMap<>();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket client_socket = serverSocket.accept();

                /* TODO, Try to run the following code on a separate thread */

                /* Two options, either create a new thread and run the following code or not */
                /* CLIENT: hello */
                String clientResponse = "";

                if (client_socket != null) {
                    BufferedReader bufferReader = Utils.getReader(client_socket);
                    String request_data = bufferReader.readLine();

                    /* Request to server */

                    if (data.containsKey(request_data)) {
                        clientResponse = data.get(request_data).getRes1();
                    } else {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpGet httpGet = new HttpGet("https://api.dictionaryapi.dev/api/v2/entries/en/" + request_data);
                        HttpResponse httpResponse = httpClient.execute(httpGet);
                        HttpEntity httpEntity = httpResponse.getEntity();
                        if (httpEntity == null) {
                            Log.e("Eroare", "Null response from server");
                        }


                        /* PArse response and add to hashmap */
                        String response = EntityUtils.toString(httpEntity);
                        JSONObject content = new JSONObject(response);

                        JSONObject meaninngsObject = content.getJSONObject("meaninngs");

                        JSONObject definitionsObject  = meaninngsObject.getJSONObject("definitions");
                        String definition = definitionsObject.getString("definition");

                        GenericResults newRes = new GenericResults();
                        newRes.setRes1(definition.toString());
                        this.data.put(request_data, newRes);
                        clientResponse = data.get(request_data).getRes1();

                    }

                    /* Write to client socket the reponse */
                    PrintWriter printWriter = Utils.getWriter(client_socket);
                    printWriter.println(clientResponse);

                    client_socket.close();


                } else {
                    Log.e("Erroare", "Null client socket");
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
