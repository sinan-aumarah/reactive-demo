# Sample Reactive App demo
This is a simple reactive web app to demonstrate how we can use Spring Webflux to push notifications reactively 

## Prerequisites
- Gradle or MVN
- Node v14+
- Java 8+
- Docker


## How to run it
- Start mongo-DB; `docker-compose up`
- Start backend; `./gradlew bootRun` or if you're using Intellij then just run `ReactiveSpringWebfluxApplication.java`
- Start frontend; `yarn start` inside `frontend` folder