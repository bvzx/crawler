package io.bvzx.service.spider;

import io.bvzx.service.base.Event;

/**
 * Created by Administrator on 2016/9/14.
 */
public interface Spider {

    void process(Event event);

    void analysis(Event event);

    void setEvent(Event event);

    Object getResult();

}
