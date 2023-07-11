# Project: IoT Data Streaming Pipeline
This project is a Docker Compose setup that creates a real-time data streaming pipeline with multiple services including Grafana, Zookeeper, Kafka, Cassandra, MQTT-Connector, and a Kafka-Streams-App. It collects IoT data from MQTT, processes it using Kafka Streams, and stores the data in Cassandra. It also visualizes the data with Grafana.

## System Architecture

MQTT --> MQTT-Connector  --> Kafka-Streams-App --> Cassandra --> Grafana 

## Services
Grafana: A platform for monitoring and observability. It makes it easy to visualize and alert on the metrics from your infrastructure and applications.
Zookeeper: A centralized service for maintaining configuration information, naming, providing distributed synchronization, and group services. Kafka uses Zookeeper.

Kafka: A distributed streaming platform that allows applications to publish and subscribe to streams of records in a fault-tolerant way.
Cassandra: A distributed NoSQL database that provides high availability and scalability.

MQTT-Connector: A custom service that receives messages from an MQTT broker and publishes them to a Kafka topic.

Kafka-Streams-App: A custom service that consumes the Kafka topic, processes the messages with Kafka Streams, and stores the results in Cassandra.

## Prerequisites
Docker and Docker-Compose should be installed on your machine.
You should have basic knowledge of Docker, Docker-Compose, and Kafka.

## How to run the project
Clone this repository and navigate to the directory containing the docker-compose.yml file.
Build the MQTT-Connector and Kafka-Streams-App services by running the following command:

`docker-compose build`

Start all services by running the following command:

`docker-compose up`

The Grafana UI can be accessed at http://localhost:3000 with the username and password admin.

To stop all services, press Ctrl+C, then run:

`docker-compose down`

## Limitations
This project is designed for development and testing purposes. It is not intended to be used in a production environment without additional security measures.

## Contributions
Contributions, issues, and feature requests are welcome. Feel free to check the issues page for any open issues or create a new one.

## Contact
For more information or any questions, please feel free to contact me.

## Acknowledgments
This project was inspired by the need to handle large volumes of real-time IoT data in a scalable, distributed manner. The open-source community was a great help in building this project.
