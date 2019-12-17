package com.zgz.nio.tcptest.server.many;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public class SubReactor implements Runnable {

    private final Selector selector;

    //注册开关表示，为什么要加这么个东西，可以参考Acceptor设置这个值那里的描述
    private boolean register = false;

    //序号，也就是Acceptor初始化SubReactor时的下标
    private int num;

    SubReactor(Selector selector, int num) {
        this.selector = selector;
        this.num = num;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            System.out.println(String.format("%d号SubReactor等待注册中...", num));
            while (!Thread.interrupted() && !register) {
                try {
                    if (selector.select() == 0) {
                        continue;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Set selectedKeys = selector.selectedKeys();
                Iterator it = selectedKeys.iterator();
                while (it.hasNext()) {
                    dispatch((SelectionKey) it.next());
                    it.remove();
                }
            }
        }
    }

    private void dispatch(SelectionKey key) {
        Runnable r = (Runnable) (key.attachment());
        if (r != null) {
            r.run();
        }
    }

    void registering(boolean register) {
        this.register = register;
    }

}
