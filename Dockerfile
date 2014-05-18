# apache-storm-0.9.1-incubating
#
# VERSION 	1.0

# use the ubuntu base image provided by dotCloud
FROM ubuntu
MAINTAINER Florian HUSSONNOIS, florian.hussonnois_gmail.com

RUN apt-get update
RUN apt-get upgrade -y

# Install Oracle JDK 7
RUN apt-get install -y python-software-properties
RUN apt-get install -y software-properties-common
RUN add-apt-repository ppa:webupd8team/java -y

RUN apt-get update
# accept the Oracle license before the installation
RUN echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
RUN apt-get install -y oracle-java7-installer
RUN apt-get update

# Install supervisor and others useful packages 
RUN apt-get install -y supervisor git vim wget tar

# Create storm group and user
ENV STORM_HOME /usr/share/apache-storm-0.9.1-incubating

RUN groupadd storm; useradd --gid storm --home-dir /home/storm --create-home --shell /bin/bash storm

# Install apache storm
RUN wget http://mir2.ovh.net/ftp.apache.org/dist/incubator/storm/apache-storm-0.9.1-incubating/apache-storm-0.9.1-incubating.tar.gz 
RUN tar -xzvf apache-storm-0.9.1-incubating.tar.gz -C /usr/share
RUN rm -rf apache-storm-0.9.1-incubating.tar.gz
RUN chown -R storm:storm $STORM_HOME

RUN mkdir /var/log/storm ; chown -R storm:storm /var/log/storm ; ln -s /var/log/storm /home/storm/log
RUN ln -s $STORM_HOME/bin/storm /usr/bin/storm
ADD share/cluster.xml $STORM_HOME/logback/cluster.xml
ADD share/storm.yaml $STORM_HOME/conf/storm.yaml

# Add scripts required to run storm daemons under supervision
ADD share/startup.sh /home/storm/startup.sh
ADD share/storm-supervisor.conf /home/storm/storm-supervisor.conf

RUN chmod u+x /home/storm/startup.sh

# Tells Supervisor to run interactively rather than daemonize
RUN echo [supervisord] | tee -a /etc/supervisor/supervisord.conf ; echo nodaemon=true | tee -a /etc/supervisor/supervisord.conf

