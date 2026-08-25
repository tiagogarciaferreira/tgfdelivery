import java.time.Instant

object Versions {
    const val SPRING_CLOUD_VERSION = "2025.1.2"
}

plugins {
    idea
    java
    id("org.springframework.boot") version "4.1.1"
    id("io.spring.dependency-management") version "1.1.7"
}

description = "Service responsible for delivery tracking."
group = "com.tgfcodes.tgfdelivery"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

springBoot {
    buildInfo {
        properties {
            name.set(project.name)
            artifact.set(project.name)
            time.set(Instant.now().toString())
            version.set(project.version.toString())
            additional.set(mapOf("description" to (project.description ?: "")))
            excludes.set(setOf("group"))
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-kafka")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    runtimeOnly("org.postgresql:postgresql")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-kafka-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${Versions.SPRING_CLOUD_VERSION}")
    }
    dependencies {
        dependency("org.apache.httpcomponents:httpclient:4.5.14")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}