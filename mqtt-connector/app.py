# Import required libraries
import paho.mqtt.client as mqtt # MQTT client library
from datetime import datetime # Datetime library to handle timestamp
from db import connect_db, create_sensor_table # Custom functions to initialize and connect to database

# Connect to the database
db_session = connect_db()

# This function is called when the client connects to the broker
def on_connect(client, userdata, flags, rc):
    print("Connected with result code " + str(rc))
    # Subscribe to all topics
    client.subscribe("#")

# This function is called when a message is received from the broker
def on_message(client, userdata, msg):
    # Split the topic into its individual parts
    parts = msg.topic.split("/")
    # Check if the topic is related to a sensor
    if parts[0] in ("sensor", "actuator"):
        # Extract the sensor type and number, and board UUID from the topic
        sensor_type, sensor_number = parts[2].split("_")
        board_uuid = parts[3]

        # format the datetime object as a string and remove trailing zeros
        now_utc = datetime.utcnow()
        now_utc_str = now_utc.strftime('%Y-%m-%d %H:%M:%S.%f')
        now_utc_str = now_utc_str[:-3]

        # Create a table for the sensor if it does not exist already
        create_sensor_table(sensor_type, db_session)

        # Insert the sensor data into the database
        query = f"""
            INSERT INTO sensor_{sensor_type} (
                sensornumber, 
                board_uuid, 
                timestamp, 
                sensorvalue)
            VALUES (%s, %s, %s, %s)
            """
        db_session.execute(query, (
            sensor_number, 
            board_uuid, 
            now_utc_str, 
            str(msg.payload)[2:-1]
            ))

# This function is responsible for receiving MQTT messages
def receive():
    # Create a new MQTT client
    client = mqtt.Client()

    # Set the on_connect and on_message callbacks
    client.on_connect = on_connect
    client.on_message = on_message

    # Connect to the MQTT broker
    client.connect("10.3.24.115", 1883, 60)
    
    # Start the MQTT client loop
    client.loop_forever()

if __name__ == "__main__":
    # Start receiving MQTT messages
    receive()
