package com.zgz.nio.demo1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {

    public static void testClient() throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(8888));
        socketChannel.configureBlocking(false);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        buffer.put("sodix".getBytes());
        buffer.flip();

        socketChannel.write(buffer);
        socketChannel.close();
    }

    public static void main(String[] args) throws IOException {
        testClient();
    }
}
