package com.liu.netty;

import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import org.apache.log4j.Logger;

import com.liu.helper.Configuration;
import com.liu.message.EnDecryptor;

public class MessageServerHandler extends ChannelInboundHandlerAdapter {
    public static final Logger logger = Logger.getLogger(MessageServerHandler.class);
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final AttributeKey<Boolean> IS_AUTH = AttributeKey.valueOf("HTTP_IS_AUTH");

    private HttpRequest request;
    /** Buffer that stores the response content */
    private final StringBuilder buf = new StringBuilder();
    private static Configuration conf = new Configuration();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.debug("Channel is active");
        ctx.attr(IS_AUTH).set(false);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.debug("Channel is inactive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        logger.debug("Receive message in Server");
        ctx.attr(IS_AUTH).set(false);
        try {
            if (msg instanceof HttpRequest) {
                HttpRequest request = this.request = (HttpRequest)msg;

                if (is100ContinueExpected(request)) {
                    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
                    ctx.writeAndFlush(response);
                }

                // Clear the buffer
                buf.setLength(0);

                if (request.getMethod().equals(HttpMethod.GET)) {
                    logger.debug("HTTP method GET is not supported by this URL");
                    NettyResponse.write(ctx.channel(), Configuration.RES_CODE_INPUT_INVALID,
                             "HTTP method GET is not supported by this URL", request);
                    return;
                }

                String contentType = request.headers().get(CONTENT_TYPE);
                if (contentType == null || !contentType.contains("application/json")) {
                    logger.debug("Content-Type is not application/json");
                    NettyResponse.write(ctx.channel(), Configuration.RES_CODE_INPUT_INVALID,
                             "Content-Type is not application/json", request);
                    return;
                }
            }

            if (msg instanceof HttpContent) {
                logger.debug("Get Http request content");
                HttpContent httpContent = (HttpContent)msg;
                ByteBuf content = httpContent.content();
                if (content.isReadable()) {
                    buf.append(content.toString(CharsetUtil.UTF_8));
                }

                if (msg instanceof LastHttpContent) {
                    String logInput = buf.toString();
                    logger.debug("Input json: " + logInput);
                    
                    String jsonInput = EnDecryptor.decrypt(logInput, conf.getCryptKey());
                    if(jsonInput == null) {
                    	logger.error("Decrypt failed," + logInput);
                    	NettyResponse.write(ctx.channel(), Configuration.RES_CODE_SERVER_ERROR, "Decrypt failed.", request);
                    	return;
                    }
                    logger.debug("decrypted: " + jsonInput);

                    InputHandlerPool.submit(ctx.channel(), jsonInput, isKeepAlive(request));
                }
            }
        } catch (Throwable e) {
            logger.error("Error occurred in channelRead", e);
            NettyResponse.write(ctx.channel(), Configuration.RES_CODE_SERVER_ERROR, "server error.", request);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Error occurred in MessageServerHandler", cause);
        ctx.close();
    }
}
