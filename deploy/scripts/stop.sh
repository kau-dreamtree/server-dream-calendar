#!/usr/bin/env bash

source ./base.sh
source ./profile.sh

IDLE_PORT=$(find_idle_port)
IDLE_PID=$(lsof -ti tcp:"$IDLE_PORT")

if [ -z "$IDLE_PID" ]; then
  echo "$TIME_NOW > 현재 실행중인 애플리케이션이 없습니다." >> "$DEPLOY_LOG"
else
  echo "$TIME_NOW > 실행중인 $IDLE_PID 애플리케이션을 종료합니다." >> "$DEPLOY_LOG"
  kill -15 "$IDLE_PID"
fi