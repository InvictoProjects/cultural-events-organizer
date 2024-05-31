package com.invictoprojects.ems

import com.amazonaws.services.lambda.runtime.events.ScheduledEvent
import io.micronaut.function.aws.MicronautRequestHandler
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class FunctionRequestHandler: MicronautRequestHandler<ScheduledEvent?, Void?>() {

    @Inject
    lateinit var eventRepository: EventRepository

    @Inject
    lateinit var notificationRepository: NotificationRepository

    override fun execute(input: ScheduledEvent?): Void? {
        runBlocking {
            val job1 = launch {
                handleEndedEvents()
            }
            val job2 = launch {
                handleStartingEvents()
            }

            job1.join()
            job2.join()
        }

        return null
    }

    private fun handleEndedEvents() {
        val endedEvents = eventRepository.findEndedEvents()
        endedEvents.forEach { (eventId, userId) ->
            val rateNotification = Notification(
                message = "Rate this event",
                userId = userId,
                eventId = eventId,
                type = "EVENT_RATE",
                created = LocalDateTime.now()
            )
            notificationRepository.save(rateNotification)
        }
    }

    private fun handleStartingEvents() {
        val startingEvents = eventRepository.findStartingEvents()
        startingEvents.forEach { (eventId, userId) ->
            val startNotification = Notification(
                message = "Your event is starting soon",
                userId = userId,
                eventId = eventId,
                type = "EVENT_START",
                created = LocalDateTime.now()
            )
            notificationRepository.save(startNotification)
        }
    }

}
