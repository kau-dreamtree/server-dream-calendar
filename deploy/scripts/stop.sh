#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)

source "${ABSDIR}/base.sh"
source "${ABSDIR}/profile.sh"

IDLE_PORT=$(find_idle_port)
IDLE_PID=$(lsof -ti tcp:$IDLE_PORT)

if [ -z $IDLE_PID ]; then
  echo -e "$TIME_NOW $0 > 현재 실행중인 애플리케이션이 없습니다.\n" >> $DEPLOY_LOG
else
  echo -e "$TIME_NOW $0 > 실행중인 $IDLE_PID 애플리케이션을 종료합니다.\n" >> $DEPLOY_LOG
  kill -15 $IDLE_PID
fi