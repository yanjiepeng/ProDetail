package com.zk.prodetail;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zk.adapter.TaskAdapter;
import com.zk.bean.TaskInfo;
import com.zk.event.StatusEvent;
import com.zk.event.TaskEvent;
import com.zk.service.SocketService;
import com.zk.service.SocketService2;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private TextView tv_proceed_cut, tv_proceed_check, tv_proceed_carving, tv_proceed_scan, tv_proceed_weld, tv_proceed_save;
    private static ListView lv_current_task;
    private String msg;
    private Socket socket;
    public static List<TaskInfo> current_task = new ArrayList<TaskInfo>();;
   public static  List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    TaskAdapter mAdapter ;


   /* public static Handler mHandler = new Handler() {
        Gson gson = new Gson();

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String res = (String) msg.obj;
            if (res != "" && res != null) {
                TaskInfo ti = gson.fromJson(res, TaskInfo.class);
                Log.i("page", ti.toString());
                PrepareData(DealData(ti));

            }

        }
    }   ;*/


    /**
     * 此处针对每次传来的数据进行处理
     * @param ti
     */
    private static List<TaskInfo> DealData(TaskInfo ti) {



        //当前任务列表为空就加入任务列表
        if (current_task.size()==0) {
            current_task.add(ti);
        }else if (ti.getKind().equals(current_task.get(0).getKind())){
            current_task.add(ti);
        }else if (   !ti.getKind().equals(current_task.get(0).getKind()) ){
            current_task.clear();
            current_task.add(ti);
        }

        return current_task;

    }

    private static void PrepareData(List<TaskInfo> current_task) {

        int size = current_task.size();
        list.clear();
        for (int i = 0; i < size; i++) {

            Map<String, Object> map = new HashMap<String, Object>();

            map.put("id",  current_task.get(i).getTaskid());
            map.put("type",current_task.get(i).getType());
            map.put("kind",current_task.get(i).getKind());
            map.put("name", current_task.get(i).getName());
            map.put("workzone", current_task.get(i).getWorkzone());
            map.put("status", current_task.get(i).getStatus());
            map.put("time", current_task.get(i).getTime());

            list.add(map);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this); //注册eventBus

        initActionBar();
        hideSystemUI();
        initContentWidget();

        startService(new Intent(this, SocketService.class));
        startService(new Intent(this, SocketService2.class));
//        PrepareData();


    }

    /**
     * EventBus自动回调
     * @param event
     */
    @Subscribe
    public void onEventMainThread(TaskEvent event) {


        Gson gson = new Gson();
        Log.e("Event", event.getMsg());
        TaskInfo ti = gson.fromJson(event.getMsg(), TaskInfo.class);
        PrepareData(DealData(ti));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    @Subscribe
    public void onEventMainThread(StatusEvent event) {

        Log.e("status", event.getStatus());

    }

    /**
     * 此方法用于获取数据
     *
     * @return list封装数据
     */
    private List PrepareData() {



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
        mAdapter = new TaskAdapter(this , list) ;
        lv_current_task.setAdapter(mAdapter );


        lv_current_task.addHeaderView(headerView);


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


}


