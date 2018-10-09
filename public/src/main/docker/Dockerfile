FROM quay.io/ukhomeofficedigital/openjdk10

RUN yum install -y curl && \
    yum install -y openssl && \
    yum clean all && \
    rpm --rebuilddb

ADD public-0.0.1-SNAPSHOT.jar /app/public.jar
ADD scripts /app/
ADD root-certs/ /app/

ENV USER dpadmin
ENV GROUP dpadmin
ENV JAR_FILE=/app/public.jar \
    ARGS=-Dhttp.port=8080

RUN groupadd -r ${GROUP} && \
    useradd -r -u 1000 -g ${GROUP} ${USER}

RUN chown ${USER}:${GROUP} /app/public.jar
RUN chown ${USER}:${GROUP} /app/*.sh

RUN chmod a+x /app/*.sh
RUN chmod a+x /app/public.jar
RUN /bin/bash -c /app/import-cacerts.sh && echo "certificate import done"

USER 1000

EXPOSE 8080