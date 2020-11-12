FROM adoptopenjdk/openjdk11:latest
ENV KEYCLOAK_ADMIN_PASSWORD null
ENV KEYCLOAK_CLIENT null
ENV KEYCLOAK_REALM null
ENV KEYCLOAK_ADMIN null
ENV DB_URL null
ENV DB_USER null
ENV DB_PASSWORD null
ENV KEYCLOAK_URL null
ENV KEYCLOAK_SECRET null

ARG JAR_FILE=build/libs/alert-covid-service-user-*.jar


WORKDIR /opt/app

COPY ${JAR_FILE} app.jar
EXPOSE 8081
ENTRYPOINT java -DKEYCLOAK_ADMIN_PASSWORD=$KEYCLOAK_ADMIN_PASSWORD -DKEYCLOAK_CLIENT=$KEYCLOAK_CLIENT -DKEYCLOAK_REALM=$KEYCLOAK_REALM -DKEYCLOAK_ADMIN=$KEYCLOAK_ADMIN -DDB_URL=$DB_URL -DDB_USER=$DB_USER -DDB_PASSWORD=$DB_PASSWORD -DKEYCLOAK_URL=$KEYCLOAK_URL -DKEYCLOAK_SECRET=$KEYCLOAK_SECRET -jar app.jar
