package com.hahazql.tools.event;/**
 * Created by zql on 15/12/23.
 */


/**
 * Created by zql on 15/12/23.
 *
 * @className AsyncEventListener
 * @classUse 异步事件监听器
 */
public abstract class AsyncEventListener<E extends IEvent> implements IEventListener<E>, Runnable {

    public void run() {
        fireEvent(getIEvent());
    }

    /**
     * 获取监听类
     *
     * @return
     */
    public abstract Class<? extends AsyncEventListener> getListenerClass();

    /**
     * 创建新的异步listen
     *
     * @return
     */
    public abstract AsyncEventListener getInstance();

    /**
     * 设置事件
     *
     * @param event
     */
    public abstract void setIEvent(E event);

    /**
     * 获取事件
     *
     * @return
     */
    public abstract E getIEvent();
}
