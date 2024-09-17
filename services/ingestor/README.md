# High level packages organization

The package organization follow the hexagonal ring architecture style:

# Packages:
- services: contains each folder contains a folder, usually packaged as a docker image
  - services/frontend: a trivial react UI
  - service/ingestor: the server top ingest the IOT events and forward to the digital twins
- scripts: utility scripts
- mocks: external systems mocks
- docker: contains the docker compose resources to start the complete system.

# Package service/ingestor
Listen for incoming events from mqtt topics, parse the incoming Lorawan events, send to the related Digital Twin.
Execute the business logic of the digital twin.

Data are saved to a postres database and the time serie points to an influxdb database.
Grafana is use to create dashboards from influxdb.

Data are also sent to Home Automation using the HA MQTT Integration.

Things are managed in SQL now, I may add later a react frontend....

Adapters
  - Presenters - Frontend/API controllers
  - Persister - Persistence of the domai objects
  - Gateways - API / listener to / from integrated systems
Use Cases

Domain
  - Model: domain objects
  - Messages: messages from the sensors and digital twins
  - Ports: interfaces
      - called by the UseCases, implemented by the adapters
      - called by the presenters, implemented by the use case

Dependencies model is very simple and poweful: Domain -> domain (model, ports, message) <- adapters
