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

# Prefer the Spring Boot executable jar, not the *-plain.jar
JAR_PATH=$(ls -t "$APP_DIR"/*.jar | grep -v -- "-plain\.jar$" | head -n 1)
if [ -z "$JAR_PATH" ]; then
  echo "No executable jar found in $APP_DIR" > "$LOG_FILE"
  exit 1
fi

nohup java -jar "$JAR_PATH" --spring.profiles.active="$PROFILE" > "$LOG_FILE" 2>&1 &
echo $! > "$PID_FILE"
