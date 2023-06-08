#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)

source "${ABSDIR}/base.sh"

function find_idle_profile()
{
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" "http://localhost/$DEPLOY_ENV-profile")

  if [ ${RESPONSE_CODE} -ge 400 ]
  then
    CURRENT_PROFILE="$DEPLOY_ENV-2"
  else
    CURRENT_PROFILE=$(curl -s "http://localhost/$DEPLOY_ENV-profile")
  fi

  if [ ${CURRENT_PROFILE} == "$DEPLOY_ENV-1" ]
  then
    IDLE_PROFILE="$DEPLOY_ENV-2"
  else
    IDLE_PROFILE="$DEPLOY_ENV-1"
  fi

  echo ${IDLE_PROFILE}
}

function find_idle_port()
{
  IDLE_PROFILE=$(find_idle_profile)

  if [ ${IDLE_PROFILE} == "$DEPLOY_ENV-1" ]
  then
    echo $PORT_1
  else
    echo $PORT_2
  fi
}