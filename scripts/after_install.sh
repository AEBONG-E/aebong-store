#!/bin/bash
set -euo pipefail
APP_DIR="/home/ubuntu/app"
if [ -d "$APP_DIR/scripts" ]; then
  chmod +x "$APP_DIR"/scripts/*.sh || true
fi
