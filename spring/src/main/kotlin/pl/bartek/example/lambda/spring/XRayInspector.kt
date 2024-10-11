package pl.bartek.example.lambda.spring

import com.amazonaws.xray.AWSXRay
import com.amazonaws.xray.entities.Subsegment
import com.amazonaws.xray.spring.aop.BaseAbstractXRayInterceptor
import com.amazonaws.xray.spring.aop.XRayInterceptorUtils
import io.github.oshai.kotlinlogging.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Aspect
@Component
class XRayInspector : BaseAbstractXRayInterceptor() {
    @Throws(Exception::class)
    override fun generateMetadata(
        proceedingJoinPoint: ProceedingJoinPoint,
        subsegment: Subsegment,
    ): Map<String, Map<String, Any>> {
        return super.generateMetadata(proceedingJoinPoint, subsegment)
    }

    @Throws(Throwable::class)
    override fun processXRayTrace(pjp: ProceedingJoinPoint): Any? {
        try {
            val subsegment = AWSXRay.beginSubsegment(pjp.signature.name)
            if (subsegment != null) {
                subsegment.metadata = generateMetadata(pjp, subsegment)
            }
            return XRayInterceptorUtils.conditionalProceed(pjp)
        } catch (e: Exception) {
            // https://github.com/aws/aws-xray-sdk-java/issues/19#issuecomment-390362827
            // Fix for lambdas since the base segment can't be modified because it throws exception with message `FacadeSegments cannot be mutated.`
            // This exception happens when an exception was unhandled. In case of a handled exception, the exception wasn't reported at all
            AWSXRay.getCurrentSubsegment()?.addException(e)
            throw e
        } finally {
            log.trace { "Ending Subsegment" }
            AWSXRay.endSubsegment()
        }
    }

    // @XRayEnabled is a class level annotation which is alternative to implementing XRayTraced interface
    @Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled)")
    public override fun xrayEnabledClasses() {
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}
