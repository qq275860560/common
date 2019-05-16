FROM qq275860560/jenkins-slave

MAINTAINER jiangyuanlin@163.com

# deploy
RUN cd /tmp &&\
    source /etc/profile &&\    
    git clone https://github.com/qq275860560/common &&\
    cd common &&\
    echo "<settings><servers><server><id>sonatype-nexus-staging</id><username>qq275860560</username><password>!A2a3a4a5a6a</password></server> </servers></settings>" > /usr/local/maven/conf/settings.xml &&\
    mvn deploy -DskipTests -Dgpg.passphrase="123456"
    
    


    
