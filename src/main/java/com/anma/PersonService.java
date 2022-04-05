package com.anma;

import com.anma.grpc.Models;
import com.anma.grpc.PersonServiceGrpc;
import io.grpc.stub.StreamObserver;

public class PersonService extends PersonServiceGrpc.PersonServiceImplBase {

    @Override
    public void getPerson(Models.PersonRequest request, StreamObserver<Models.PersonResponse> responseObserver) {

        Models.Person vasyl = Models.Person.newBuilder()
                .setName("Vasyl")
                .setAge(45)
                .build();
        Models.PersonResponse response = Models.PersonResponse.newBuilder().setPerson(vasyl).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
