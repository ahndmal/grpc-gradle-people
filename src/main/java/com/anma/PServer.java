package com.anma;

import com.anma.grpc.Models;
import com.anma.grpc.PersonServiceGrpc;
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
public class PServer {
    private final static int PORT = 9093;
    private final Server server;

    public PServer(int port) {
        this.server = ServerBuilder.forPort(port)
                .addService(new PersonService())
                .build();
    }

    public void start() {
        System.out.println("Server started!");
        try {
            server.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    PServer.this.stop();
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

    // await termination
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException, UnknownHostException {
        PServer server = new PServer(PORT);
        server.start();
        server.blockUntilShutdown();



    }
}
