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

description = "Service responsible for registry of services."
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
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
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