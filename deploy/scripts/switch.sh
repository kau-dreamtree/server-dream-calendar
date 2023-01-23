#!/usr/bin/env bash

source ./base.sh
source ./profile.sh

function switch_proxy() {

  IDLE_PORT=$(find_idle_port)

  echo "$TIME_NOW > $IDLE_PORT 포트로 전환합니다." >> "$DEPLOY_LOG"
  echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

  echo "$TIME_NOW > Nginx Reload" >> "$DEPLOY_LOG"
  sudo service nginx reload

}