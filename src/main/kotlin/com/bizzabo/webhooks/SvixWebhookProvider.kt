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

    suspend fun updateApplication(name: String, uuid: String, rateLimit: Int? = null): ApplicationOut =
        svixClient.application.update(
            uuid,
            ApplicationIn(
                name = name,
                uid = uuid,
                rateLimit = rateLimit
            )
        )

    suspend fun deleteApplication(appId: String) = svixClient.application.delete(appId)

    suspend fun createEndpoint(
        appId: String,
        endpointIn: EndpointIn
    ): EndpointOut {
        val encryptEndpointInSecret = encryptSecret(endpointIn)
        return svixClient.endpoint.create(appId, encryptEndpointInSecret)
    }

    suspend fun updateEndpoint(
        appId: String,
        endpointId: String,
        endpointUpdate: EndpointUpdate,
    ): EndpointOut = svixClient.endpoint.update(appId, endpointId, endpointUpdate)

    suspend fun createMessage(
        appId: String,
        messageIn: MessageIn,
    ): MessageOut = svixClient.message.create(appId, messageIn)

    private fun encryptSecret(endpointIn: EndpointIn): EndpointIn {
        return if (!endpointIn.secret.isNullOrBlank())
            endpointIn.copy(
                secret = Base64.getEncoder().encodeToString(endpointIn.secret?.encodeToByteArray())
            ) else endpointIn
    }
}
