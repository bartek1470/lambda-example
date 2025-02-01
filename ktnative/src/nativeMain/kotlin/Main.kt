import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.KotlinLoggingConfiguration
import io.github.oshai.kotlinlogging.Level
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import platform.posix.getenv

@Serializable
private data class Message(
    val topic: String,
    val content: String,
)

private val PrettyPrintJson =
    Json {
        prettyPrint = true
    }

private val log = KotlinLogging.logger {}

@OptIn(ExperimentalForeignApi::class)
fun main() {
    KotlinLoggingConfiguration.logLevel = Level.TRACE
    val handler = getenv("_HANDLER")?.toKString() ?: throw IllegalStateException("_HANDLER not set")
    log.info { "Initializing... Handler: $handler" }
    val client = HttpClient(CIO)
    while (true) {
        runBlocking {
            val awsLambdaRuntimeApi =
                getenv("AWS_LAMBDA_RUNTIME_API")?.toKString()
                    ?: throw IllegalStateException("AWS_LAMBDA_RUNTIME_API not set")
            log.info { "AWS Lambda Runtime API: $awsLambdaRuntimeApi" }
            val response = client.get("http://$awsLambdaRuntimeApi/2018-06-01/runtime/invocation/next")
            val requestId =
                response.headers["Lambda-Runtime-Aws-Request-Id"] ?: throw IllegalStateException(
                    "Lambda-Runtime-Aws-Request-Id not set",
                )
            log.info { "Lambda-Runtime-Aws-Request-Id: $requestId" }

            val eventData = getenv("EVENT_DATA")?.toKString() ?: throw IllegalStateException("EVENT_DATA not set")
            log.info { "EVENT_DATA: $eventData" }
            client.post("http://$awsLambdaRuntimeApi/2018-06-01/runtime/invocation/$requestId/response") {
                setBody(eventData)
            }
        }
    }
//    aSocket(SelectorManager()).udp()
//        .bind(InetSocketAddress("0.0.0.0", 12345))
//        .send(Datagram(Source()))
}
