# base image
FROM tomcat
# copy *.war
COPY target/*.war /usr/local/tomcat/webapps/
# export port
EXPOSE 8080
# exec command
CMD ["catalina.sh","run"]