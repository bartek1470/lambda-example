plugins {
    alias(libs.plugins.kotlinJvm)
    application
    alias(libs.plugins.spotless)
}

group = "pl.bartek.lambda"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("com.amazonaws:aws-xray-recorder-sdk-bom:2.18.2"))
    implementation(platform(libs.aws.sdk.bom))
    implementation(libs.logback.classic)
    implementation(libs.logback.core)
    implementation(libs.slf4j.api)
    implementation(libs.kotlin.logging.jvm)
    implementation(libs.logstashLogbackEncoder)
    compileOnly(libs.aws.lambdaJavaCore)

    // https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-java.html#xray-sdk-java-submodules
    implementation("com.amazonaws:aws-xray-recorder-sdk-core")
    implementation("com.amazonaws:aws-xray-recorder-sdk-slf4j")

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.AMAZON
    }
}

application {
    mainClass.set("pl.bartek.TestHandler")
}

tasks.test {
    useJUnitPlatform()
}

val packageLambdaLayer = tasks.create("packageLambdaLayer", Zip::class) {
    group = "aws lambda"
    description = "Creates a AWS Lambda layer package from the dependencies"

    archiveFileName.set("${project.name}-layer.zip")

    from(configurations.runtimeClasspath) {
        into("java/lib")
    }
}

spotless {
    kotlin {
        ktlint()
    }
}
