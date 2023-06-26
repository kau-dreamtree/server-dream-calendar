#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)

source "${ABSDIR}/base.sh"

PROFILE_URI="http://localhost:$PORT_1/$DEPLOY_ENV-profile"
RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" $PROFILE_URI)

function find_idle_profile()
{
  if [ ${RESPONSE_CODE} -ge 400 ]
  then
    CURRENT_PROFILE="$DEPLOY_ENV-2"
    IDLE_PROFILE="$DEPLOY_ENV-1"
  else
    CURRENT_PROFILE="$DEPLOY_ENV-1"
    IDLE_PROFILE="$DEPLOY_ENV-2"
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