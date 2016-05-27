package com.zk.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.zk.event.StatusEvent;
import com.zk.event.TaskEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketService2 extends Service {

    ServerSocket statusSocket = null;
    public SocketService2() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new StatusThread().start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            statusSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class StatusThread extends Thread {

        @Override
        public void run() {
            super.run();

            //获取机器人状态socket

            try {
                statusSocket = new ServerSocket(5557);
                Log.i("LocalService", "ServerSocke5557启动成功");
                while (true) {

                    Socket client2 = statusSocket.accept();
                    Log.i("LocalService", "机器人客户端连接成功，ip" + client2.getInetAddress());
                    StatusReceiveThread srt = new StatusReceiveThread(client2);
                    srt.start();
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class StatusReceiveThread extends Thread {

        Socket client ;

        public StatusReceiveThread(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            super.run();
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(client.getInputStream(),"utf-8"));
                String res = br.readLine();
                String str = new String(res.getBytes("GBK"),"UTF-8" );
                EventBus.getDefault().post(new StatusEvent(str));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
