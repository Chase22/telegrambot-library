# telegrambot-library

a bunch of miscelanious functions for my telegram bots

### Gradle commands

Description | Command
------ | ------
Build | ./gradlew build
Prepare Release Bugfix | ./gradlew release
Prepare Release Minor | ./gradlew release -Prelease.versionIncrementer=incrementMinor
Prepare Release Major | ./gradlew release -Prelease.versionIncrementer=incrementMajor
Push Release | ./gradlew release