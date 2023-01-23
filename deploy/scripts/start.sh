#!/usr/bin/env bash

ABSPATH=$(readlink -f "$0")
ABSDIR=$(dirname "$ABSPATH")

source "${ABSDIR}"/base.sh
source "${ABSDIR}"/profile.sh

JAR_DIR="$PROJECT_ROOT/jar"

echo "$TIME_NOW > Copy JAR file to $JAR_DIR" >> "$DEPLOY_LOG"
cp "$PROJECT_ROOT"/*.jar "$JAR_DIR"/

JAR_NAME=$(ls -tr "$JAR_DIR"/*.jar | tail -n 1)
chmod +x "$JAR_NAME"

echo "$TIME_NOW > $JAR_NAME 실행" >> "$DEPLOY_LOG"
nohup java -jar \
  -Dspring.config.location=classpath:/application.yml,"$PROJECT_ROOT"/application-key.yml \
  -Dspring.profiles.active="$IDLE_PROFILE" \
  "$JAR_NAME" > "$APP_LOG" 2> "$ERROR_LOG" &

CURRENT_PID=$(pgrep -f JAR_NAME)
echo "$TIME_NOW > 실행된 PID $CURRENT_PID" >> "$DEPLOY_LOG"
