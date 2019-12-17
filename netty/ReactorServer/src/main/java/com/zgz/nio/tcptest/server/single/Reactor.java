package com.zgz.nio.tcptest.server.single;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable {

    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;

    public Reactor(int port) throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        selector = Selector.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(port));
        SelectionKey sk = serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor(serverSocketChannel,selector));
    }

    @Override
    public void run() {
        while (!Thread.interrupted()){
            try {
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while (it.hasNext()){
                    dispatch((SelectionKey)it.next());
                }
                selected.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void dispatch(SelectionKey key) {
        Runnable r = (Runnable) key.attachment();
        if(r!=null){
            r.run();
        }
    }
}
