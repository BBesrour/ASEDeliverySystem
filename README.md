# ASE delivery system
Team: 40

In this repository is code of the delivery system implemented for the lecture Advanced topics of software engineering. 

Readme structure:
1. Overview
2. Quick start
4. Cloud hosting
3. Pipeline

## Overview
### Architecture
![Screenshot_2023-01-31_at_11.55.10](uploads/ba1eeef827213db79c204dbdd27cc4d3/Screenshot_2023-01-31_at_11.55.10.png)
### Requirements
Please make sure you have the following Requirements already installed on your computer before attempting to run the application:
- Docker
## Quick start
In order to run the application in development mode with Docker, execute the following command:
- `docker-compose -f docker-compose.test.yml up -d`
## Cloud hosting
### Mongo Atlas
Our mongo database is hosted in [Mongodb atlas](https://www.mongodb.com/atlas/database)
### Linode
All of our microservices and react app are running on [Linode cloud hosting service](https://www.linode.com)
Our Linode server has 4 CPU Cores and 8GB RAM. It is located in Frankfurt Germany.
## Pipeline
![Screenshot_2023-01-31_at_12.13.06](uploads/963f2a8b4eb12f027dd85f0ee5d8d1eb/Screenshot_2023-01-31_at_12.13.06.png)
