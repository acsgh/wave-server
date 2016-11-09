package com.acs.wave.provider.netty;

import com.acs.wave.router.WebSocketRouter;
import com.acs.wave.router.websocket.request.*;
import com.acs.wave.router.websocket.response.WebSocketResponse;
import com.acs.wave.router.websocket.response.WebSocketResponseBinary;
import com.acs.wave.router.websocket.response.WebSocketResponseText;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class NettyWebSocketFrameHandler extends WebSocketServerProtocolHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final WebSocketRouter webSocketRouter;
    private final String uri;
    private final String subprotocol;

    public NettyWebSocketFrameHandler(WebSocketRouter webSocketRouter, String uri, String subprotocol) {
        super(uri, subprotocol, true);
        this.webSocketRouter = webSocketRouter;
        this.uri = uri;
        this.subprotocol = subprotocol;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);

        WebSocketResponse response = webSocketRouter.handle(toWebSocketConnectedRequest(ctx));

        if (response.close) {
            ctx.close();
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);

        WebSocketResponse response = webSocketRouter.handle(toWebSocketDisconnectedRequest(ctx));

        if (response.close) {
            ctx.close();
        }
    }

    private WebSocketRequest toWebSocketConnectedRequest(ChannelHandlerContext ctx) {
        return new WebSocketRequestConnect(
                ctx.channel().id().asLongText(),
                uri,
                subprotocol,
                ctx.channel().remoteAddress().toString()
        );
    }

    private WebSocketRequest toWebSocketDisconnectedRequest(ChannelHandlerContext ctx) {
        return new WebSocketRequestDisconnect(
                ctx.channel().id().asLongText(),
                uri,
                subprotocol,
                ctx.channel().remoteAddress().toString()
        );
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
        WebSocketRequest request = getWebSocketRequest(ctx, frame);

        if (request != null) {
            WebSocketResponse response = webSocketRouter.handle(request);

            WebSocketFrame webSocketFrame = toWebSocketFrame(response);

            if (webSocketFrame != null) {
                ctx.channel().writeAndFlush(webSocketFrame);
            }
        } else {
            super.decode(ctx, frame, out);
        }
    }

    private WebSocketFrame toWebSocketFrame(WebSocketResponse response) {
        if (response instanceof WebSocketResponseText) {
            return new TextWebSocketFrame(((WebSocketResponseText) response).text);
        } else if (response instanceof WebSocketResponseBinary) {
            return new BinaryWebSocketFrame(Unpooled.wrappedBuffer(((WebSocketResponseBinary) response).bytes));
        } else {
            return null;
        }
    }

    private WebSocketRequest getWebSocketRequest(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame) {
            return getTextRequest(ctx, ((TextWebSocketFrame) frame));
        } else if (frame instanceof BinaryWebSocketFrame) {
            return getBinaryRequest(ctx, ((BinaryWebSocketFrame) frame));
        } else {
            return null;
        }
    }

    private WebSocketRequest getTextRequest(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        return new WebSocketRequestText(
                ctx.channel().id().asLongText(),
                uri,
                subprotocol,
                ctx.channel().remoteAddress().toString(),
                frame.text()
        );
    }

    private WebSocketRequest getBinaryRequest(ChannelHandlerContext ctx, BinaryWebSocketFrame frame) {
        return new WebSocketRequestBinary(
                ctx.channel().id().asLongText(),
                uri,
                subprotocol,
                ctx.channel().remoteAddress().toString(),
                ByteBufUtil.getBytes(frame.content())
        );
    }
}