package com.zgz.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetAddress;
import java.util.Date;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static int numbers = 1;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
        if(obj instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) obj;
            if(IdleState.READER_IDLE.equals(event.state())){
                System.out.println("已经10s没有接收到客户端的信息了");
                if(numbers > 2){
                    System.out.println("关闭这个不活跃的channel");
                    ctx.channel().close();
                }
                numbers ++;
            }
        }else{
            super.userEventTriggered(ctx,obj);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接的客户端地址:" + ctx.channel().remoteAddress());
        ctx.writeAndFlush("客户端"+ InetAddress.getLocalHost().getHostName() + "成功与服务端建立连接！ \n");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {
        String msg = object.toString();
        System.err.println("服务器收到信息：" + msg);
        if("heart".equals(msg)){
            msg = "服务器收到了心跳包！";
            ctx.writeAndFlush(msg+"\n");
        }else{
            Date date=new Date();
            // 返回客户端消息
            ctx.writeAndFlush(date+"\n");
        }
    }

}