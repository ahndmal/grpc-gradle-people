package com.anma;

import com.anma.grpc.Models;
import com.anma.grpc.PersonServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PClient {
    private final static int PORT = 9093;
    public static void main(String[] args) throws UnknownHostException {
        //client
        ManagedChannel channel =
                ManagedChannelBuilder.forAddress(
                                InetAddress.getLocalHost().getHostName(), PORT)
                        .usePlaintext()
                        .build();
        PersonServiceGrpc.PersonServiceBlockingStub stub = PersonServiceGrpc.newBlockingStub(channel);

        // schedule client requests
        ScheduledExecutorService service = Executors.newScheduledThreadPool(3);
        service.scheduleAtFixedRate(() -> {
            Models.PersonRequest personRequest = Models.PersonRequest.newBuilder().setName("petro").build();
            Models.PersonResponse person = stub.getPerson(personRequest);
            System.out.println(person.getPerson());
        }, 1L, 5L, TimeUnit.SECONDS);

        try {
            channel.awaitTermination(10, TimeUnit.SECONDS);
            channel.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

         public void serverStreamingExample(
        RequestType request,
        StreamObserver<ResponseType> responseObserver)
         */

        ManagedChannel channel2 = ManagedChannelBuilder.forAddress("localhost", PORT).build();
    }
}
