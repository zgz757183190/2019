package com.zgz.nio.tcptest.server.many;

import com.zgz.nio.tcptest.server.single.AsyncHandler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable {

    private final ServerSocketChannel serverSocketChannel;

    // 获取CPU核心数
    private final int coreNum = Runtime.getRuntime().availableProcessors();

    // 创建selector给SubReactor使用，个数为CPU核心数（如果不需要那么多可以自定义，毕竟这里会吞掉一个线程）
    private final Selector[] selectors = new Selector[coreNum];

    // 轮询使用subReactor的下标索引
    private int next = 0;

    // subReactor
    private SubReactor[] reactors = new SubReactor[coreNum];

    // subReactor的处理线程
    private Thread[] threads = new Thread[coreNum];

    public Acceptor(ServerSocketChannel serverSocketChannel) throws IOException {
        this.serverSocketChannel = serverSocketChannel;
        // 初始化
        for (int i = 0; i < coreNum; i++) {
            selectors[i] = Selector.open();
            //初始化sub reactor
            reactors[i] = new SubReactor(selectors[i], i);
            //初始化运行sub reactor的线程
            threads[i] = new Thread(reactors[i]);
            threads[i].start();
        }
    }

    @Override
    public void run() {
        SocketChannel socketChannel;
        try {
            // 连接
            socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                System.out.println(String.format("收到来自 %s 的连接",
                        socketChannel.getRemoteAddress()));
                socketChannel.configureBlocking(false);
                // 注意一个selector在select时是无法注册新事件的，因此这里要先暂停下select方法触发的程序段，
                // 下面的weakup和这里的setRestart都是做这个事情的，具体参考SubReactor里的run方法
                reactors[next].registering(true);
                // 使一個阻塞住的selector操作立即返回
                selectors[next].wakeup();
                SelectionKey selectionKey = socketChannel.register(selectors[next],
                        // 当前客户端通道SocketChannel向selector[next]注册一个读事件，返回key
                        SelectionKey.OP_READ);
                // 使一個阻塞住的selector操作立即返回
                selectors[next].wakeup();
                // 本次事件注册完成后，需要再次触发select的执行，因此这里Restart要在设置回false（具体参考SubReactor里的run方法）
                reactors[next].registering(false);
                // 绑定Handler
                selectionKey.attach(new AsyncHandler(socketChannel, selectors[next]));
                if (++next == selectors.length) {
                    //越界后重新分配
                    next = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}