package com.zgz.nio.tcptest.server.single;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        new Thread(new Reactor(8888)).start();
    }
}