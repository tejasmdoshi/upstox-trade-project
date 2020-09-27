FROM tomcat:8.0-alpine

LABEL maintainer="tejasmdoshi@outlook.com"

ADD target/tradetest.war /usr/local/tomcat/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]