#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)

source "${ABSDIR}/base.sh"
source "${ABSDIR}/profile.sh"

JAR_DIR="$PROJECT_ROOT/jar"
IDLE_PROFILE=$(find_idle_profile)

echo "$TIME_NOW $0 > Copy JAR file to $JAR_DIR" >> $DEPLOY_LOG
cp "$PROJECT_ROOT/*.jar" $JAR_DIR

JAR_NAME=$(ls -tr $JAR_DIR/*.jar | tail -n 1)
chmod +x $JAR_NAME

echo "$TIME_NOW $0 > $JAR_NAME $IDLE_PROFILE 실행" >> $DEPLOY_LOG

nohup java -jar \
  -Dspring.config.location="$PROJECT_ROOT/application-$IDLE_PROFILE".yml,"$PROJECT_ROOT/application-key.yml","$PROJECT_ROOT/application-test-db.yml","$PROJECT_ROOT/application-email.yml" \
  -Dspring.profiles.active=$IDLE_PROFILE \
  $JAR_NAME > $DEBUG_LOG 2> $ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_NAME)
echo "$TIME_NOW $0 > 실행된 PID $CURRENT_PID\n" >> $DEPLOY_LOG
