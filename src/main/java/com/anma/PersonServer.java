package com.anma;

import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/*
API - https://grpc.github.io/grpc-java/javadoc/
There are four types of gRPC service methods: unary,
server-streaming, client-streaming, and bidirectional-streaming.
 */
public class PersonServer {
    private final static int PORT = 9093;
    private final Server server;

    public PersonServer(int port) {
        this.server = ServerBuilder.forPort(port)
                .addService(new PersonService())
                .build();
    }

    public void start() {
        try {
            server.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    PersonServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException, UnknownHostException {
        PersonServer server = new PersonServer(PORT);
        server.start();
        server.blockUntilShutdown();

        ManagedChannel channel =
                ManagedChannelBuilder.forAddress(
                                InetAddress.getLocalHost().getHostName(), PORT)
                        .build();
        channel.awaitTermination(30, TimeUnit.SECONDS);
        ManagedChannel shutdown = channel.shutdown();

        StreamObserver<Person> responseObserver = new StreamObserver<Person>() {
            @Override
            public void onNext(Person value) {
                System.out.println(value.getAge());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        };

        // Auth
        /*
        ServerCall<?, ?> call;
        Status status = AuthorizationUtil.clientAuthorizationCheck(
            call, Lists.newArrayList("foo@iam.gserviceaccount.com"));
         */

        ManagedChannel channel1 = ManagedChannelBuilder.forAddress("localhost", PORT).build();
    }
}
