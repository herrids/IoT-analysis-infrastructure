import paho.mqtt.client as mqtt
from cassandra.cluster import Cluster
from cassandra.auth import PlainTextAuthProvider
from datetime import datetime

def receive(db_session):
    client = mqtt.Client()

    def on_connect(client, userdata, flags, rc):
        print("Connected with result code " + str(rc))
        client.subscribe("#")

    client.on_connect = on_connect

    def on_message(client, userdata, msg):
        query = "INSERT INTO sensor_data (sensorName, sensorValue, timestamp)"
        query = query + " VALUES (%s, %s, %s)"
        db_session.execute(query, (msg.topic, str(msg.payload)[2:-1], datetime.utcnow()))

    client.on_message = on_message

    client.connect("10.3.24.115", 1883, 60)
    client.loop_forever()

def connect_db():
    auth_provider = PlainTextAuthProvider(username='cassandra', password='cassandra')
    cluster = Cluster(['cassandra'], port=9042, auth_provider = auth_provider)
    session = cluster.connect(wait_for_all_pools=True)
    try:
        session.execute("""
            CREATE KEYSPACE IF NOT EXISTS myno 
            WITH REPLICATION = 
            { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }
        """)

        session.set_keyspace('myno')

        session.execute("""
            CREATE TABLE IF NOT EXISTS sensor_data (
                sensorName VARCHAR,
                sensorValue VARCHAR,
                timestamp TIMESTAMP,
                PRIMARY KEY (sensorName, timestamp)
            )
        """)
        return session

    except Exception as e:
        print(e)

if __name__ == "__main__":
    db = connect_db()
    receive(db)