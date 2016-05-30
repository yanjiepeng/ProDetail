package com.zk.bean;

/**
 * Created by Administrator on 2016/5/25.
 */
public class TaskInfo {


    /**
     * taskid : 00001
     * kind : 0001
     * type : 产品
     * name : 产品A 工件2
     * workzone : 工位1
     * status : 生成完成
     * time : 2016-05-25 11:23:07
     */

    private String taskid;
    private String kind;
    private String type;
    private String name;
    private String workzone;
    private String status;
    private String time;

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkzone() {
        return workzone;
    }

    public void setWorkzone(String workzone) {
        this.workzone = workzone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    @Override
    public String toString() {
        return "TaskInfo{" +
                "taskid='" + taskid + '\'' +
                ", kind='" + kind + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", workzone='" + workzone + '\'' +
                ", status='" + status + '\'' +
                ", time='" + time + '\'' +
                '}';
    }


}
