#!/bin/bash

# Configuration
SERVER_JAR="../build/libs/server-0.0.1-SNAPSHOT.jar"
CLIENT_JAR="../build/libs/client-0.0.1-SNAPSHOT.jar"
NODES_FILE="../src/main/resources/nodes.json"
LOG_DIR="logs_$(date +%Y%m%d_%H%M%S)"
mkdir -p $LOG_DIR

# Variables to store server info
PORT1=""
PORT2=""
SERVER1_PID=""
SERVER2_PID=""

# Helper function to find a free port
find_free_port() {
    local port=9090
    while lsof -Pi :$port -sTCP:LISTEN -t >/dev/null ; do
        port=$((port + 1))
    done
    echo $port
}

# Start servers
start_servers() {
    echo "Starting server instances..."
    PORT1=$(find_free_port)
    java -jar $SERVER_JAR Server1 $PORT1 $NODES_FILE > "$LOG_DIR/server1.log" 2>&1 &
    SERVER1_PID=$!
    echo "Server1 started on port $PORT1 (PID $SERVER1_PID)"

    PORT2=$(find_free_port)
    java -jar $SERVER_JAR Server2 $PORT2 $NODES_FILE > "$LOG_DIR/server2.log" 2>&1 &
    SERVER2_PID=$!
    echo "Server2 started on port $PORT2 (PID $SERVER2_PID)"

    sleep 5 # Wait for servers to initialize
}

# Stop servers
stop_servers() {
    echo "Stopping servers..."
    if [ -n "$SERVER1_PID" ]; then
        kill $SERVER1_PID
    fi
    if [ -n "$SERVER2_PID" ]; then
        kill $SERVER2_PID
    fi
}

# Test Cases
run_test_valid() {
    echo "Running Test Case: Valid message"
    java -jar $CLIENT_JAR localhost $PORT1 > "$LOG_DIR/client_valid.log" 2>&1
    if grep -q "Acknowledged" "$LOG_DIR/client_valid.log"; then
        echo "Test Case 1: Passed"
    else
        echo "Test Case 1: Failed"
    fi
}

run_test_malformed() {
    echo "Running Test Case: Malformed message"
    java -jar $CLIENT_JAR localhost $PORT1 malformed > "$LOG_DIR/client_malformed.log" 2>&1
    if grep -q "ERROR: Malformed message" "$LOG_DIR/client_malformed.log"; then
        echo "Test Case 2: Passed"
    else
        echo "Test Case 2: Failed"
    fi
}

run_test_error() {
    echo "Running Test Case: Simulated error injection"
    java -jar $CLIENT_JAR localhost $PORT1 error > "$LOG_DIR/client_error.log" 2>&1
    if grep -q "ERROR: Simulated server error response" "$LOG_DIR/client_error.log"; then
        echo "Test Case 3: Passed"
    else
        echo "Test Case 3: Failed"
    fi
}

run_test_parallel() {
    echo "Running Test Case: Parallel clients"
    java -jar $CLIENT_JAR localhost $PORT1 > "$LOG_DIR/client_parallel_1.log" 2>&1 &
    java -jar $CLIENT_JAR localhost $PORT1 > "$LOG_DIR/client_parallel_2.log" 2>&1 &
    wait
    echo "Parallel test completed. Check logs for details."
}

# Main execution flow
start_servers

case $1 in
    valid) run_test_valid ;;
    malformed) run_test_malformed ;;
    error) run_test_error ;;
    parallel) run_test_parallel ;;
    all)
        run_test_valid
        run_test_malformed
        run_test_error
        run_test_parallel
        ;;
    *)
        echo "Usage: $0 {valid|malformed|error|parallel|all}"
        ;;
esac

stop_servers

# Logs
echo "Test results saved in $LOG_DIR:"
ls $LOG_DIR
