FROM quay.io/ukhomeofficedigital/openjdk10

RUN yum install -y curl && \
    yum install -y openssl && \
    yum clean all && \
    rpm --rebuilddb

ADD public-0.0.1-SNAPSHOT.jar /app/public.jar
ADD scripts /app/
ADD root-certs/ /app/

ENV JAR_FILE=/app/public.jar \
    ARGS=-Dhttp.port=8080

RUN chmod a+x /app/*.sh
RUN /bin/bash -c /app/import-cacerts.sh && echo "certificate import done"

EXPOSE 8080