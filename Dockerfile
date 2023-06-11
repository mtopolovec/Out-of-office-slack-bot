FROM openjdk:latest
ADD out/artifacts/Out_of_office_bot_jar/Out_of_office_bot.jar Out_of_office_bot.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","Out_of_office_bot.jar"]