# Las rosas IOT

## Description
Refactoring of the previous project lasrosasiot-old.

I wrote this applicaiton to manage the events coming from my Lorawan sensors at home to 
manage my water tanks levels, temperature and trigger remote switches in area without 5G/wifi....

The refactoring objectives are:
- use the Clean/Hexagonal design principles.
- simplify the code.
- allow a smooth integration with HomeAutomation.
- modularize the code
- support docker

## Architecture
The project contains services. There are 3 services:
- Ingestor: providing a mqtt endpoint for my RAK Lorawan gateway. The code can be easily customized to support other protocol or gateways.
- Reactor: contained the Digital Twin and business code.
- Integrator: integrate other systems: save to the time-serie database, integrate Home Automation...
- Frontend: a react frontend to edit the sensors.

It's an on-going work.

## Releases
- Release 1.0: Ingestor
  decode Lorawan messages and sen the normalized messages to mqtt
- Release 1.1: Integrator
  Save to the time serie database InfluxDB, create Grafana dashboards
- Release 1.2: Reactor
  add the digital twins to execute business code
