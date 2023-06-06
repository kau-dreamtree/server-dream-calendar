#!/usr/bin/env bash

ABSPATH=$(readlink -f "$0")
ABSDIR=$(dirname "$ABSPATH")

source "${ABSDIR}/values.sh"

TIME_NOW=$(date +%c)

LOG_ROOT="$TEST_ROOT/log"

APP_LOG="$LOG_ROOT/debug.log"
ERROR_LOG="$LOG_ROOT/error.log"
DEPLOY_LOG="$LOG_ROOT/deploy.log"