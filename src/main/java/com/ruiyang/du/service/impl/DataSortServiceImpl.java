package com.ruiyang.du.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DataSortServiceImpl{

    public static void main(String[] args) {
        List<Integer> array = Lists.newArrayList(1,5,6,9,2,8,4,3,7);
        System.out.println(JSONObject.toJSONString(array));
        System.out.println(JSONObject.toJSONString(quicklySort(array)));

    }


    /**
     * 直接插入排序：每次从当前数组中，选择下一个，放到新的数组的合适的位置
     * @param sourceList
     * @return
     */
    public static List<Integer> strightInsertSort(List<Integer> sourceList) {
        List<Integer> targetList = new ArrayList<Integer>();
        for (Integer source : sourceList) {
            if (targetList.size() == 0) {
                targetList.add(source);
                continue;
            }
            for (int i = 0; i < targetList.size(); i++) {
                Integer resultInt = targetList.get(i);
                if (i == 0 && resultInt > source) {
                    //当前结果数组里，最左端的一个数字，大于当前的source
                    targetList.add(0, source);
                    break;
                }
                if (i > 0 && resultInt > source && targetList.get(i - 1) < source) {
                    //当前结果数组里，存在介于source两侧的两个连续数字
                    targetList.add(i, source);
                    break;
                }
                //当前结果数组里，最右端的一个数字，小于当前的source
                if (i == targetList.size()-1 && resultInt < source) {
                    //当前结果数组里，最左端的一个数字，大于当前的source
                    targetList.add(source);
                    break;
                }
            }
        }
        return targetList;
    }


    /**
     * 简单选择排序：每次从当前剩余数组里，选择最小的一个，放到新数组里
     * @param sourceList
     * @return
     */
    public static List<Integer> simpleSelectSort(List<Integer> sourceList) {
        List<Integer> targetList = new ArrayList<Integer>();
        //定义空的结果数组，遍历原数组，每次找出当前最小的一个，放在结果数组里，同时从原数组移除
        return simpleSelectSort(sourceList,targetList);
    }

    private static List<Integer> simpleSelectSort(List<Integer> sourceList,List<Integer> targetList){
        int minNum = getMinNum(sourceList);
        targetList.add(minNum);
        sourceList.remove((Integer) minNum);
        if(sourceList.size()<1){
            return targetList;
        }
        return simpleSelectSort(sourceList,targetList);
    }

    private static int getMinNum(List<Integer> sourceList){
        int min = sourceList.get(0);
        for(Integer current: sourceList){
            if(min>current){
                min = current;
            }
        }
        return min;
    }


    /**
     * 冒泡排序：从第一个元素开始，和下一个元素比较大小，滚动换位置，直到把最大的一个放到列尾；依次把剩余最大的放到列尾
     * @param array
     * @return
     */
    public static List<Integer> buddleSort(List<Integer> array) {
        return null;
    }


    public static List<Integer> shellSort(List<Integer> array) {
        return null;
    }


    /**快速排序：
     * 每次选择一个元素作为基准，把其他元素以基准为界，分成两部分，此时基准元素到位；
     * 针对上次分出来的两部分，递归执行上一步
     * @param array
     * @return
     */
    public static List<Integer> quicklySort(List<Integer> array) {
        Integer[] result = new Integer[array.size()];
        doQuicklySort(array,0,result);
        return Arrays.asList(result);
    }

    private static void doQuicklySort(List<Integer> subArray,int headIndex,Integer[] result) {
        if(subArray.size() == 0){
            return;
        }
        if (subArray.size() == 1) {
            result[headIndex] = subArray.get(0);
            return;
        }
        int aimNum = findPivot(subArray.get(0), subArray.get(subArray.size() / 2), subArray.get(subArray.size()-1));
        List<Integer> leftSubArray = new ArrayList<Integer>();
        List<Integer> rightSubArray = new ArrayList<Integer>();
        for (Integer current : subArray) {
            if (current < aimNum) {
                leftSubArray.add(current);
            } else if(current > aimNum){
                rightSubArray.add(current);
            }
        }
        result[headIndex + leftSubArray.size()] = aimNum;
        doQuicklySort(leftSubArray, headIndex, result);
        doQuicklySort(rightSubArray, headIndex + leftSubArray.size() + 1, result);
    }

    private static int findPivot(int head,int body,int tail) {
        if (Math.max(head, body) < tail) {
            return Math.max(head, body);
        }else {
            return Math.max(Math.min(head, body),tail);
        }
    }
}
