package com.bizzabo.webhooks

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebhooksApplication

fun main(args: Array<String>) {
	runApplication<WebhooksApplication>(*args)
}
