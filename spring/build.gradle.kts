plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    alias(libs.plugins.spotless)
}

group = "pl.bartek.lambda"
version = "0.0.1-SNAPSHOT"

dependencyManagement {
    imports {
        mavenBom(libs.springCloudDependencies.get().toString())
        mavenBom("com.amazonaws:aws-xray-recorder-sdk-bom:2.18.2")
    }
}

repositories {
    mavenCentral()
}

configurations.all {
    exclude(group = "commons-logging")
}

dependencies {
    implementation(libs.springBootStarter)
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
    implementation("net.logstash.logback:logstash-logback-encoder:8.0")
    implementation(libs.kotlin.reflect)
    implementation(libs.springCloudFunctionAdapterAws)
    implementation(libs.springCloudFunctionContext)

    // https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-java.html#xray-sdk-java-submodules
    implementation("com.amazonaws:aws-xray-recorder-sdk-core")
    implementation("com.amazonaws:aws-xray-recorder-sdk-spring")
    implementation("com.amazonaws:aws-xray-recorder-sdk-slf4j")

    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.kotlin.test.junit)
    testRuntimeOnly(libs.junitPlatformLauncher)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.AMAZON
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.jar {
    manifest {
        attributes(mapOf("Main-Class" to "pl.bartek.example.lambda.spring.Application")) // required by Spring Cloud Function
    }
}

val packageLambdaLayer = tasks.create("packageLambdaLayer", Zip::class) {
    group = "aws lambda"
    description = "Creates a AWS Lambda layer package from the dependencies"

    archiveFileName.set("${project.name}-layer.zip")

    from(configurations.runtimeClasspath) {
        into("java/lib")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

spotless {
    kotlin {
        ktlint()
    }
}
