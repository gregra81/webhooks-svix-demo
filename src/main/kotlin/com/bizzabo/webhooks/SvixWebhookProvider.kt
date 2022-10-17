package com.bizzabo.webhooks

import com.svix.kotlin.Svix
import com.svix.kotlin.SvixOptions
import com.svix.kotlin.models.ApplicationIn
import com.svix.kotlin.models.ApplicationOut
import com.svix.kotlin.models.EndpointIn
import com.svix.kotlin.models.EndpointOut
import com.svix.kotlin.models.EndpointUpdate
import com.svix.kotlin.models.MessageIn
import com.svix.kotlin.models.MessageOut
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Base64

@Component
class SvixWebhookProvider(
    @Value("\${svix.domain}") private val svixDomain: String,
    @Value("\${svix.token}") private val svixToken: String,
) {
    private val svixClient = Svix(svixToken, SvixOptions(serverUrl = svixDomain))

    suspend fun createApplication(name: String, uuid: String, rateLimit: Int? = null): ApplicationOut =
        svixClient.application.create(
            ApplicationIn(
                name = name,
                uid = uuid,
                rateLimit = rateLimit
            )
        )

    suspend fun createEndpoint(
        appId: String,
        endpointIn: EndpointIn
    ): EndpointOut = svixClient.endpoint.create(appId, endpointIn)
}
