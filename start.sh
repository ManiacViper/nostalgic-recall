#!/bin/bash

sbt clean assembly
# Load environment variables
source ./env-setup.sh
# Path to your JAR file
JAR_PATH="target/scala-2.13/nostalgic-service.jar"

# Run the application
java -DHOST=${HOST} -DPORT=${PORT} -jar ${JAR_PATH}
