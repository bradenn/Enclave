#!/bin/sh

if ls server &>/dev/null; then
  if screen -list | grep -q "serverThread"; then
    echo "[INFO] Found server, attempting to reload plugin.";
    screen -S serverThread -p 0 -X stuff "plugman reload Enclave$(echo '\015')"
    screen -S serverThread -p 0 -X stuff "say §7Enclaves Reloaded §7@ §7$(date)$(echo '\015')";
    echo "[INFO] Enclave plugin reloaded.";
  else
    echo "[INFO] Server is not running, attempting to start the server.";
    exec ./scripts/start.sh
  fi
else
  echo "[INFO] Server directory could not be found, ignoring.";
  exit 0
fi

