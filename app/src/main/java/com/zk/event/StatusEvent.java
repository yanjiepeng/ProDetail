package com.zk.event;

/**
 * Created by Administrator on 2016/5/27.
 */
public class StatusEvent {

    private String status;

    public StatusEvent(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
