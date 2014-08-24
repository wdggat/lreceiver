package com.liu.netty;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import org.apache.log4j.Logger;

import com.liu.message.Response;

public class NettyResponse {
    private static Logger logger = Logger.getLogger(NettyResponse.class);

    public static void write(Channel channel, int code,
                             String errMsg, HttpRequest request) {
    	Response response = genResponse(code, errMsg);
        write(channel, response, request);
    }

    public static void write(Channel channel, Response response,
                             HttpRequest request) {
        write(channel, response, isKeepAlive(request));
    }

    public static void write(Channel channel, Response res,
                             boolean isKeepAlive) {
        ByteBuf buf = copiedBuffer(res.toString(), CharsetUtil.UTF_8);

        // Decide whether to close the connection or not
        boolean close = isKeepAlive;

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        if (!close) {
            // Add keep alive header as per:
            // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        ChannelFuture future = channel.writeAndFlush(response);
        // Close the connection after the write operation is done if necessary
        if (!close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    public static Response genResponse(int code, String errMsg) {
        return new Response(code, errMsg);
    }
}

