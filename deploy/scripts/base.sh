#!/usr/bin/env bash

TIME_NOW=$(date +%c)

PROJECT_ROOT="/home/ec2-user/dreamtree"

LOG_ROOT="$PROJECT_ROOT/log"

APP_LOG="$LOG_ROOT/debug.log"
ERROR_LOG="$LOG_ROOT/application-error.log"
DEPLOY_LOG="$LOG_ROOT/deploy.log"