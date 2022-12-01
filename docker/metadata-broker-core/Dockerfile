FROM adoptopenjdk/openjdk11:alpine-slim

ADD metadata-broker-core-*.jar metadata-broker-core.jar
ADD run.sh run.sh

RUN sh -c 'touch /metadata-broker-core.jar'
RUN sh -c 'chmod +x /run.sh'



ENTRYPOINT ["/run.sh"]
