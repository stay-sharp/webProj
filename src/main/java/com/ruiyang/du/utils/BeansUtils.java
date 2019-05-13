package com.ruiyang.du.utils;

import com.ruiyang.du.bo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;


public class BeansUtils {

    Logger logger = LoggerFactory.getLogger(BeansUtils.class);

    @Autowired
    private static ApplicationContextHolder applicationContextHolder;

    /**
     * 通过bean的名字和类型，获取spring维护的bean
     * @param name
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> type) {
        if (StringUtils.isEmpty(name) || type == null) {
            return null;
        }
        ApplicationContext applicationContext = applicationContextHolder.getApplicationContext();
        try {
            T bean = applicationContext.getBean(name, type);
            return bean;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 反射获取对象o的包名和类名
     *
     * @param o
     * @return
     */
    public static String getClassName(Object o) {
        if (o == null) {
            return null;
        }
        return o.getClass().getName();
    }

    /**
     * 根据类名反射获取一个Class对象
     *
     * @param name package.Class
     * @return
     */
    public static Class<?> getClassObj(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
        //注：有以下3种方式获取Class对象
        // Class.forName(name);
        // or obj.getClass();
        // or Object.class;
        // Class对象可以用于获取并执行对应类的构造方法，或直接调用无参构造方法：class.newInstance();
    }

    /**
     * 获取一个Class对象对应的类的全部public方法(不继承)
     * @param clazz
     * @return
     */
    public static Method[] getDeclaredMethods(Class<?> clazz){
        if(clazz==null){
            return null;
        }
        Method[] methods = clazz.getDeclaredMethods();
        return methods;
        //注：getMethods相比getDeclaredMethods，多出的部分是继承自父类的方法。
    }

    public static void main(String[] args) {
        User user = new User();
        Class<?> classObj = getClassObj(getClassName(user));
        Method[] methods = getDeclaredMethods(classObj);
        for(Method method:methods){
            System.out.println(method.getName());
        }
    }

}
