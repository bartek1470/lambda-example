package pl.bartek.example.lambda.spring

import com.amazonaws.xray.spring.aop.XRayEnabled
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import java.util.function.Function
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.PathWalkOption
import kotlin.io.path.absolutePathString
import kotlin.io.path.walk

private val log = KotlinLogging.logger {}

@XRayEnabled
@Component
class MessageProcessor(
    private val anotherComponent: AnotherComponent,
) : Function<Message<String>, String> {
    override fun apply(message: Message<String>): String {
        logPaths()
        try {
            anotherComponent.failMethod()
        } catch (e: Exception) {
            log.error(e) { "Fail" }
        }

        return "Hello World!"
    }

    @OptIn(ExperimentalPathApi::class)
    private fun logPaths() {
        log.info {
            val files =
                Path(".")
                    .walk(PathWalkOption.BREADTH_FIRST)
                    .map { path -> path.absolutePathString() }
                    .toList()
            "current dir: ${files.joinToString("\n")}"
        }
        log.info {
            val files =
                Path("/lib")
                    .walk(PathWalkOption.BREADTH_FIRST)
                    .map { path -> path.absolutePathString() }
                    .toList()
            "lib: ${files.joinToString("\n")}"
        }
        log.info {
            val files =
                Path("/lib64")
                    .walk(PathWalkOption.BREADTH_FIRST)
                    .map { path -> path.absolutePathString() }
                    .toList()
            "lib64: ${files.joinToString("\n")}"
        }
        log.info {
            val files =
                Path("/opt")
                    .walk(PathWalkOption.BREADTH_FIRST)
                    .map { path -> path.absolutePathString() }
                    .toList()
            "opt: ${files.joinToString("\n")}"
        }
        log.info {
            val files =
                Path("/var/runtime")
                    .walk(PathWalkOption.BREADTH_FIRST)
                    .map { path -> path.absolutePathString() }
                    .toList()
            "/var/runtime: ${files.joinToString("\n")}"
        }
        log.info { "Classpath: ${System.getProperty("java.class.path")}" }
    }
}
