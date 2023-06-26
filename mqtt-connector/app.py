# Import required libraries
import json
import paho.mqtt.client as mqtt # MQTT client library
from datetime import datetime # Datetime library to handle timestamp
import time
from kafka import KafkaProducer

kafka_producer = KafkaProducer(bootstrap_servers='kafka:9092', 
                               value_serializer=lambda v: json.dumps(v).encode('utf-8'))

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
        timestamp = int(time.mktime(now_utc.timetuple())) * 1000


        data = {
            "sensorType": sensor_type,
            "sensorNumber": sensor_number,
            "boardUuid": board_uuid,
            "timestamp": timestamp,
            "value": str(msg.payload)[2:-1]
        }

        print(data)
        
        # Publish the MQTT message to the Kafka topic
        kafka_producer.send("sensor-data-topic", value=data)
        kafka_producer.flush()  # Ensure the message is sent immediately

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
