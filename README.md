# AWS Cloud Practitioner Certification

This repository is a guide to face the exam to get the first AWS certification Cloud Practitioner through
architecture design exercise with AWS services and capabilities used.

# References

AWS invite you to accept the Global Challenge to get Cloud Practitioner certification through 6 episodes
with information of different services, practice quiz, free voucher for practice exam, Twitch broadcast and more.   
For more information go to: https://pages.awscloud.com/Global-Challenge-Resources.html

# Domains

Below are the domain tested by AWS to validate your knowledge. Each domain reference a set the services,
capabilities and advantages that AWS offers to all that want to use AWS.

* Domain 1: Cloud Concepts

* Domain 2: Security and Compliance

* Domain 3: Technology

* Domain 4: Billing and Pricing

More information go to Exam Guide: https://d1.awsstatic.com/training-and-certification/docs-cloud-practitioner/AWS-Certified-Cloud-Practitioner_Exam-Guide.pdf

# Exam recommendations

Get 30 min aditionals.

## Architecture

This is the architecture view

![Screenshot](https://github.com/JoseLuisSR/springboot-aws-ec2-rds/blob/master/doc/img/architecture-view.png?raw=true)

In this architecture you can find more than 20 AWS services/components, each one with capabilities that help us to resolve or needs.

We walk through each service to know the reason to use it:

* Cloudfront

* Global Accelerator

* Route53

* API Gateway

Service to build HTTP, REST & WebSocket API to integration between client application and back-end systems over internet. 
This is serverless services with high availability and autoscaling capabilities.

This is a serverless service, we need to care about the security of data in transit and aws care for the rest regarding 
the shared responsibility model.

More detail of API Gateway here.

* Shield

It is a security service to protect systems against DDOS (Distribution Deny of Service) threats at Network and Transport 
OSI layers. You can set up policies to deny traffic by geografic places, IP address and more.

* WAF

Security service to protect your web application against threats like SQL Injection, Man In the Middle and others.
You can set up policy to deny request by HTTP Headers, Payload, Query Parameters and others things.

* Cognito

Cognito is an Authentication and Authorization server with identity manager capabilities and generate and validate access token.
You can set up API Gateway with Cognito to validate the access token send in HTTP Header Request Authorization.

You can use grant types like client credentials, authorization code, JWT Bear Token, also you can use authentication capabilities
of social network like facebook to validate the identity of the users.

* Region

* Availability Zone

* VPC

* SubNets

* Security Groups

* NACL

* Route Table

* Internet Gateway

* Network Load Balancer

* EC2 Instances

* RDS

* NAT

* VPC Link

* IAM Roles

* Secrets Manager

* Cloudfront

* Route53


Working in progress...


Spring Boot & AWS EC2 & RDS

This repository is to deploy spring boot application in EC2 instances and integrate with RDS MySQL Database.
EC2 & RDS are inside VPC and SubNets with communication restrictions to control access over internet.
  