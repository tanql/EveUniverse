# Product Configuration

## Setup

### Prerequisites
This application can be run with or without docker. To run with docker, docker needs to be installed on the machine first.

### Running the application

You can run the application by either running the object `App` from your IDE, or `./gradlew run` in a shell to run without Docker.

You can also run the application with docker by: 

1. Building a jar: "./gradlew clean(if needed) build"
2. Creating a docker image: "docker build -t eve ." (This will tag it the latest. To give it another tag you need to specify it: "docker build -t eve:2.0 .")
3. Run the image: "docker run eve" (run latest) or "docker run eve:..." to specify image. 
The application will now run within docker.  