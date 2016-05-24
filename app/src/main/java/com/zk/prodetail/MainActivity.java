package com.zk.prodetail;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zk.adapter.TaskAdapter;
import com.zk.util.AppConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private TextView tv_proceed_cut, tv_proceed_check, tv_proceed_carving, tv_proceed_scan, tv_proceed_weld, tv_proceed_save;
    private ListView lv_current_task;
    private String msg;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initActionBar();
        hideSystemUI();
        initContentWidget();
        new NetworkThread().start();


//        PrepareData();


    }

    /**
     * 此方法用于获取数据
     *
     * @return list封装数据
     */
    private List PrepareData() {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("id", "0000001");
        map.put("type", "产品A");
        map.put("name", "产品A 工件1");
        map.put("workzone", "工位1");
        map.put("status", "生产完成");
        map.put("time", "2016/05/23 15:37");

        list.add(map);


        return list;
    }

    /**
     * 初始化主界面控件
     */
    private void initContentWidget() {
        tv_proceed_carving = (TextView) findViewById(R.id.tv_proceed_scan);
        tv_proceed_cut = (TextView) findViewById(R.id.tv_proceed_cut);
        tv_proceed_check = (TextView) findViewById(R.id.tv_proceed_check);
        tv_proceed_scan = (TextView) findViewById(R.id.tv_proceed_scan);
        tv_proceed_weld = (TextView) findViewById(R.id.tv_proceed_weld);
        tv_proceed_save = (TextView) findViewById(R.id.tv_proceed_save);

        lv_current_task = (ListView) findViewById(R.id.lv_current_task);


        LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View headerView = lif.inflate(R.layout.list_header, lv_current_task, false);

        lv_current_task.addHeaderView(headerView);

        lv_current_task.setAdapter(new TaskAdapter(MainActivity.this, PrepareData()));
    }

    /**
     * 初始化actionbar
     */
    private void initActionBar() {

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setCustomView(R.layout.titlebar);
        actionBar.setIcon(R.mipmap.mjlogo);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);

    }

    /**
     * 隐藏系统ui
     */
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                // remove the following flag for version < API 19
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    /**
     * 开启View闪烁效果
     */

    private void startFlick(View view) {

        if (null == view) {

            return;

        }

        Animation alphaAnimation = new AlphaAnimation(1, 0);

        alphaAnimation.setDuration(300);

        alphaAnimation.setInterpolator(new LinearInterpolator());

        alphaAnimation.setRepeatCount(Animation.INFINITE);

        alphaAnimation.setRepeatMode(Animation.REVERSE);

        view.startAnimation(alphaAnimation);

    }

    /**
     * 取消View闪烁效果
     */

    private void stopFlick(View view) {

        if (null == view) {

            return;

        }

        view.clearAnimation();

    }


    /**
     * 获取socket
     * @param host 服务器地址
     * @param port 端口号
     * @return socket
     * @throws IOException
     */
    private Socket RequestSocket(String host , String port) throws IOException {
        Socket socket = new Socket(host, Integer.parseInt(port));
        return socket;
    }

    /**
     * 读取socket的数 据
     * @param socket
     * @return 数据字符串
     * @throws IOException
     */
    private void ReceiveMsg(Socket socket) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GBK"));
        byte[] serverSay = new byte[1024];// 读取<1KB
        InputStream is = null;
        String msg = null;
        boolean status = true ;
        while ( status ) {
            try {
                socket.sendUrgentData(0xFF);
                is = socket.getInputStream();
                int len = is.read(serverSay);
                msg = new String(serverSay, 0, len,"GBK");
                Log.v("server msg", msg);
            } catch (Exception e) {
                e.printStackTrace();
                status = false;
                new NetworkThread().start();
            }
        }
    }

    /**
     * 判断是否断开连接，断开返回true,没有返回false
     * @param socket
     * @return
     */
    public Boolean isServerClose(Socket socket){
        try{
            socket.sendUrgentData(0xff);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
            return false;
        }catch(Exception se){
            return true;
        }
    }

    class NetworkThread extends Thread {

        @Override
        public void run() {
            super.run();

            try {
                socket = RequestSocket(AppConfig.HOST, AppConfig.PORT);
                ReceiveMsg(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


