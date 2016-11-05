package com.acs.waveserver.examples;

import com.acs.waveserver.converter.json.JsonBodyReader;
import com.acs.waveserver.converter.json.JsonBodyWriter;
import com.acs.waveserver.converter.json.json.ObjectMapperProvider;
import com.acs.waveserver.core.Router;
import com.acs.waveserver.core.RouterBuilder;
import com.acs.waveserver.core.constants.RequestMethod;
import com.acs.waveserver.core.constants.ResponseStatus;
import com.acs.waveserver.core.files.StaticClasspathFolderFilter;
import com.acs.waveserver.core.files.StaticFilesystemFolderFilter;
import com.acs.waveserver.provider.common.NettyServer;
import com.acs.waveserver.provider.common.NettyServerBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public final class Boot {

    public static void main(String[] args) throws Exception {
        Router router = getRouter();

        NettyServer nettyServer = new NettyServerBuilder()
                .router(router)
                .build();

        nettyServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(nettyServer::stop));

    }

    private static Router getRouter() throws FileNotFoundException {
        Map<Long, Person> persons = new HashMap<>();
        AtomicLong ids = new AtomicLong(0);

        for (long i = 0; i < 10; i++) {
            Long id = ids.addAndGet(1);
            persons.put(id, new Person(id, "John Doe " + id, (int) (2 * id)));
        }
        RouterBuilder builder = new RouterBuilder();
        ObjectMapper objectMapper = new ObjectMapperProvider(true).getObjectMapper();
        JsonBodyWriter jsonBodyWriter = new JsonBodyWriter(objectMapper);
        JsonBodyReader<Person> jsonBodyReader = new JsonBodyReader<>(objectMapper, Person.class);

        builder.handler("/persons", (request, responseBuilder) -> {
            responseBuilder.body(persons.values(), jsonBodyWriter);
            return Optional.of(responseBuilder.build());
        }, RequestMethod.GET);

        builder.handler("/persons", (request, responseBuilder) -> {
            Person person = request.body(jsonBodyReader);

            person = new Person(ids.addAndGet(1), person.getName(), person.getAge());
            persons.put(person.getId(), person);
            responseBuilder.body(person.getId(), jsonBodyWriter);
            responseBuilder.status(ResponseStatus.CREATED);
            return Optional.of(responseBuilder.build());
        }, RequestMethod.POST);

        builder.handler("/persons/{id}", (request, responseBuilder) -> {
            Person person = persons.get(request.pathParams().getMandatory("id", Long.class));
            Person personNew = request.body(jsonBodyReader);

            if (person != null) {
                personNew = new Person(person.getId(), personNew.getName(), personNew.getAge());
                persons.put(personNew.getId(), personNew);
                responseBuilder.body(personNew.getId(), jsonBodyWriter);
                return Optional.of(responseBuilder.build());
            } else {
                return Optional.of(responseBuilder.error(ResponseStatus.NOT_FOUND));
            }
        }, RequestMethod.PUT);

        builder.handler("/persons/{id}", (request, responseBuilder) -> {
            Person person = persons.get(request.pathParams().getMandatory("id", Long.class));

            if (person != null) {
                responseBuilder.body(person, jsonBodyWriter);
                return Optional.of(responseBuilder.build());
            } else {
                return Optional.of(responseBuilder.error(ResponseStatus.NOT_FOUND));
            }
        }, RequestMethod.GET);

        builder.handler("/persons/{id}", (request, responseBuilder) -> {
            Long id = request.pathParams().getMandatory("id", Long.class);

            if (persons.containsKey(id)) {
                persons.remove(id);
                return Optional.of(responseBuilder.build());
            } else {
                return Optional.of(responseBuilder.error(ResponseStatus.NOT_FOUND));
            }
        }, RequestMethod.DELETE);

//        builder.filter("/*", new StaticClasspathFolderFilter("public", true));
        builder.filter("/webjars/{path+}", new StaticClasspathFolderFilter("META-INF/resources/webjars", true));
        return builder.build();
    }
}
