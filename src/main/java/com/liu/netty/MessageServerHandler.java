package com.liu.netty;

import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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

import java.nio.ByteBuffer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.liu.helper.Configuration;
import com.liu.helper.CryptHelper;

public class MessageServerHandler extends ChannelInboundHandlerAdapter {
    public static final Logger logger = Logger.getLogger(MessageServerHandler.class);
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final AttributeKey<Boolean> IS_AUTH = AttributeKey.valueOf("HTTP_IS_AUTH");
    private static final int BUFFER_SIZE = 1024 * 1024;

    private HttpRequest request;
    /** Buffer that stores the response content */
//    private final StringBuilder buf = new StringBuilder();
    private ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
//    private ByteBuf buf = Unpooled.directBuffer();
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
        boolean done = false;
        try {
            if (msg instanceof HttpRequest) {
                HttpRequest request = this.request = (HttpRequest)msg;

                if (is100ContinueExpected(request)) {
                    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
                    ctx.writeAndFlush(response);
                }

                // Clear the buffer
//                buf.setLength(0);
                buf.clear();

                if (request.getMethod().equals(HttpMethod.GET)) {
                    logger.info("HTTP method GET is not supported by this URL");
                    NettyResponse.write(ctx.channel(), Configuration.RES_CODE_INPUT_INVALID,
                             "HTTP method GET is not supported by this URL", request);
                    done = true;
                    return;
                }

                String contentType = request.headers().get(CONTENT_TYPE);
                if (contentType == null || !contentType.contains("application/x-gzip")) {
                    logger.info("Content-Type is not application/x-gzip");
                    NettyResponse.write(ctx.channel(), Configuration.RES_CODE_INPUT_INVALID,
                             "Content-Type is not application/x-gzip", request);
                    done = true;
                    return;
                }
            }

            if (msg instanceof HttpContent) {
                logger.debug("Get Http request content");
                if(done) {
                	logger.debug("http head error, body dropped.");
                	return;
                }
                
                HttpContent httpContent = (HttpContent)msg;
                ByteBuf content = httpContent.content();
                
                logger.debug("$content.readableBytes() = " + content.readableBytes());
                if (content.isReadable()) {
                	byte[] data = new byte[content.readableBytes()];
//                    buf.append(content.toString(CharsetUtil.UTF_8));
                	content.getBytes(0, data);
                	buf.put(data);
//                	buf.put(content.array());
                }
                
                if (msg instanceof LastHttpContent) {
//                    String logInput = buf.toString();
//                    logger.debug("Input json: " + logInput);
                    
                	if(buf.position() == 0){
                		logger.info("data empty, return.");
                		NettyResponse.write(ctx.channel(), Configuration.RES_CODE_INPUT_INVALID, "data empty.", request);
                		return;
                	}
                	
                	byte[] contentBytes = new byte[buf.position()];
                	for(int i = 0; i < buf.position(); i++)
                	    contentBytes[i] = buf.array()[i];
                	
                    String jsonInput = CryptHelper.decrypt(contentBytes, conf.getCryptKey());
//                    String jsonInput = logInput;
                    if(jsonInput == null) {
                    	logger.error("Decrypt failed(base64)," + new String(CryptHelper.base64Encode(contentBytes)));
                    	NettyResponse.write(ctx.channel(), Configuration.RES_CODE_INPUT_INVALID, "Decrypt failed.", request);
                    	return;
                    }
                    logger.info("$input_decrypted: " + jsonInput);
                    
                    if(!checkInput(jsonInput)) {
                    	NettyResponse.write(ctx.channel(), Configuration.RES_CODE_INPUT_INVALID, "Input is not a json.", request);
                    	return;
                    }

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
    
    private static boolean checkInput(String inputContent) {
    	return !StringUtils.isEmpty(inputContent) && inputContent.contains("{") && inputContent.contains("}");
    }
}
