import paho.mqtt.client as mqtt
import click

@click.command()
@click.option('--password', default=None, help='MQTT password')
@click.option('--username', default=None, help='MQTT username')
@click.option('--hostname', default="10.3.24.115", help='MQTT server hostname')
@click.option('--port', default=1883, help='MQTT server port')
def receive(username, password, hostname, port):
    client = mqtt.Client()

    def on_connect(client, userdata, flags, rc):
        print("Connected with result code " + str(rc))
        client.subscribe("#")

    client.on_connect = on_connect

    def on_message(client, userdata, msg):
        print(msg)

    client.on_message = on_message

    if password is not None and username is not None:
        client.username_pw_set(username=username, password=password)
    
    client.connect(hostname, port, 60)
    client.loop_forever()

if __name__ == "__main__":
    receive()