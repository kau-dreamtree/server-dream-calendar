#!/usr/bin/env bash

PROJECT_ROOT="/home/ec2-user/dreamtree"
JAR_FILE="$PROJECT_ROOT/dream-calendar-server.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/application-error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

echo "$TIME_NOW > $JAR_FILE 복사" >> $DEPLOY_LOG
cp $PROJECT_ROOT/build/libs/*.jar $JAR_FILE

echo "$TIME_NOW > $JAR_FILE 실행" >> $DEPLOY_LOG
nohup java -jar $JAR_FILE \
  -Dspring.config.location=classpath:/application.yml,$PROJECT_ROOT/application-key.yml \
  > $APP_LOG 2> $ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 PID $CURRENT_PID" >> $DEPLOY_LOG

