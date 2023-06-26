#!/bin/bash
set -e

# Wait for Cassandra to start
echo "Waiting for Cassandra to start..."
sleep 30

# Initialize schema
echo "Initializing schema..."
cqlsh -e "
CREATE KEYSPACE IF NOT EXISTS myno WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
USE myno;
CREATE TABLE IF NOT EXISTS sensor_data (sensor_id text, value double, PRIMARY KEY (sensor_id));
"
echo "Schema initialized."

# Run the main command
exec "$@"
