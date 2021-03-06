#!/bin/bash
set -xev
source /etc/profile
#
echo "当前目录"
SHELL_FOLDER=$(cd "$(dirname "$0")";pwd)
echo ${SHELL_FOLDER}
cd ${SHELL_FOLDER}
#

#执行打包
cd ${SHELL_FOLDER}
timeout 600 mvn clean  install -DskipTests -Dgpg.skip -Dmaven.javadoc.skip=true

#代码分析
cd ${SHELL_FOLDER}
mvn sonar:sonar  || true

#(git add src pom.xml README.md  || true) && (git commit -m "commit" || true) && git push
