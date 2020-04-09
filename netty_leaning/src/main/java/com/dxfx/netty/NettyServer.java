package com.dxfx.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.EventExecutorGroup;

public class NettyServer {
    public static void main(String[] args) {
        //1第一个线程组用于接收client端连接的
        NioEventLoopGroup parentGroup = new NioEventLoopGroup(1);
        //2第二个线程组是用于实际业务处理的操作
        NioEventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            //3创建一个辅助类bBootstrap,就是对我们的Server进行一系列的配置
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //把列个工作线程组加进来
            serverBootstrap.group(parentGroup, childGroup);
            //指定使用NioServerSokerChannel这种类型的通道
            serverBootstrap.channel(NioServerSocketChannel.class);
            //一定要使用childHandler去绑定具体的事件处理器
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                            ch.pipeline().addLast(new SimpleHandler());
                            ch.pipeline().addLast(new StringEncoder());
                        }
                    });
            //绑定指定端口进行监听
            ChannelFuture future = serverBootstrap.bind(8080).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //等待关闭
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }


    }

}
