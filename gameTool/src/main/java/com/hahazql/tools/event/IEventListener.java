package com.hahazql.tools.event;


/**
 * Created by zql on 15/12/23.
 *
 * @className IEventListener
 * @classUse 事件监听器接口
 */
public interface IEventListener<E extends IEvent> {
    /**
     * 触发事件
     *
     * @param event 事件对象
     */
    public void fireEvent(E event);

}
