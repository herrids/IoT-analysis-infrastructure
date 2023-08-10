import json
import paho.mqtt.client as mqtt
from datetime import datetime
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

    if parts[0] in ("sensor", "state"): # no actuator because its the manual pump
        sensor_type, sensor_number = parts[2].split("_")
        board_uuid = parts[3]

        now_utc = datetime.utcnow()
        timestamp = int(time.mktime(now_utc.timetuple())) * 1000

        parse_to_integer = lambda s: int(s) if s.isdigit() else None

        data = {
            "sensorType": sensor_type,
            "sensorNumber": parse_to_integer(sensor_number),
            "boardUuid": board_uuid,
            "timestamp": timestamp,
            "value": str(msg.payload)[2:-1]
        }
        
        # Publish the MQTT message to the Kafka topic and ensure the message is sent immediately
        kafka_producer.send("sensor-data-topic", value=data)
        kafka_producer.flush()

# This function is responsible for receiving MQTT messages
def receive():
    client = mqtt.Client()

    client.on_connect = on_connect
    client.on_message = on_message

    client.connect("10.3.24.115", 1883, 60)
    
    client.loop_forever()

if __name__ == "__main__":
    receive()
