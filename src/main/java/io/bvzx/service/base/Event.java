package io.bvzx.service.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public class Event<T> {

    private int code;

    private String msg;

    public EventType eventType;

    private T obj;

    private List<EventAction> eventActionList =new ArrayList<>();


    public void execute(){
        eventActionList.forEach(v -> v.onEvent());
    }

    public void setEventAction(EventAction eventAction) {
        eventActionList.add(eventAction);
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public List<EventAction> getEventActionList() {
        return eventActionList;
    }

    public void setEventActionList(List<EventAction> eventActionList) {
        this.eventActionList = eventActionList;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
