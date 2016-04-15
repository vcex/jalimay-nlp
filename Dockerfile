FROM tomcat:7-jre7
WORKDIR $CATALINA_HOME
EXPOSE 8080
CMD ["catalina.sh", "run"]