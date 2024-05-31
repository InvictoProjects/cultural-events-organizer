package com.invictoprojects.model

import java.time.Instant
import javax.persistence.*


@Entity
@Table(name = "tickets")
class Ticket(

    @ManyToOne
    val event: Event,

    @ManyToOne
    val user: User,

    @Column(name = "purchase_time", nullable = false)
    val purchaseTime: Instant,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: TicketStatus,

    @Column(nullable = false)
    val price: Double,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

)
