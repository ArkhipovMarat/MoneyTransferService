FROM openjdk:15
ADD build/libs/money_transfer_service-0.0.1-SNAPSHOT.jar mts.jar
EXPOSE 5500
ENTRYPOINT ["java","-jar","/mts.jar"]