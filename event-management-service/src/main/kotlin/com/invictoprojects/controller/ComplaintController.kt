package com.invictoprojects.controller

import com.invictoprojects.dto.ComplaintDto
import com.invictoprojects.model.Complaint
import com.invictoprojects.model.ComplaintStatus
import com.invictoprojects.service.CompliantService
import com.invictoprojects.service.EventService
import com.invictoprojects.service.UserService
import com.oracle.svm.core.annotate.Delete
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import jakarta.inject.Inject
import java.security.Principal

@Controller("/api/complaint")
class ComplaintController(
    @Inject private val complaintService: CompliantService,
    @Inject private val eventService: EventService,
    @Inject private val userService: UserService
) {

    @Get("/user/")
    @Secured("USER")
    fun getUserComplaints(principal: Principal): List<Complaint> {
        return complaintService.getUserComplaints(principal.name)
    }

    @Get("/event/{eventId}")
    @Secured("USER")
    fun getEventComplaints(eventId: Long, principal: Principal): HttpResponse<Any> {
        val event = eventService.findById(eventId)
        if (event.creator.username == principal.name) {
            return HttpResponse.ok(complaintService.getEventComplaints(eventId))
        }
        return HttpResponse.notAllowed()
    }

    @Get
    @Secured("ADMIN")
    fun getAll(): List<Complaint> {
        return complaintService.findAll()
    }

    @Get("/{id}")
    @Secured("USER")
    fun getById(id: Long, principal: Principal): HttpResponse<Any> {
        val complaint = complaintService.findById(id)
        if (complaint != null) {
            if (complaint.author.username == principal.name) {
                return HttpResponse.ok(complaint)
            }
        }
        return HttpResponse.notAllowed()
    }

    @Post
    fun create(principal: Principal, @Body complaintDto: ComplaintDto): Complaint {
        val user = userService.findByUsername(principal.name)
        val event = eventService.findById(complaintDto.eventId)
        val complaint = Complaint(
            status = ComplaintStatus.REPORTED,
            event = event,
            author = user!!,
            text = complaintDto.text
        )
        return complaintService.create(complaint)
    }

    @Delete("/{id}")
    @Secured("USER")
    fun deleteById(id: Long, principal: Principal): HttpResponse<Any> {
        val complaint = complaintService.findById(id)
        if (complaint != null) {
            if (complaint.author.username == principal.name) {
                return HttpResponse.noContent()
            }
        }
        return HttpResponse.notAllowed()
    }

    @Post("/{id}/approve")
    @Secured("ADMIN")
    fun approveComplaint(id: Long) {
        return complaintService.approve(id)
    }

    @Post("/{id}/cancel")
    @Secured("ADMIN")
    fun cancelComplaint(id: Long) {
        return complaintService.cancel(id)
    }

}
