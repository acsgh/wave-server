package com.acs.waveserver.examples;

import com.acs.waveserver.core.Router;
import com.acs.waveserver.core.RouterBuilder;
import com.acs.waveserver.core.constants.RequestMethod;
import com.acs.waveserver.provider.common.NettyServer;
import com.acs.waveserver.provider.common.NettyServerBuilder;

import java.util.Optional;

public final class Boot {

    public static void main(String[] args) throws Exception {
        Router router = getRouter();

        NettyServer nettyServer = new NettyServerBuilder()
                .router(router)
                .build();

        nettyServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(nettyServer::stop));

    }

    private static Router getRouter() {
        RouterBuilder builder = new RouterBuilder();
        builder.filter("/", RequestMethod.GET, (request, responseBuilder, next) -> {
            responseBuilder.header("filter1", "1");
            return next.get();
        });
        builder.filter("/{id}", RequestMethod.GET, (request, responseBuilder, next) -> {
            responseBuilder.header("filter2", "1");
            return next.get();
        });

        builder.handler("/{id}", RequestMethod.GET, (request, responseBuilder) -> {
            responseBuilder.header("Content-Type", "text/html");
            responseBuilder.body("Hello from Handler: " + request.pathParams().get("id", Long.class) + "<br/>" + request.fullUri());
            return Optional.of(responseBuilder.build());
        });
        return builder.build();
    }
}
