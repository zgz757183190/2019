package com.zgz.netty.server;

import com.zgz.netty.client.NettyClientHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class NettyServerFilter extends ChannelInitializer {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // \n 结尾进行截取
        pipeline.addLast("framer",new DelimiterBasedFrameDecoder(8912,Delimiters.lineDelimiter()));
        //心跳
        pipeline.addLast(new IdleStateHandler(10,9,0,TimeUnit.SECONDS));
        pipeline.addLast("decoder",new StringDecoder());
        pipeline.addLast("encoder",new StringEncoder());
        pipeline.addLast("handler",new NettyServerHandler());
    }
}