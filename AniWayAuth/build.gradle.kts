plugins {
	java
	id("org.springframework.boot") version "3.4.1"
}

group = "com.shadowshiftstudio"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:3.4.1")
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.1")
    implementation("org.springframework.boot:spring-boot-starter-security:3.4.1")
    implementation("org.springframework.boot:spring-boot-starter-logging:3.4.1")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.1")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    runtimeOnly("org.postgresql:postgresql")
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("org.springframework:spring-web:5.3.10")
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor ("org.projectlombok:lombok:1.18.36")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.4.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
