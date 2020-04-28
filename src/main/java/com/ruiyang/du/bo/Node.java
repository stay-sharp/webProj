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


}
