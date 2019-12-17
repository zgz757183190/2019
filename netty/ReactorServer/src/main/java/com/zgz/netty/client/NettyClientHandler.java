package com.zgz.netty.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import java.util.Date;

public class NettyClientHandler extends SimpleChannelInboundHandler {

    /** 客户端请求的心跳命令  */
    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(
            Unpooled.copiedBuffer("heart"+"\n",
            CharsetUtil.UTF_8));

    /** 空闲次数 */
    private int idle_count = 1;

    /**循环次数 */
    private int fcount = 1;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) {
        System.out.println("循环请求的时间："+new Date()+"，次数"+fcount);
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            if (IdleState.WRITER_IDLE.equals(event.state())) {
                if(idle_count <= 3){
                    idle_count++;
                    ctx.channel().writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
                }else{
                    System.out.println("不再发送心跳请求了！");
                }
                fcount++;
            }
        }
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        System.out.println("客户端接受的消息: " + msg.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("正在连接... ");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("连接关闭! ");
        System.err.println("10s后开始重连 ..........");

        Thread.sleep(10000);
        EventLoopGroup loop = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        NettyClient.connect(loop,bootstrap);

        super.channelInactive(ctx);
    }
}
