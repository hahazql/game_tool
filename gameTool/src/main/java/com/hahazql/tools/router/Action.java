package com.hahazql.tools.router;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class Action {

    private String actionPath;
    private Object controller;
    private Method method;

    public String getActionPath() {
        return actionPath;
    }

    public void setActionPath(String actionPath) {
        this.actionPath = actionPath;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    /**
     * <p>
     * 比较参数类型是否一致
     * </p>
     *
     * @param types   asm的类型
     * @param clazzes java 类型({@link Class})
     * @return
     */
    @SuppressWarnings("unused")
    private static boolean sameType(Type[] types, Class<?>[] clazzes) {
        // 个数不同
        if (types.length != clazzes.length) {
            return false;
        }

        for (int i = 0; i < types.length; i++) {
            if (types[i] != clazzes[i])
                return false;
        }
        return true;
    }

    /**
     * 调用方法
     *
     * @param params 参数
     * @return
     * @throws RouterException
     */
    public Object invoke(Object... params) throws RouterException {
        try {
            return method.invoke(controller, params);
        } catch (IllegalAccessException e) {
            throw (new RouterException(e));
        } catch (InvocationTargetException e) {
            throw (new RouterException(e));
        }
    }

}
