package com.zgz.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {

    public static void testClient() throws IOException {
        //创建TCP通道，并设置非阻塞
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(8888));
        socketChannel.configureBlocking(false);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("123".getBytes());
        /**
         * put方法写入数据之后，可以直接从buffer中读吗？
         *
         * 呵呵，不能。
         *
         * 还需要调用filp（）走一个转换的工作。flip()方法是Buffer的一个模式转变的重要方法。
         * 简单的说，是写模式翻转成读模式——写转读。
         */
        buffer.flip();

        //将输入写入到通道
        socketChannel.write(buffer);
        socketChannel.close();
    }

    public static void main(String[] args) throws IOException {
        testClient();
    }
}
