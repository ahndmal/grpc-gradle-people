java_grpc:
	protoc --java_out=outt --proto_path=/home/malandr/IdeaProjects/grpc-gradle-people/src/main/proto/models --java-grpc_out=se
go_grpc:
	protoc --proto_path=proto proto/*.proto --go_out=server --go-grpc_out=server
