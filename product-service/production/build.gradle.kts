import org.gradle.internal.impldep.com.sun.jna.StringArray
import org.jetbrains.kotlin.codegen.intrinsics.ArraySet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.0-M2"
	id("io.spring.dependency-management") version "1.1.3"
	id("com.google.protobuf") version "0.9.4"
	kotlin("jvm") version "1.9.10"
	kotlin("plugin.spring") version "1.9.10"
	kotlin("plugin.jpa") version "1.9.10"
}

group = "com.amrtm.microservice.store"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
	implementation("io.springfox:springfox-swagger-ui:3.0.0")
	implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
//	implementation("io.grpc:grpc-kotlin-stub:1.2.0")
//	implementation("com.google.protobuf:protobuf-java:3.22.3")
//	implementation("io.grpc:grpc-protobuf:1.58.0")
//	implementation("io.grpc:grpc-stub:1.58.0")
//	compileOnly("org.apache.tomcat:annotations-api:6.0.53")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testImplementation("org.testcontainers:kafka")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

sourceSets {
	main {
		kotlin {
			srcDir("src/main/kotlin")
		}
		proto {
			srcDir("src/main/proto")
		}
		resources {
			srcDir("src/main/resources")
		}
	}
}

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:3.22.3"
	}
	plugins {
		create("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:1.58.0"
		}
	}
	generateProtoTasks {
		all().forEach {
			it.plugins {
				create("grpc")
			}
		}
	}
}

tasks.processResources {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
