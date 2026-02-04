#!/bin/bash
set -euo pipefail
PID_FILE="/home/ubuntu/app/app.pid"
if [ -f "$PID_FILE" ]; then
  PID=$(cat "$PID_FILE")
  if ps -p "$PID" > /dev/null 2>&1; then
    kill -15 "$PID"
    sleep 5
  fi
  rm -f "$PID_FILE"
fi
