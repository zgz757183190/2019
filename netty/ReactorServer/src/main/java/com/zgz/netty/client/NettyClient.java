package com.zgz.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    private static Channel ch;
    public static String host = "127.0.0.1";
    public static int port = 8888;

    public static void main(String[] args) throws InterruptedException {
        //客户端连接设置
        EventLoopGroup loop = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        connect(loop,bootstrap);
    }

    public static void connect(EventLoopGroup loop,Bootstrap bootstrap) throws InterruptedException {
        try {
            bootstrap.group(loop);
            bootstrap.channel(NioSocketChannel.class);
            //过滤器设置，保持和server一致
            bootstrap.handler(new NettyClientFilter());
            ch = bootstrap.connect(host, port).sync().channel();
            send();
        }catch (Exception ex){
            //发生异常重连server
            loop.shutdownGracefully();

            System.err.println("重来 ............");
            Thread.sleep(10000);
            EventLoopGroup loops = new NioEventLoopGroup();
            Bootstrap bootstraps = new Bootstrap();
            connect(loops,bootstraps);
        }
    }

    public static void send() {
        String str="Hello Netty";
        for(int i = 0 ; i < 5 ; i ++){
            if(i == 4){
                str = "quit";
            }
            //发送数据以 \n结尾
            ch.writeAndFlush(str+ "\r\n");
        }
        System.out.println("客户端发送数据:"+str);
    }
}
