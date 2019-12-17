package com.zgz.nio.tcptest.server.single;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable {
    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;

    public Acceptor(ServerSocketChannel serverSocketChannel, Selector selector) {
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            SocketChannel serverSocket = serverSocketChannel.accept();
            if(serverSocket!=null){
                new Handler(serverSocket,selector);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
