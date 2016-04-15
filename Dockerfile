FROM tomcat:7-jre7
FROM maven:3-jdk-7

COPY tomcat-users.xml $CATALINA_HOME/conf
COPY pom.xml /home/jalimay-nlp
COPY src/ /home/jalimay-nlp

WORKDIR /home/jalimay-nlp

RUN mvn package -Dmaven.test.skip=true

RUN cp target/jnlp.war $CATALINA_HOME/webapps

WORKDIR $CATALINA_HOME
EXPOSE 8080
CMD ["catalina.sh", "run"]