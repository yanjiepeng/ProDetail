package com.zk.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zk.prodetail.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/5/23.
 * 任务列表的适配器
 */
public class TaskAdapter extends BaseAdapter {


    private Context context;
    private LayoutInflater inflater;
    private List<Map<String,Object>> list;

    public TaskAdapter(Context context , List<Map<String,Object>> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
            if(convertView == null) {
                view = inflater.inflate(R.layout.task_list_item, null);
                TextView tv_id = (TextView) view.findViewById(R.id.tv_current_id);
                TextView tv_type = (TextView) view.findViewById(R.id.tv_current_type);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_current_name);
                TextView tv_work_zone = (TextView) view.findViewById(R.id.tv_current_workzone);
                TextView tv_status = (TextView) view.findViewById(R.id.tv_current_status);
                TextView tv_finish_time = (TextView) view.findViewById(R.id.tv_current_finish_time);

                //创建viewholder保存对象
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.tv_id = tv_id;
                viewHolder.tv_type = tv_type;
                viewHolder.tv_name = tv_name;
                viewHolder.tv_work_zone = tv_work_zone;
                viewHolder.tv_status = tv_status;
                viewHolder.tv_finish_time = tv_finish_time;

                //保存viewholder
                view.setTag(viewHolder);

            } else {
                view = convertView;
                Log.v("TaskAdapter适配器","复用");
            }
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        //此处获取数据并设置显示

        Map map = list.get(position % list.size());

        viewHolder.tv_id.setText((String) map.get("id"));
        viewHolder.tv_type.setText((CharSequence) map.get("type"));
        viewHolder.tv_name.setText((CharSequence) map.get("name"));
        viewHolder.tv_work_zone.setText((CharSequence) map.get("workzone"));
        viewHolder.tv_status.setText((CharSequence) map.get("status"));
        viewHolder.tv_finish_time.setText((CharSequence) map.get("time"));


        return view;
    }

    class ViewHolder {
        TextView tv_id;
        TextView tv_type;
        TextView tv_name;
        TextView tv_work_zone;
        TextView tv_status;
        TextView tv_finish_time;
    }
}
