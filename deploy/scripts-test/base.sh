#!/usr/bin/env bash

ABSPATH=$(readlink -f "$0")
ABSDIR=$(dirname "$ABSPATH")

TIME_NOW=$(date +%c)

TEST_PORT_1="8081"
TEST_PORT_2="8082"

PROJECT_ROOT="/home/ec2-user/dreamcalendar-test"
LOG_ROOT="$PROJECT_ROOT/log"

APP_LOG="$LOG_ROOT/debug.log"
ERROR_LOG="$LOG_ROOT/error.log"
DEPLOY_LOG="$LOG_ROOT/deploy.log"