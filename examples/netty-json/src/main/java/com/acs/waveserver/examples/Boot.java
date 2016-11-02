package com.acs.waveserver.examples;

import com.acs.waveserver.core.Router;
import com.acs.waveserver.core.RouterBuilder;
import com.acs.waveserver.core.constants.RequestMethod;
import com.acs.waveserver.core.constants.ResponseStatus;
import com.acs.waveserver.provider.common.NettyServer;
import com.acs.waveserver.provider.common.NettyServerBuilder;

import java.util.Optional;

public final class Boot {

    public static void main(String[] args) throws Exception {
        JsonSupport jsonSupport = new JsonSupport();
        Router router = getRouter(jsonSupport);

        NettyServer nettyServer = new NettyServerBuilder()
                .router(router)
                .build();

        nettyServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(nettyServer::stop));

    }

    private static Router getRouter(JsonSupport jsonSupport) {
        RouterBuilder builder = new RouterBuilder();

        builder.handler("/person/{id}", RequestMethod.GET, (request, responseBuilder) -> {
            responseBuilder.header("Content-Type", "application/json");
            responseBuilder.body(new Person(1L, "Jonh Doe", 30), jsonSupport::marshall);
            return Optional.of(responseBuilder.build());
        });
        return builder.build();
    }
}
