# Multi-stage Containerfile for the Quarkus hybrid-demo app.
# Stage 1: build the Quarkus fast-jar with Maven.
FROM registry.access.redhat.com/ubi9/openjdk-17:1.20 AS build
USER root
WORKDIR /workspace
COPY --chown=185 pom.xml .
COPY --chown=185 src ./src
RUN mvn -B -DskipTests -Dquarkus.package.jar.type=fast-jar package

# Stage 2: minimal runtime.
FROM registry.access.redhat.com/ubi9/openjdk-17-runtime:1.20
ENV LANGUAGE='en_US:en'
COPY --from=build --chown=185 /workspace/target/quarkus-app/lib/      /deployments/lib/
COPY --from=build --chown=185 /workspace/target/quarkus-app/*.jar     /deployments/
COPY --from=build --chown=185 /workspace/target/quarkus-app/app/      /deployments/app/
COPY --from=build --chown=185 /workspace/target/quarkus-app/quarkus/  /deployments/quarkus/
USER 185
EXPOSE 8080
ENTRYPOINT ["java","-jar","/deployments/quarkus-run.jar"]
