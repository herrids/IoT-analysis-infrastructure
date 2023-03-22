# Import required libraries
from cassandra.cluster import Cluster # Cassandra cluster library
from cassandra.auth import PlainTextAuthProvider # Authentication library

# Connect to the Cassandra database
def connect_db():
    # Set up authentication for the Cassandra cluster
    auth_provider = PlainTextAuthProvider(username='cassandra', password='cassandra')

    # Connect to the Cassandra cluster
    cluster = Cluster(['cassandra'], port=9042, auth_provider=auth_provider)
    session = cluster.connect(wait_for_all_pools=True)

    # Set the keyspace for the Cassandra session
    session.set_keyspace('myno')

    # Return the Cassandra session
    return session

# Initialize the Cassandra database
def init_db(db_session):
    try:
        # Create the keyspace if it does not exist
        db_session.execute("""
            CREATE KEYSPACE IF NOT EXISTS myno 
            WITH REPLICATION = 
            { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }
        """)

        # Set the keyspace for the Cassandra session
        db_session.set_keyspace('myno')

    except Exception as e:
        print(e)

# Create a table in the Cassandra database for a sensor
def create_sensor_table(name, db_session):
    db_session.execute(f"""
        CREATE TABLE IF NOT EXISTS sensor.{name} (
            sensornumber text,
            board_uuid text,
            timestamp timestamp,
            sensorvalue double,
            PRIMARY KEY (sensornumber, board_uuid, timestamp)
        )
    """)
