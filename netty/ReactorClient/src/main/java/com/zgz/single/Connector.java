package com.zgz.single;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Connector implements Runnable {

    private final Selector selector;

    private final SocketChannel socketChannel;

    Connector(SocketChannel socketChannel, Selector selector) {
        this.socketChannel = socketChannel;
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            //这里连接完成（与服务端的三次握手完成）
            if (socketChannel.finishConnect()) {
                System.out.println(String.format("已完成 %s 的连接",
                        socketChannel.getRemoteAddress()));
                //连接建立完成后，接下来的动作交给Handler去处理（读写等）
                new Handler(socketChannel, selector);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
