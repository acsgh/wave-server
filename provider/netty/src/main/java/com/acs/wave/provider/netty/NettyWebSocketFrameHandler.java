package com.acs.wave.provider.netty;

import com.acs.wave.router.WebSocketRouter;
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

    private final WebSocketRouter webSocketRouter;

    public NettyWebSocketFrameHandler(WebSocketRouter webSocketRouter, String uri, String subprotocol) {
        super(uri, subprotocol, true);

        this.webSocketRouter = webSocketRouter;
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
        if (frame instanceof TextWebSocketFrame) {
            String request = ((TextWebSocketFrame) frame).text();
            logger.info("{} received {}", ctx.channel(), request);
            ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase(Locale.US)));
        } else {
            super.decode(ctx, frame, out);
        }
    }
}