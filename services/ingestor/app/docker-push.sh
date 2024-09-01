#/bin/sh
DOCKER_REGISTRY=${1-lasrosas05.lasrosas:5000}
docker push $DOCKER_REGISTRY/lasrosasiot/ingestor:1.0.0-SNAPSHOT
