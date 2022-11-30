VERSION=latest
AUTHFILE=~/camelquarkusmongoclient.auth
#podman login quay.io
podman build --squash -f Containerfile.quarkus . -t quay.io/jstakun/camel-quarkus-mongodb-client:$VERSION
podman push --authfile $AUTHFILE quay.io/jstakun/camel-quarkus-mongodb-client:$VERSION
