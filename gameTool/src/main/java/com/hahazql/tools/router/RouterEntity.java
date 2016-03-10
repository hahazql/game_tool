package com.hahazql.tools.router;

import com.hahazql.tools.clazz.PackageUtil;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.TreeMap;


public class RouterEntity {
    protected static TreeMap<String, Action> _ix_path = new TreeMap<String, Action>();

    /**
     * 遍历项目导入所有的Router
     *
     * @throws UnsupportedEncodingException
     * @throws RouterException
     */
    public static void loadRouter() throws RouterException {
        try {
            List<String> classNames = PackageUtil.getClassName("model.controller");
            for (String className : classNames) {
                System.out.println(classNames);
                Class clazz = Class.forName(className);
                if (!clazz.isAnnotationPresent(Controller.class))//只有有Controller注解的类才处理
                    continue;
                /**
                 * 遍历类下的方法
                 */
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(RequestMapping.class))//只有有RequestMapping的方法才生成router
                        continue;

                    Annotation annotation = method.getAnnotation(RequestMapping.class);
                    Action action = new Action();
                    action.setActionPath(((RequestMapping) annotation).value());
                    action.setController(clazz.newInstance());
                    action.setMethod(method);
                    addRouter(action);
                }
            }
        } catch (ClassNotFoundException e) {
            throw (new RouterException(e));
        } catch (InstantiationException e) {
            throw (new RouterException(e));
        } catch (IllegalAccessException e) {
            throw (new RouterException(e));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            throw (new RouterException(e));
        }
    }

    /**
     * 添加路由
     *
     * @param action
     */
    public static void addRouter(Action action) {
        _ix_path.put(action.getActionPath(), action);
    }

    /**
     * 获取Action
     *
     * @param path
     * @return
     */
    public static Action getAction(String path) {
        return _ix_path.get(path);
    }

}
