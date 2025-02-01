pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "ktnative"

dependencyResolutionManagement {
    versionCatalogs {
        create("ktnativeLibs") {
            from(files("./ktnativeLibs.versions.toml"))
        }
    }
}
