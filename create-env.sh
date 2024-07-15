#!/bin/bash

CONFIG_FILE=".env"
DATA_SERVER_DIR="data-server"
WEB_SCRAPPER_DIR="web-scrapper"

overwrite=false

usage() {
  echo "Usage: $0 [-d] key=value [key=value ...]"
  echo "  -d    Overwrite existing config file if it exists"
  exit 1
}

while getopts "d" opt; do
  case ${opt} in
    d )
      overwrite=true
      ;;
    \? )
      usage
      ;;
  esac
done
shift $((OPTIND -1))

if [ $# -lt 1 ]; then
  usage
fi

declare -A config
for arg in "$@"; do
  if [[ "$arg" == *=* ]]; then
    key=${arg%%=*}
    value=${arg#*=}
    config[$key]=$value
  else
    echo "Error: Invalid argument format '$arg'. Expected key=value."
    usage
  fi
done

create_config() {
  local dir=$1
  local file_path="$dir/$CONFIG_FILE"
  if [ -f "$file_path" ] && [ "$overwrite" = false ]; then
    echo "Error: $file_path already exists. Use -d to overwrite."
    exit 1
  fi
  mkdir -p "$dir"
  > "$file_path"
  for key in "${!config[@]}"; do
    echo "$key=${config[$key]}" >> "$file_path"
  done
  echo "Created $file_path"
}

create_config $DATA_SERVER_DIR
create_config $WEB_SCRAPPER_DIR