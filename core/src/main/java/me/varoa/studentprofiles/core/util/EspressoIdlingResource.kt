package me.varoa.studentprofiles.core.util

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
    private const val RESOURCE = "GLOBAL"
    private val COUNTING_IDLING_RESOURCE = CountingIdlingResource(RESOURCE)

    fun increment() {
        COUNTING_IDLING_RESOURCE.increment()
    }

    fun decrement() {
        if (!COUNTING_IDLING_RESOURCE.isIdleNow) {
            COUNTING_IDLING_RESOURCE.decrement()
        }
    }
}
