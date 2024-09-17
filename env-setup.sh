#!/bin/bash

export SPOTIFY_CLIENT_ID="your_spotify_client_id_here"
export SPOTIFY_CLIENT_SECRET="your_spotify_client_secret_here"

export HOST="0.0.0.0"
export PORT="8888"

# Print the environment variables to verify
echo "SPOTIFY_CLIENT_ID=${SPOTIFY_CLIENT_ID}"
echo "SPOTIFY_CLIENT_SECRET=${SPOTIFY_CLIENT_SECRET}"
echo "HOST=${HOST}"
echo "PORT=${PORT}"
