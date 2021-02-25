import org.gradle.api.JavaVersion.VERSION_11

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin library project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/6.7.1/userguide/building_java_projects.html
 */

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.4.21"
    id("org.jetbrains.kotlin.kapt") version "1.4.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.21"
    id("pl.allegro.tech.build.axion-release") version "1.12.1"
    id("io.micronaut.library") version "1.3.2"
    `maven-publish`
    jacoco
}
group = "io.github.chase22.telegram"

version = scmVersion.version

val kotlinVersion = project.properties["kotlinVersion"]
System.getenv().forEach(::println)

java {
    sourceCompatibility = VERSION_11
    targetCompatibility = VERSION_11
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = VERSION_11.toString()
}

repositories {
    // Use JCenter for resolving dependencies.
    jcenter()
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")

    implementation("org.telegram:telegrambots:5.0.1")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.3.2")
    testImplementation("io.mockk:mockk:1.10.5")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "$group"
            artifactId = rootProject.name
            version = scmVersion.version
            from(components["java"])
            artifact(tasks.kotlinSourcesJar)
        }
    }
    repositories {
        maven {
            url = uri("https://chase-186482393463.d.codeartifact.eu-central-1.amazonaws.com/maven/chase-repository/")
            credentials {
                username = "aws"
                password = getAuthToken()
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

// ******
// Jacoco - see also: https://docs.gradle.org/current/userguide/jacoco_plugin.html
// ******

 jacoco {
    toolVersion = "0.8.6"
}


tasks.jacocoTestReport {
    classDirectories.setFrom(
        fileTree("build/classes/kotlin/main"))

    reports {
        xml.isEnabled = true
        csv.isEnabled = false
        html.destination = file("$buildDir/jacocoHtml")
    }
}

fun getAuthToken(): String {
    val awsAuthTokenProcess = ProcessBuilder().command(
        "aws", "codeartifact", "get-authorization-token",
        "--domain", "chase",
        "--domain-owner", "186482393463",
        "--query", "authorizationToken",
        "--duration-seconds", "900",
        "--output", "text"
    ).start()
    awsAuthTokenProcess.waitFor(1, TimeUnit.MINUTES)
    return awsAuthTokenProcess.inputStream.bufferedReader().readText()
}