#!/bin/bash
set -euo pipefail
sleep 5
curl -f http://localhost:8080/actuator/health
