#!/usr/bin/env bash

ABSPATH=$(readlink -f "$0")
ABSDIR=$(dirname "$ABSPATH")

TIME_NOW=$(date +%c)

DEPLOY_ENV="product"

PORT_1="9091"
PORT_2="9092"

PROJECT_ROOT="/home/ec2-user/dreamcalendar-product"
LOG_ROOT="$PROJECT_ROOT/log"

DEBUG_LOG="$LOG_ROOT/debug.log"
ERROR_LOG="$LOG_ROOT/error.log"
DEPLOY_LOG="$LOG_ROOT/deploy.log"