#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)

source "${ABSDIR}/base.sh"
source "${ABSDIR}/profile.sh"
source "${ABSDIR}/switch.sh"

IDLE_PORT=$(find_idle_port)
PROFILE_URI="http://localhost:$IDLE_PORT/$DEPLOY_ENV-profile"

echo "$TIME_NOW $0 > Health Check ..." >> $DEPLOY_LOG
echo "$TIME_NOW $0 > IDLE_PORT: $IDLE_PORT" >> $DEPLOY_LOG
echo "$TIME_NOW $0 > curl -s $PROFILE_URI" >> $DEPLOY_LOG

sleep 3

for RETRY_COUNT in {1..5}
do

  RESPONSE=$(curl -s $PROFILE_URI )
  UP_COUNT=$(echo ${RESPONSE} | grep $DEPLOY_ENV | wc -l)  # 문자열 "test 또는 product"이 있는지 확인

  if [ ${UP_COUNT} -ge 1 ]
  then
    echo -e "$TIME_NOW $0 > Health Check 성공\n" >> $DEPLOY_LOG
    switch_proxy
    break
  else
    echo "$TIME_NOW $0 > 응답을 알 수 없거나 실행 상태가 아닙니다." >> $DEPLOY_LOG
    echo -e "$TIME_NOW $0 > RESPONSE: ${RESPONSE}\n" >> $DEPLOY_LOG
  fi

  if [ "${RETRY_COUNT}" -eq 5 ]
  then
    echo "$TIME_NOW $0 > Health Check 실패" >> $DEPLOY_LOG
    echo -e "$TIME_NOW $0 > Nginx에 연결하지 않고 배포를 종료합니다.\n" >> $DEPLOY_LOG
    exit 1
  fi

  echo "$TIME_NOW $0 > Health Check 실패. 재시도합니다..." >> $DEPLOY_LOG
  sleep 5

done