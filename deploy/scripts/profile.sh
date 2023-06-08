#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)

source "${ABSDIR}/base.sh"

PROFILE_URI="http://localhost:$PORT_1/$DEPLOY_ENV-profile"

function find_idle_profile()
{
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" $PROFILE_URI)

  if [ ${RESPONSE_CODE} -ge 400 ]
  then
    CURRENT_PROFILE="$DEPLOY_ENV-2"
  else
    CURRENT_PROFILE=$(curl -s $PROFILE_URI)
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