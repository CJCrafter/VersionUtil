plugins {
    kotlin("jvm") version "1.9.23"
    `maven-publish`
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "com.cjcrafter"
version = "1.0.0"

repositories {
    mavenCentral()

    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.6-R0.1-SNAPSHOT")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

tasks {
    test {
        useJUnitPlatform()
    }
    compileKotlin {
        kotlinOptions {
            jvmTarget = "16"
        }
    }
    compileJava {
        options.encoding = "UTF-8"
        options.release = 16
    }
}
kotlin {
    jvmToolchain(21)
}
