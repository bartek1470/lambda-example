package pl.bartek

import com.amazonaws.xray.AWSXRay
import io.github.oshai.kotlinlogging.KotlinLogging

private val log = KotlinLogging.logger { }

fun <T> xRaySegment(
    name: String,
    action: () -> T,
): T {
    try {
        val subsegment = AWSXRay.beginSubsegment(name)
        subsegment.metadata = mapOf("ClassInfo" to mapOf("Class" to className(action)))
        return action()
    } catch (e: Exception) {
        log.error(e) { }
        AWSXRay.getCurrentSubsegment()?.addException(e)
        throw e
    } finally {
        log.trace { "Ending Subsegment" }
        AWSXRay.endSubsegment()
    }
}

/**
 * Implementation from io.github.oshai.kotlinlogging.internal.KLoggerNameResolver#name
 */
private fun <T> className(func: () -> T): String {
    val name = func.javaClass.name
    val slicedName =
        when {
            name.contains("Kt$") -> name.substringBefore("Kt$")
            name.contains("$") -> name.substringBefore("$")
            else -> name
        }
    return slicedName
}
