plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("kapt") version "1.8.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "pl.tuso.switcher"
version = "1.0"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    kapt("com.velocitypowered:velocity-api:3.1.1")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        project.configurations.implementation.get().isCanBeResolved = true
        configurations = listOf(project.configurations.implementation.get())
        minimize()
    }
}

kotlin {
    jvmToolchain(11)
}