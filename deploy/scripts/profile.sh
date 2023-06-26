#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)

source "${ABSDIR}/base.sh"

PROFILE_URI="https://${DOMAIN}.dreamtree.shop/${DEPLOY_ENV}-profile"
CURRENT_PROFILE=$(curl -s $PROFILE_URI)
RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" $PROFILE_URI)

function find_idle_profile()
{
  if [ "$CURRENT_PROFILE" == "${DEPLOY_ENV}-1" ]
  then
    IDLE_PROFILE="$DEPLOY_ENV-2"
  else if [ "$CURRENT_PROFILE" == "${DEPLOY_ENV}-2" ]
    IDLE_PROFILE="$DEPLOY_ENV-1"
  fi

  echo ${IDLE_PROFILE}
}

function find_idle_port()
{
  IDLE_PROFILE=$(find_idle_profile)

  if [ $IDLE_PROFILE == "${DEPLOY_ENV}-1" ]
  then
    echo $PORT_1
  else
    echo $PORT_2
  fi
}