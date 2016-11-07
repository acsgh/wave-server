package com.acs.wave.provider.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;

class NettyWebSocketFrameHandler extends WebSocketServerProtocolHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public NettyWebSocketFrameHandler(String websocketPath) {
        super(websocketPath);
    }

    public NettyWebSocketFrameHandler(String websocketPath, String subprotocols) {
        super(websocketPath, subprotocols);
    }

    public NettyWebSocketFrameHandler(String websocketPath, String subprotocols, boolean allowExtensions) {
        super(websocketPath, subprotocols, allowExtensions);
    }

    public NettyWebSocketFrameHandler(String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize) {
        super(websocketPath, subprotocols, allowExtensions, maxFrameSize);
    }

    public NettyWebSocketFrameHandler(String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize, boolean allowMaskMismatch) {
        super(websocketPath, subprotocols, allowExtensions, maxFrameSize, allowMaskMismatch);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
        if (frame instanceof TextWebSocketFrame) {
            // Send the uppercase string back.
            String request = ((TextWebSocketFrame) frame).text();
            logger.info("{} received {}", ctx.channel(), request);
            ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase(Locale.US)));
        } else {
            super.decode(ctx, frame, out);
        }
    }
}