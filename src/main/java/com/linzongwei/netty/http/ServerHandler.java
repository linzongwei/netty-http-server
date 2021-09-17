package com.linzongwei.netty.http;

import com.sun.xml.internal.ws.api.pipe.ContentType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author linli
 * @since 2021/2/22 9:24
 */
@Sharable
public class ServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
            FullHttpRequest fullHttpRequest) throws Exception {

        String requestUri = fullHttpRequest.uri();
        Map<String,String> result = new HashMap<>();
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(requestUri);
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        for (Map.Entry<String, List<String>> attr : parameters.entrySet()) {
            for (String attrVal : attr.getValue()) {
                result.put(attr.getKey(), attrVal);
            }
        }

        String responseBody = "<html><body>" + result.toString() + "</body></html>";
        byte[] responseByte = responseBody.getBytes();
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.valueOf("HTTP/1.1"), HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseByte));
        response.headers().set("Content-Type", "text/html; charset=utf-8");
        response.headers().setInt("Content-Length", response.content().readableBytes());
        channelHandlerContext.write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
