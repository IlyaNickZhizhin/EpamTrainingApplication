# Gym Management System

## Overview
This is a RESTful backend application for managing a gym. It allows the registration of trainers and trainees, booking of training sessions, viewing available trainers, and the trainings that trainees have signed up for.

## Features
- **Trainer and Trainee Registration**: Allows trainers and trainees to register in the system.
- **Training Session Booking**: Enables trainees to book training sessions.
- **View Available Trainers**: Trainees can view the trainers available for training sessions.
- **View Booked Training Sessions**: Trainees can view the training sessions they have signed up for.

## Versions
The application has two versions:
- **Local Version**: Uses PostgreSQL and MongoDB databases, with interaction through ActiveMQ.
- **Cloud Version**: Hosted on Amazon Cloud, uses PostgreSQL and DynamoDB databases, with interaction through Amazon SQS.

## Controllers
- **Login Controller**: Handles user authentication.
- **Trainee Controller**: Handles registration, updating, deleting, and selecting trainees.
- **Trainer Controller**: Handles registration, updating, deleting, and selecting trainers.
- **Training Controller**: Handles the creation of training sessions and other operations related to trainings.

## Technologies Used
- Java
- Spring Boot
- PostgreSQL
- MongoDB
- ActiveMQ
- Amazon Cloud
- DynamoDB
- Amazon SQS
- Swagger for API documentation

## Setup Instructions
For the local version, use the `docker-compose.yml` file. For the cloud version, create two microservice images using the `DockerfileGymService` and `DockerfileReportService` Dockerfiles to create tar files. Then, deploy them on EC2 instances, create images from them, and set them in the lines of `EAT.yaml`, which is a CloudFormation template that deploys all the necessary infrastructure for the application.

## License
This application does not have any license.

