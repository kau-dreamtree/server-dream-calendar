#!/usr/bin/env bash

source ./base.sh
source ./profile.sh
source ./switch.sh

IDLE_PORT=$(find_idle_port)
PROFILE_URI="http://localhost:$IDLE_PORT/profile"

echo "$TIME_NOW > Health Check ..." >> "$DEPLOY_LOG"
echo "$TIME_NOW > IDLE_PORT: $IDLE_PORT" >> "$DEPLOY_LOG"
echo "$TIME_NOW > curl -s $PROFILE_URI" >> "$DEPLOY_LOG"
sleep 10

for RETRY_COUNT in {1..5}
do

  RESPONSE=$(curl -s $PROFILE_URI )
  UP_COUNT=$(echo ${RESPONSE} | grep 'real' | wc -l)

  if [ "${UP_COUNT}" -ge 1 ]
  then # 문자열 "real"이 있는지 확인
    echo "$TIME_NOW > Health Check 성공" >> "$DEPLOY_LOG"
    switch_proxy
    break
  else
    echo "$TIME_NOW > 응답을 알 수 없거나 실행 상태가 아닙니다." >> "$DEPLOY_LOG"
    echo "$TIME_NOW > RESPONSE: ${RESPONSE}" >> "$DEPLOY_LOG"
  fi

  if [ "${RETRY_COUNT}" -eq 10 ]
  then
    echo "$TIME_NOW > Health Check 실패" >> "$DEPLOY_LOG"
    echo "$TIME_NOW > Nginx에 연결하지 않고 배포를 종료합니다." >> "$DEPLOY_LOG"
    exit 1
  fi

  echo "$TIME_NOW > Health Check 실패. 재시도합니다..." >> "$DEPLOY_LOG"
  sleep 10

done