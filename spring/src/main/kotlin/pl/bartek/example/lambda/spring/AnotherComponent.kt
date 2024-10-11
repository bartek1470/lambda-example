package pl.bartek.example.lambda.spring

import com.amazonaws.xray.spring.aop.XRayTraced
import org.springframework.stereotype.Component

@Component
class AnotherComponent : XRayTraced {
    fun failMethod() {
        throw RuntimeException("oopsie whoopsie")
    }
}
