package com.ruiyang.du.demo.algorithm;

import com.alibaba.fastjson.JSONObject;

public class ArrayAlgorithmDemo {
    public static void main(String[] args) {
        //int[] ints = new int[]{1, 2, 2, 2, 2, 2, 2, 3, 4};
        //int[] ints = new int[]{1, 2, 3, 4, 5, 6, 9, 11, 12};
        int[] ints = new int[]{1, 2, 3, 3, 5, 6, 9, 11, 12};
        int aim = 3;
        int leftOver = 0;
//        System.out.println(findAimIndex(ints, aim, leftOver));

        System.out.println(JSONObject.toJSONString(removeAim(ints,aim)));

    }

    /**
     * 寻找有序数组中指定元素的下标，若重复返回第一个
     * @param ints
     * @param aim
     * @param leftOver
     * @return
     */
    public static int findAimIndex(int[] ints, int aim, int leftOver) {
        int length = ints.length;
        if (length == 2) {
            if (ints[0] == aim) {
                return leftOver;
            } else if(ints[1]==aim){
                return leftOver + 1;
            }else {
                return -1;
            }
        }
        int indexOfMid = length / 2;
        int mid = ints[indexOfMid];
        int[] part;

        if (mid == aim) {
            if(ints[indexOfMid-1]==mid){
                part = subArray(ints, 0, indexOfMid);
                return findAimIndex(part, aim, leftOver);
            }
            return indexOfMid + leftOver;
        } else if (mid < aim) {
            part = subArray(ints, indexOfMid, length);
            leftOver = leftOver + indexOfMid;
            return findAimIndex(part, aim, leftOver);
        } else {
            part = subArray(ints, 0, indexOfMid);
            return findAimIndex(part, aim, leftOver);
        }
    }

    /**
     * 获取子数组
     * @param ints
     * @param from
     * @param to
     * @return
     */
    private static int[] subArray(int[] ints, int from, int to) {
        int[] sub = new int[to - from];
        int index = 0;
        for (int i = from; i < to; i++, index++) {
            sub[index] = ints[i];
        }
        return sub;
    }

    /**
     * 原地移除数组中的指定元素
     * @param source
     * @param aim
     * @return
     */
    public static int[] removeAim(int[] source,int aim){
        int over=0;
        for(int i=0;i<source.length;i++){
            int value = source[i];
            while (value==aim){
                over++;
                value = source[i+over];
            }
            source[i]=value;
        }
        return source;
    }

}
