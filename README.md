# Las rosas IOT

## Features:
- Ingest messages from Lorawan sensors and any other source.
- Handle these messages with Digital Twins containing the business logic.
- Integration with  Home Automation

## Architecture
- Hexagonal architecture
- Single service, but may be extended to support multiple services (micro or not)
- Java, Spring Boot, JPA/hibernate/postgres, mqtt, Docker (or not, up to you)

## Intent
I use this server to handle IOT events in my home, a large and old agricol farm in Andalusia (Finca)
to get the level of water in my water deposites, temperatures, or wireless remote switches.

I have a RAK DIY Loraway gateway installed on my roof and my data center contains a charming 4 Rapsberry rack with a Unifi network.

Work is on-goint.
