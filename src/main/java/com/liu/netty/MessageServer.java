package com.liu.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.liu.helper.Configuration;
import com.liu.helper.QueueHelper;

public class MessageServer {
    public static final Logger logger = Logger.getLogger(MessageServer.class);

    private int port;

    public MessageServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decoder", new HttpRequestDecoder());
                            ch.pipeline().addLast("encoder", new HttpResponseEncoder());
                            ch.pipeline().addLast("handler", new MessageServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Start the server.
            ChannelFuture f = b.bind(port).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            PropertyConfigurator.configure(Configuration.DEFAULT_CONF_PATH);

            final int httpPort;
            if (args.length != 1) {
                logger.error("Missing httpPort, abort");
                return;
            } else {
                httpPort = Integer.parseInt(args[0]);
            }

            logger.info("Initializing input handler pool");
            if (!InputHandlerPool.init()) {
                logger.error("Error occurred during initializing input handler pool, abort");
                return;
            }
            
            logger.info("Initializing QueueHelper");
            if(!QueueHelper.init()) {
            	logger.error("Error occurred during initializing input handler pool, abort");
            	return;
            }

            logger.info("Starting Http server on " + httpPort);
            new MessageServer(httpPort).run();
        } catch (Throwable e) {
            logger.error("Error occurred in starting Http server", e);
            System.exit(-1);
        }
    }
}
