# telegrambot-library
![CI](https://github.com/Chase22/telegrambot-library/workflows/CI/badge.svg) 
[![codecov](https://codecov.io/gh/Chase22/telegrambot-library/branch/main/graph/badge.svg?token=SXZSE01V7D)](https://codecov.io/gh/Chase22/telegrambot-library)

a bunch of miscelanious functions for my telegram bots

### Gradle commands

Description | Command
------ | ------
Build | ./gradlew build
Prepare Release Bugfix | ./gradlew release
Prepare Release Minor | ./gradlew release -Prelease.versionIncrementer=incrementMinor
Prepare Release Major | ./gradlew release -Prelease.versionIncrementer=incrementMajor
Push Release | ./gradlew release
