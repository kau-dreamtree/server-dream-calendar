#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)

source "${ABSDIR}/base.sh"
source "${ABSDIR}/profile.sh"

function switch_proxy() {

  IDLE_PORT=$(find_idle_port)

  echo "$TIME_NOW $0 > $IDLE_PORT 포트로 전환합니다." >> "$DEPLOY_LOG"
  echo "set \$service_url http://127.0.0.1:$IDLE_PORT;" | sudo tee "/etc/nginx/conf.d/$DEPLOY_ENV-url.inc"

  echo "$TIME_NOW $0 > Nginx Reload" >> "$DEPLOY_LOG"
  sudo service nginx reload

}