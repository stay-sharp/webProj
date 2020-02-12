package com.ruiyang.du.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public class MapToObject {


    public static Object mapToObject(Map<Object, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return beanClass.newInstance();
        Object obj = beanClass.newInstance();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            if (map.containsKey(field.getName())) {
                field.set(obj, map.get(field.getName()));
            }
        }
        return obj;
    }
}
