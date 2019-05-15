package com.ruiyang.du.utils;

import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;

import java.util.HashMap;
import java.util.Map;

public class XmlUtils {

    private static XStream xStream = new XStream();

    public static String map2xml(Map<String,Object> map){
        String toXML = xStream.toXML(map);
        return toXML;
    }

    public static Map<String,Object> xml2map(String xml){
        Object fromXML = xStream.fromXML(xml);
        return (Map<String, Object>) fromXML;
    }

    public static String toXml(Object o){
        String toXML = xStream.toXML(o);
        return toXML;
    }

    public static Object fromXml(String xml){
        Object fromXML = xStream.fromXML(xml);
        return fromXML;
    }

    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("key1","value1");
        map.put("key2","value22");
        map.put("key3","value333");
        Map<String,Object> childMap = new HashMap<>();
        childMap.put("childKey1","childValue1");
        map.put("childMap",childMap);

        System.out.println(JSONObject.toJSONString(map));
        String map2xml = toXml(map);
        System.out.println(map2xml);
        Map<String, Object> xml2map = (Map<String, Object>) fromXml(map2xml);
        System.out.println(JSONObject.toJSONString(xml2map));

    }
}
