package pl.bartek

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.xray.AWSXRay
import com.amazonaws.xray.slf4j.SLF4JSegmentListener
import io.github.oshai.kotlinlogging.KotlinLogging

private val log = KotlinLogging.logger {}

class SimpleHandler : RequestHandler<Map<String, Any>, String> {
    init {
        with(AWSXRay.getGlobalRecorder()) {
            addSegmentListener(SLF4JSegmentListener("")) // remove `AWS-XRAY-TRACE-ID: ` from trace value
        }
    }

    override fun handleRequest(
        input: Map<String, Any>,
        context: Context,
    ): String =
        xRaySegment("handler") {
            log.info { "Received: $input" }
            "hello, world!"
        }
}
