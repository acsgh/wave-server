package com.acs.waveserver.examples;

import com.acs.waveserver.converter.json.JsonBodyReader;
import com.acs.waveserver.converter.json.JsonBodyWriter;
import com.acs.waveserver.converter.json.ObjectMapperProvider;
import com.acs.waveserver.core.Router;
import com.acs.waveserver.core.RouterBuilder;
import com.acs.waveserver.core.constants.RequestMethod;
import com.acs.waveserver.core.constants.ResponseStatus;
import com.acs.waveserver.provider.common.NettyServer;
import com.acs.waveserver.provider.common.NettyServerBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
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
        Map<Long, Person> persons = new HashMap<>();
        for (long i = 0; i < 10; i++) {
            persons.put(i, new Person(i, "Jonh Doe " + i, (int) (2 * i)));
        }
        RouterBuilder builder = new RouterBuilder();
        ObjectMapper objectMapper = new ObjectMapperProvider().getObjectMapper();
        JsonBodyWriter jsonBodyWriter = new JsonBodyWriter(objectMapper);
        JsonBodyReader<Person> jsonBodyReader = new JsonBodyReader<>(objectMapper, Person.class);

        builder.handler("/persons", RequestMethod.GET, (request, responseBuilder) -> {
            responseBuilder.body(persons.values(), jsonBodyWriter);
            return Optional.of(responseBuilder.build());
        });

        builder.handler("/persons", RequestMethod.POST, (request, responseBuilder) -> {
            Person person = request.body(jsonBodyReader);

            if (person != null) {
                responseBuilder.body(person, jsonBodyWriter);
                return Optional.of(responseBuilder.build());
            } else {
                return Optional.of(responseBuilder.error(ResponseStatus.NOT_FOUND));
            }
        });

        builder.handler("/persons/{id}", RequestMethod.GET, (request, responseBuilder) -> {
            Person person = persons.get(request.pathParams().getMandatory("id", Long.class));

            if (person != null) {
                responseBuilder.body(person, jsonBodyWriter);
                return Optional.of(responseBuilder.build());
            } else {
                return Optional.of(responseBuilder.error(ResponseStatus.NOT_FOUND));
            }
        });

        builder.handler("/persons/{id}", RequestMethod.DELETE, (request, responseBuilder) -> {
            Long id = request.pathParams().getMandatory("id", Long.class);

            if (persons.containsKey(id)) {
                persons.remove(id);
                return Optional.of(responseBuilder.build());
            } else {
                return Optional.of(responseBuilder.error(ResponseStatus.NOT_FOUND));
            }
        });
        return builder.build();
    }
}
