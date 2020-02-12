package com.ruiyang.du.demo;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import java.beans.Customizer;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Java8Demo {

    public static void main(String[] args) {
        //streamDemo();
        functionalInterfaceDemo();
    }

    private static void streamDemo() {
        List<Integer> arrayList = Lists.newArrayList(1, 5, 6, 9, 2, 8, 4, 3, 7);
        //集合转stream ：Collection.stream()
        //anyMatch
        boolean b = arrayList.stream().anyMatch(x -> x.equals(5));
        System.out.println(b);
        //stream转数组
        Object[] array = arrayList.stream().toArray();
        System.out.println(JSONObject.toJSONString(array));
        //过滤
        Object[] array1 = arrayList.stream().filter(x -> x < 5).toArray();
        System.out.println(JSONObject.toJSONString(array1));
        //映射 : 可映射出原集合中的每个元素的某属性到新集合
        Set<Double> collect = arrayList.stream().map(Integer::doubleValue).collect(Collectors.toSet());
        System.out.println(collect);

        //数组转stream ： Arrays.stream(array)
        //findFirst
        Optional<Object> first = Arrays.stream(array1).findFirst();
        System.out.println(first.get());
        //由值直接创建stream ： Stream.of()
        //forEach
        Stream.of(1, 3, 5, 7, 8).forEach(x -> System.out.println(x));
    }

    private static void functionalInterfaceDemo(){
//        List<String> list = Lists.newArrayList("123","111","33333","7812312");
//        List<Integer> integers = translateListTR(list, x->x.length());
//        System.out.println(JSONObject.toJSONString(integers));

//        Object supplier = supplierGet(()-> System.currentTimeMillis());
//        System.out.println(supplier);

//        String str = "1237asd213";
//        consumerAccept(str,x-> System.out.println(x));

        String str1 = "000AA999";
        boolean b = predicateTest(str1, x -> x.startsWith("000"));
        System.out.println(b);
    }

    static <T, R> List<R> translateListTR(List<T> tList, Function<T, R> function) {
        List<R> rList = new ArrayList<>();
        for (T t : tList) {
            rList.add(function.apply(t));
        }
        return rList;
    }

    static <T> T supplierGet(Supplier<T> supplier) {
        return supplier.get();
    }

    static <T> void consumerAccept(T t,Consumer<T> consumer){
        consumer.accept(t);
    }

    static <T> boolean predicateTest(T t, Predicate<T> predicate){
        return predicate.test(t);
    }
}
