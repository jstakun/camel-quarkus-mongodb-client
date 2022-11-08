VERSION=latest
AUTHFILE=/home/jstakun/camelquarkusmongoclient.auth
#podman login quay.io
podman build -f Containerfile.distroless . -t quay.io/jstakun/camel-quarkus-mongodb-client:$VERSION
podman push --authfile $AUTHFILE quay.io/jstakun/camel-quarkus-mongodb-client:$VERSION
