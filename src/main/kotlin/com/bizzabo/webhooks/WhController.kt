package com.bizzabo.webhooks

import com.svix.kotlin.models.EndpointIn
import kotlinx.coroutines.reactor.mono
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/webhooks")
class WhController(private val svixProvider: SvixWebhookProvider) {
    @PostMapping("/hello-tom")
    fun hello() = mono {
        svixProvider.createApplication("hello-tom", "some-app-id")
        val endpointIn = EndpointIn(
            url = URI.create("http://localhost:8080/webhooks/hello-tom"),
            version = 1
        )
        svixProvider.createEndpoint("some-app-id", endpointIn)
    }
}
