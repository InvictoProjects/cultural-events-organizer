package com.invictoprojects.ncs

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("cleanup")
class CleanupProperties {
    var expirationInDays: Int? = null
}
