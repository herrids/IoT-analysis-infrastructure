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
        parts = msg.topic.split("/")
        if parts[0] == "sensor":
            sensor_type, sensor_number = parts[2].split("_")
            board_uuid = parts[3]

            create_sensor_table(sensor_type)

            query = """
                INSERT INTO sensor.{0} (
                    sensornumber, 
                    board_uuid, 
                    timestamp, 
                    sensorvalue)
                """.format(sensor_type)
            query = query + " VALUES (%s, %s, %s, %s)"
            db_session.execute(query, (
                sensor_number, 
                board_uuid, 
                datetime.utcnow(), 
                str(msg.payload)[2:-1]
                ))

    client.on_message = on_message

    client.connect("10.3.24.115", 1883, 60)
    client.loop_forever()

def connect_db():
    auth_provider = PlainTextAuthProvider(username='cassandra', password='cassandra')
    cluster = Cluster(['cassandra'], port=9042, auth_provider = auth_provider)
    session = cluster.connect(wait_for_all_pools=True)
    session.set_keyspace('myno')
    return session

def create_sensor_table(name, db_session):
    db_session.execute("""
    CREATE TABLE IF NOT EXISTS sensor.{0} (
        sensornumber text,
        board_uuid text,
        timestamp timestamp,
        sensorvalue double,
        PRIMARY KEY (sensornumber, uuid, timestamp)
        )
    """.format(name))

def init_db(db_session):
    try:
        db_session.execute("""
            CREATE KEYSPACE IF NOT EXISTS myno 
            WITH REPLICATION = 
            { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }
        """)

        db_session.set_keyspace('myno')

    except Exception as e:
        print(e)


if __name__ == "__main__":
    db_session = connect_db()
    init_db(db_session)
    receive(db_session)