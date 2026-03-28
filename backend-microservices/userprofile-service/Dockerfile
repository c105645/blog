# Stage 1: Extract layers from the layered JAR
FROM eclipse-temurin:17-jre AS builder
WORKDIR /app
COPY build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Stage 2: Final image with layers in correct cache order
FROM eclipse-temurin:17-jre
WORKDIR /app

# Layer 1: dependencies (changes least often — cached aggressively)
COPY --from=builder /app/dependencies/ ./

# Layer 2: spring-boot-loader (rarely changes)
COPY --from=builder /app/spring-boot-loader/ ./

# Layer 3: snapshot-dependencies (changes occasionally)
COPY --from=builder /app/snapshot-dependencies/ ./

# Layer 4: application (changes most often — always last)
COPY --from=builder /app/application/ ./

EXPOSE 9010
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
