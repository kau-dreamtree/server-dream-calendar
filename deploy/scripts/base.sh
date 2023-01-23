#!/usr/bin/env bash

ABSPATH=$(readlink -f "$0")
ABSDIR=$(dirname "$ABSPATH")

TIME_NOW=$(date +%c)

PROJECT_ROOT="/home/ec2-user/dreamtree"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/application-error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"