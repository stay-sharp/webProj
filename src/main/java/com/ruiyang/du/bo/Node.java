package com.ruiyang.du.bo;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String id;
    private Node left;
    private Node right;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node(String id) {
        this.id = id;
    }

    public static void main(String[] args) {
        //无重复字符的最长子串
//        System.out.println(getMaxUnrepeatSubStringLength("abceabcdb"));

        //杨辉三角
//        List<List<Integer>> yangHuiTriangel = getYangHuiTriangel(5);
//        for(List<Integer> row:yangHuiTriangel){
//            System.out.println(row);
//        }

        //杨辉三角的最后一行
//        System.out.println(getLastrowOfYangHuiTriangel(2));

        //中序遍历
//        Node top=new Node("5");
//        Node three=new Node("3");
//        top.setLeft(three);
//        Node seven=new Node("7");
//        top.setRight(seven);
//
//        Node two=new Node("2");
//        three.setLeft(two);
//        Node four=new Node("4");
//        three.setRight(four);
//        Node six=new Node("6");
//        seven.setLeft(six);
//        Node eight=new Node("8");
//        seven.setRight(eight);
//
//        Node one=new Node("1");
//        two.setLeft(one);
//        Node nine=new Node("9");
//        eight.setRight(nine);
//
//        System.out.println(iteratorMiddle(top,""));
    }


    /**
     * 中序遍历
     * @return
     */
    private static String iteratorMiddle(Node dad,String stringBuilder) {
        Node left = dad.getLeft();
        Node right = dad.getRight();
        if(dad.hasLeft() && dad.hasRight()){
            return iteratorMiddle(left, stringBuilder)+dad.getId()+iteratorMiddle(right, stringBuilder);
        }else if(dad.hasLeft()){
            return iteratorMiddle(left, stringBuilder)+dad.getId();
        }else if(dad.hasRight()){
            return stringBuilder+dad.getId()+iteratorMiddle(right, stringBuilder);
        }else {
            return stringBuilder+dad.getId();
        }
    }

    public boolean hasLeft(){
        return this.getLeft()!=null;
    }
    public boolean hasRight(){
        return this.getRight()!=null;
    }

    /**
     * 直接获取杨辉三角的最后一行
     * @param rows
     * @return
     */
    private static List<Integer> getLastrowOfYangHuiTriangel(int rows) {
        List<Integer> row = new ArrayList<>(rows);
        int lastModified = 1;
        for (int i = 0; i < rows; i++) {
            if(i==0) {
                row.add(1);
            }else {
                lastModified = toLastRow(row, lastModified);
            }
        }
        return row;
    }

    private static int toLastRow(List<Integer> current, int lastModified) {
        for (int i = 1; i <= current.size(); i++) {
            Integer lastValue = lastModified;
            Integer anotherValue = 0;

            if (i < current.size()) {
                anotherValue = current.get(i);
                lastModified = current.get(i);
                current.set(i, lastValue + anotherValue);
            }else {
                current.add(lastValue + anotherValue);
                return lastModified;
            }
        }
        return 0;
    }



    /**
     * 获取杨辉三角
     * @param rows
     * @return
     */
    private static List<List<Integer>> getYangHuiTriangel(int rows) {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            List<Integer> row = new ArrayList<>();
            if(i==0) {
                row.add(1);
            }else {
                row = nextRow(result.get(i-1));
            }
            result.add(row);
        }
        return result;
    }

    private static List<Integer> nextRow(List<Integer> lastRow) {
        List<Integer> nextRow = new ArrayList<>();
        int sizeOfLast = lastRow.size();
        for (int i = 0; i < sizeOfLast + 1; i++) {
            Integer lastValue = 0;
            Integer anouther = 0;
            if(i==0){
                lastValue = lastRow.get(i);
            }else if (i < sizeOfLast) {
                lastValue = lastRow.get(i-1);
                anouther = lastRow.get(i);
            }else {
                lastValue = lastRow.get(i-1);
            }
            nextRow.add(lastValue + anouther);
        }
        return nextRow;
    }

    /**
     * 获取不含重复字符的最长子串的长度
     * @param str
     * @return
     */
    private static int getMaxUnrepeatSubStringLength(String str){
        int totalLength = str.length();
        if(totalLength==0){
            int lastMax = 0;
        }
        int lastMax = 1;
        for (int from = 0; from < totalLength; from++) {
            int currMax = 1;
            for (int to = from + 1; to < totalLength; to++) {
                if (checkSub(from, to, str)) {
                    break;
                }
                currMax++;
            }
            if (currMax > lastMax) {
                lastMax = currMax;
            }
        }
        return lastMax;
    }

    private static boolean checkSub(int from,int to,String str){
        if(to==str.length()){
            return true;
        }
        String substring = str.substring(from, to);
        char nextChar = str.charAt(to);
        return substring.contains(String.valueOf(nextChar));
    }

}
