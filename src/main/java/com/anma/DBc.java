package com.anma;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static io.r2dbc.postgresql.PostgresqlConnectionFactoryProvider.OPTIONS;
import static io.r2dbc.spi.ConnectionFactoryOptions.*;

public class DBc {

    public String connectToDb() {
        ConnectionFactory connectionFactory = ConnectionFactories.get("r2dbc:postgresql://172.17.0.2:5432/persons");
        Publisher<? extends Connection> connectionPublisher = connectionFactory.create();
//        connectionPublisher.subscribe();
        return "";
    }

    public void connectProg() {
        Map<String, String> options = new HashMap<>();
//        options.put("lock_timeout", "10s");
//        options.put("statement_timeout", "5m");

        ConnectionFactory connectionFactory = ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, "postgresql")
                .option(HOST, System.getenv("HOST"))
                .option(USER, "dev")
                .option(PASSWORD, System.getenv("PASS"))
                .option(DATABASE, "persons")  // optional
//                .option(OPTIONS, options) // optional
                .build());

//        Publisher<? extends Connection> connectionPublisher = connectionFactory.create();
        Mono<Connection> connectionMono = Mono.from(connectionFactory.create());
        connectionMono.flatMapMany(conn -> conn.createStatement("select * from persons").execute())
                .doOnNext(res -> {
                    res.map(it -> {
//                        Object id = (String) it.g(0);
                        System.out.println(it);
                        return it;
                    });
                })
                .subscribe();

    }
}
