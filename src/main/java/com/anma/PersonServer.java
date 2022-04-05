package com.anma;

import io.grpc.ServerBuilder;

public class PersonServer {
    public static void main(String[] args) {
        var server = ServerBuilder.forPort(9091)
                .addService(new PersonService())
                .build();

    }
}
