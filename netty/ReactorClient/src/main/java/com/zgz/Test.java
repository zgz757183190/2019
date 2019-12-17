package com.zgz;

import com.zgz.single.NIOClient;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        new Thread(new NIOClient("127.0.0.1", 8888)).start();
    }
}
