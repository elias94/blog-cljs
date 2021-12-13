FROM openjdk:8-alpine

COPY target/uberjar/blog.jar /blog/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/blog/app.jar"]
