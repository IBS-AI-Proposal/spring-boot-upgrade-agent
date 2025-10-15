# Spring Boot Upgrade Agent

This CLI agent uses an LLM (e.g., OpenAI GPT-4) to analyze a Spring Boot project and suggest or apply upgrades.

##### Initial Setup
Set the environment variables:
M2_HOME, JAVA_HOME etc

##### Build command :
`./mvnw clean build`

##### To Run the application :
`./mvnw spring-boot:run`

##### If you have a ~/.m2/settings.xml that points to your organizations maven repo
`./mvnw -s settings.xml spring-boot:run`
