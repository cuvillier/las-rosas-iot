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
The project contains a react frontend and a Jav Spring boot backend.
The backend is based on event notification and mqtt integration with Spring Integration.

It's an on-going work.
