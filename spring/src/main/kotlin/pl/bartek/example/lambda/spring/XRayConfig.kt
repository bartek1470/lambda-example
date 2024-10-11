package pl.bartek.example.lambda.spring

import com.amazonaws.xray.AWSXRay
import com.amazonaws.xray.slf4j.SLF4JSegmentListener
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy

@EnableAspectJAutoProxy
@Configuration
class XRayConfig {
    @PostConstruct
    fun init() {
        with(AWSXRay.getGlobalRecorder()) {
            addSegmentListener(SLF4JSegmentListener("")) // remove `AWS-XRAY-TRACE-ID: ` from trace value
        }
    }
}
