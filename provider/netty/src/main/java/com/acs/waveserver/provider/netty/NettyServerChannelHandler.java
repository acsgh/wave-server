package com.acs.waveserver.provider.netty;

import com.acs.waveserver.core.HTTPRequest;
import com.acs.waveserver.core.HTTPResponse;
import com.acs.waveserver.core.Router;
import com.acs.waveserver.core.constants.ProtocolVersion;
import com.acs.waveserver.core.constants.RequestMethod;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

class NettyServerChannelHandler extends ChannelInboundHandlerAdapter {
    private static final byte[] CONTENT = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Router router;

    NettyServerChannelHandler(Router router) {
        this.router = router;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;

            if (HttpUtil.isKeepAlive(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }
            boolean keepAlive = HttpUtil.isKeepAlive(req);

            HTTPRequest waveRequest = getWaveRequest(req);
            HTTPResponse wareResponse = router.process(waveRequest);
            HttpResponse response = getNettyResponse(wareResponse);

            if (!keepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.write(response);
            }
        }
    }

    private HttpResponse getNettyResponse(HTTPResponse waveResponse) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                getNettyHttpVersion(waveResponse.protocolVersion),
                HttpResponseStatus.valueOf(waveResponse.responseStatus.code),
                Unpooled.wrappedBuffer(CONTENT)
        );

        response.headers().set(CONTENT_TYPE, "text/plain");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }



    private HTTPRequest getWaveRequest(HttpRequest request) {
        return new HTTPRequest(
                getWaveRequestMethod(request.method()),
                request.uri(),
                getWaveHTTPVersion(request.protocolVersion())
        );
    }

    private HttpVersion getNettyHttpVersion(ProtocolVersion protocolVersion) {
        return HttpVersion.valueOf(protocolVersion.toString());
    }

    private ProtocolVersion getWaveHTTPVersion(HttpVersion httpVersion) {
        return ProtocolVersion.fromString(httpVersion.toString().toUpperCase());
    }

    private RequestMethod getWaveRequestMethod(HttpMethod method) {
        return RequestMethod.fromString(method.name().toUpperCase());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Error during request", cause);
        ctx.close();
    }
}