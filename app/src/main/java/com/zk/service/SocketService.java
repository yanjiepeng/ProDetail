package com.zk.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.zk.event.StatusEvent;
import com.zk.event.TaskEvent;
import com.zk.prodetail.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketService extends Service {

    ServerSocket serverSocket;
    public SocketService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        new AcceptThread().start();

        return super.onStartCommand(intent, flags, startId);



    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    private class AcceptThread extends Thread {

        @Override
        public void run() {

            Log.i("LocalService" , "正在启动服务");
            try {
                //获取订单的socket
               serverSocket = new ServerSocket(5556);
                Log.i("LocalService" , "ServerSocket5556启动成功");


                while (true) {

                    Socket client = serverSocket.accept();
                    Log.i("LocalService", "客户端连接成功，ip" + client.getInetAddress());
                    ReceiveThread rt = new ReceiveThread(client);
                    rt.start();


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private class  ReceiveThread extends Thread {
        Socket client;
        public ReceiveThread(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {

                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream(),"utf-8"));
                String res = br.readLine();
                String str = new String(res.getBytes("GBK"),"UTF-8" );
                EventBus.getDefault().post(new TaskEvent(str));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}


