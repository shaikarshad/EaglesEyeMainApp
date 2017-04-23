package com.base.ours.eagleseyemainapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Krutarth on 3/28/2017.
 */

public class ClientConnection extends MultiDexApplication {

    static WebSocketClient mWebSocketClient; //varvlet-blog
    public static String euID = "";
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        connectWebSocket();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mWebSocketClient.close();
    }

    public void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://fbcd3b88.ngrok.io/websockets/eagleseyeserverendpoint");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                //mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                System.out.println(s);
                Intent intent = new Intent("CUSTOM_INTENT");
                String[] split = s.split("\t");
                if (split[0].equals("bus")) {
                    //bus send lat and long: "bus" \t route \t ID \t Lat \t Long \t FullStatus \t CommentReset
                    System.out.println("bus: " + s);
                    intent.putExtra("bus", s);
                    sendBroadcast(intent);
                } else if (split[0].equals("comment")) {//comment reply: "comment" \t Route BusID \t CommentID \t comment(UserID Comment)
                    intent.putExtra("comment", s);
                    sendBroadcast(intent);
                }
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

    public static void sendMessage(String msg) {
        mWebSocketClient.send(msg);
    }
}
