# Build
mvn clean package && docker build -t com.amrtm.microservice.store/grpc-service .

# RUN

docker rm -f grpc-service || true && docker run -d -p 8080:8080 -p 4848:4848 --name grpc-service com.amrtm.microservice.store/grpc-service 