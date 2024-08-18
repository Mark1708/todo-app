import dev.monosoul.jooq.RecommendedVersions
import dev.monosoul.jooq.migration.MigrationLocation

plugins {
    kotlin("kapt") version "1.7.21"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("dev.monosoul.jooq-docker") version "6.0.0"
}

group = "com.mark1708"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation ("org.springframework.boot:spring-boot-starter-jetty")
    implementation ("org.springframework.boot:spring-boot-starter-log4j2")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    project.extra["jooq.version"] = RecommendedVersions.JOOQ_VERSION
    project.extra["flyway.version"] = RecommendedVersions.FLYWAY_VERSION
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.flywaydb:flyway-core")
    runtimeOnly("org.postgresql:postgresql")
    "org.postgresql:postgresql"
        .also(::runtimeOnly)
        .also(::jooqCodegen)


    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.mapstruct.extensions.spring:mapstruct-spring-annotations:0.1.2")
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")

    implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks {
    generateJooqClasses {
        schemas.set(listOf("public"))
        basePackageName.set("com.mark1708.todo.generated")
        outputDirectory.set(project.layout.projectDirectory.dir("src/main/generated-jooq"))
        migrationLocations.setFromFilesystem(
            project.files("$projectDir/src/main/resources/db/migration"),
        )
        migrationLocations.set(
            listOf(
                MigrationLocation.Filesystem(
                    project.files("$projectDir/src/main/resources/db/migration"),
                ),
            ),
        )
        usingJavaConfig {
            database.isIncludeExcludeColumns = true
            database.withExcludes(
                listOf(
                    "(pg_catalog|information_schema)\\..*",
                    "flyway_schema_history"
                ).joinToString("|"),
            )
        }
    }
}

sourceSets.getByName("main") {
    java.srcDir("src/main/generated-jooq")
}

tasks.withType<Test> {
    useJUnitPlatform()
}