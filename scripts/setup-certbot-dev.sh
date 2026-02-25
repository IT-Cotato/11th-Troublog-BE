#!/bin/bash
set -e

DOMAIN="troublog.cloud"
EMAIL="zksdbstjq@gmail.com"

# Certbot 설치
if ! command -v certbot &> /dev/null; then
    sudo apt-get update
    sudo apt-get install -y certbot
fi

mkdir -p /home/ubuntu/troublog/certbot/www

cd /home/ubuntu/troublog
sudo docker-compose stop nginx

# 인증서 발급
sudo certbot certonly --standalone \
  --preferred-challenges http \
  --email ${EMAIL} \
  --agree-tos \
  --no-eff-email \
  --non-interactive \
  -d ${DOMAIN} \
  -d www.${DOMAIN}

sudo docker-compose up -d

echo "인증서 발급 완료: /etc/letsencrypt/live/${DOMAIN}/"
