package com.acs.waveserver.provider.common;

import com.acs.waveserver.core.HTTPHeaders;
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
                Unpooled.wrappedBuffer(waveResponse.getBytes())
        );

        waveResponse.headers.stream().forEach(header -> response.headers().set(header.key, header.value));

        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }


    private HTTPRequest getWaveRequest(HttpRequest request) {
        return new HTTPRequest(
                getWaveRequestMethod(request.method()),
                request.uri(),
                getWaveHTTPVersion(request.protocolVersion()),
                getHeaders(request),
                ""
        );
    }

    private HTTPHeaders getHeaders(HttpRequest request) {
        HTTPHeaders result = new HTTPHeaders();

        if (request instanceof FullHttpRequest) {
            FullHttpRequest fullRequest = (FullHttpRequest) request;
            fullRequest.headers().forEach(header -> result.add(header.getKey(), header.getValue()));
        }

        return result;
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