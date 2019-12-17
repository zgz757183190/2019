package com.zgz.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class NettyServer {

    private static int port = 8888;

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        connect(boss,work,bootstrap);
    }

    private static void connect(EventLoopGroup boss, EventLoopGroup work, ServerBootstrap bootstrap) throws InterruptedException {
        try {
            bootstrap.group(boss,work);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.handler(new LoggingHandler());
            //过滤器
            bootstrap.childHandler(new NettyServerFilter());
            bootstrap.option(ChannelOption.SO_BACKLOG,128).childOption(ChannelOption.SO_KEEPALIVE,true);

            //绑定端口，进行监听
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception ex){
            boss.shutdownGracefully();
            work.shutdownGracefully();
            System.err.println("服务器处理异常！");

            Thread.sleep(10000);
            EventLoopGroup bosss = new NioEventLoopGroup();
            EventLoopGroup works = new NioEventLoopGroup();
            ServerBootstrap bootstraps = new ServerBootstrap();
            connect(bosss,works,bootstraps);
        }
    }
}
