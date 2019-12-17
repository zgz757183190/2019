package com.zgz.count.stack;

public class MyStackTest {
    public static void main(String[] args) {
        MyStack stack = new MyStack(6);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println(stack.peek());
        while(!stack.isEmpty()){
            System.out.println(stack.pop());
        }
    }
}
