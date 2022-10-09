podman login quay.io
podman build -f Containerfile.distroless . -t quay.io/jstakun/camel-quarkus-mongodb-client:latest
podman push quay.io/jstakun/camel-quarkus-mongodb-client:latest
