tasks.create("spotlessApply") {
    group = "verification"
    setDependsOn(gradle.includedBuilds.map { it.task(":spotlessApply") })
}

tasks.create("spotlessCheck") {
    group = "verification"
    setDependsOn(gradle.includedBuilds.map { it.task(":spotlessCheck") })
}

tasks.create("clean") {
    group = "build"
    setDependsOn(gradle.includedBuilds.map { it.task(":spotlessCheck") })
}
