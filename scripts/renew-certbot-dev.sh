#!/bin/bash
set -e

LOG_FILE="/var/log/certbot-renew.log"

echo "[$(date)] Certbot 갱신 시작" >> ${LOG_FILE}

sudo certbot renew --quiet >> ${LOG_FILE} 2>&1

cd /home/ubuntu/troublog
sudo docker-compose restart nginx >> ${LOG_FILE} 2>&1

echo "[$(date)] 갱신 완료" >> ${LOG_FILE}
