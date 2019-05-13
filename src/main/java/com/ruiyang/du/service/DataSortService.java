package com.ruiyang.du.service;

/**
 * 常见排序算法
 */
public interface DataSortService {

    /**
     * 直接插入排序
     * @param array
     * @return
     */
    Integer[] StrightInsertSort(Integer[] array);

    /**
     * 简单选择排序
     * @param array
     * @return
     */
    Integer[] SimpleSelectSort(Integer[] array);

    /**
     * 冒泡排序
     * @param array
     * @return
     */
    Integer[] BuddleSort(Integer[] array);

    /**
     * 希尔排序
     * @param array
     * @return
     */
    Integer[] ShellSort(Integer[] array);

    /**
     * 快速排序
     * @param array
     * @return
     */
    Integer[] QuicklySort(Integer[] array);
}
