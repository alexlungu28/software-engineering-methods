# CSE2115 - Project

## Gradle Setup

### Global Tasks

This a list of Gradle tasks you can run:

-   `./gradlew clean` remove all build files
-   `./gradlew build` build all microservices
-   `./gradlew test` run all tests across all microservices
-   `./gradlew jacocoTestReport` creates a coverage report for each microservice as a `.html` file located at `services/<service>/build/reports/jacoco/test/html/index.html`
-   `./gradlew jacocoTestCoverageVerification` checks whether test verification rules are met across all tests for all services
-   `./gradlew checkstyleMain checkstyleTest` run checkstyle across all microservices (main files as well as test files)

### Service-specific Tasks

Each task can also be run for a specific microservice, where `<service>` is any of {`authentication`, `courses`, `room-scheduler`, `rooms`, `student`} for example:

-   `./gradlew services:<service>:clean` remove build files for `<service>`
-   `./gradlew services:<service>:build` build `<service>` project
-   etc.

### Running a service

To run a single service use: `./gradlew services:<service>:bootRun` (this task can technically also be run without specifying a specific service, but that will only start the first service and get stuck at 15% of executing the Gradle task, so you need to start each service individually)

## Notes

-   You should have a local .gitignore file to make sure that any OS-specific and IDE-specific files do not get pushed to the repo (e.g. .idea). These files do not belong in the .gitignore on the repo.
-   If you change the name of the repo to something other than template, you should also edit the build.gradle file.
-   You can add issue and merge request templates in the .gitlab folder on your repo.
