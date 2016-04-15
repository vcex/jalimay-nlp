FROM tomcat:7-jre7
FROM maven:3-jdk-7

RUN cp tomcat-users.xml $CATALINA_HOME/conf

RUN mvn package -Dmaven.test.skip=true

RUN cp target/jnlp.war $CATALINA_HOME/webapps

WORKDIR $CATALINA_HOME
EXPOSE 8080
CMD ["catalina.sh", "run"]