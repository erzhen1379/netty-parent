package com.dxfx.netty;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("客户端读取数据");
        System.out.println("接收到服务器的数据为：" + msg.toString());
        ctx.channel().attr(AttributeKey.valueOf("ChannelKey")).set(msg.toString());
        ctx.channel().close();
    }
}
