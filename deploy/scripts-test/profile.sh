#!/usr/bin/env bash

ABSPATH=$(readlink -f "$0")
ABSDIR=$(dirname "$ABSPATH")

source "${ABSDIR}"/values.sh

function find_idle_profile()
{
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/test-profile)

  if [ "${RESPONSE_CODE}" -ge 400 ]
  then
    CURRENT_PROFILE=test-2
  else
    CURRENT_PROFILE=$(curl -s http://localhost/test-profile)
  fi

  if [ "${CURRENT_PROFILE}" == test-1 ]
  then
    IDLE_PROFILE=test-2
  else
    IDLE_PROFILE=test-1
  fi

  echo "${IDLE_PROFILE}"
}

function find_idle_port()
{
  IDLE_PROFILE=$(find_idle_profile)

  if [ "${IDLE_PROFILE}" == test-1 ]
  then
    echo $TEST_PORT_1
  else
    echo $TEST_PORT_2
  fi
}