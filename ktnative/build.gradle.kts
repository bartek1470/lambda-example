plugins {
    alias(ktnativeLibs.plugins.spotless)
    alias(ktnativeLibs.plugins.kotlinMultiplatform)
    alias(ktnativeLibs.plugins.kotlinxSerialization)
}

group = "pl.bartek"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    linuxX64 {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(ktnativeLibs.ktor.client.core)
            implementation(ktnativeLibs.ktor.client.cio)
            implementation(ktnativeLibs.kotlin.logging.common)
        }
        nativeMain.dependencies {
            implementation(ktnativeLibs.kotlinxSerializationJson)
        }
    }
}

tasks.create("packageLambda", Zip::class) {
    group = "aws-lambda"
    description = "Creates a AWS Lambda package"
    dependsOn("linuxX64Binaries")

    archiveFileName.set("${project.name}.zip")

    from("${layout.buildDirectory.get().asFile}/bin/linuxX64/releaseExecutable/${project.name}.kexe") {
        filePermissions {
            unix("755")
        }
    }
    from(layout.projectDirectory.file("bootstrap").asFile.absolutePath) {
        filePermissions {
            unix("755")
        }
    }
    from("/lib/x86_64-linux-gnu/libcrypt.so.1") {
        filePermissions {
            unix("755")
        }
        into("lib")
    }
    from("/lib/x86_64-linux-gnu/libc.so.6") {
        filePermissions {
            unix("755")
        }
        into("lib")
    }
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint()
    }
}
