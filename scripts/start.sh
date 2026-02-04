#!/bin/bash
set -euo pipefail
APP_DIR="/home/ubuntu/app"
LOG_FILE="$APP_DIR/app.log"
PID_FILE="$APP_DIR/app.pid"
PROFILE_FILE="$APP_DIR/profile"
PROFILE="prod"
if [ -f "$PROFILE_FILE" ]; then
  PROFILE=$(cat "$PROFILE_FILE")
fi
JAR_PATH=$(ls "$APP_DIR"/*.jar | head -n 1)
nohup java -jar "$JAR_PATH" --spring.profiles.active="$PROFILE" > "$LOG_FILE" 2>&1 &
echo $! > "$PID_FILE"
