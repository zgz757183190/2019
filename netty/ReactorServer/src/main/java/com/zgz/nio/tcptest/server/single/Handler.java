package com.zgz.nio.tcptest.server.single;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Handler implements Runnable {
    private final SelectionKey selectionKey;
    private final SocketChannel socketChannel;

    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer sendBuffer = ByteBuffer.allocate(2048);

    private final static int READ = 0;
    private final static int SEND = 1;

    private int status = READ;

    public Handler(SocketChannel socketChannel, Selector selector) throws IOException {
        this.socketChannel = socketChannel;
        this.socketChannel.configureBlocking(false);
        selectionKey = socketChannel.register(selector,0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            switch (status) {
                case READ:
                    read();
                    break;
                case SEND:
                    send();
                    break;
                default:
            }
        } catch (IOException e) {
            selectionKey.cancel();
            try {
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void read() throws IOException{
        if(selectionKey.isValid()){
            readBuffer.clear();
            int count = socketChannel.read(readBuffer);
            if(count > 0 ){
                System.out.println(String.format("收到来自 %s 的消息: %s",
                        socketChannel.getRemoteAddress(),
                        new String(readBuffer.array())));
                status = SEND;
                selectionKey.interestOps(SelectionKey.OP_WRITE);
            }else{
                socketChannel.close();
                selectionKey.cancel();
            }
        }
    }

    private void send() throws IOException{
        if(selectionKey.isValid()){
            sendBuffer.clear();
            sendBuffer.put(String.format("我收到来自%s的信息辣：%s,  200ok;",
                    socketChannel.getRemoteAddress(),
                    new String(readBuffer.array())).getBytes());
            sendBuffer.flip();
            int count = socketChannel.write(sendBuffer);
            if(count < 0 ){
                selectionKey.cancel();
                socketChannel.close();
            }
            status = READ;
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }
}