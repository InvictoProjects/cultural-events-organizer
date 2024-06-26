package com.invictoprojects.model

import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "events")
class Event(
    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val startTime: LocalDateTime,

    @Column(nullable = false)
    val endTime: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val eventType: EventType,

    @ManyToOne
    val category: EventCategory,

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "event_creators",
        joinColumns = [JoinColumn(name = "event_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val creators: MutableSet<User> = HashSet(),

    @Column
    val location: String?,

    @Column(nullable = false)
    val isTicketsLimited: Boolean,

    @Column(name = "max_tickets")
    val maxTickets: Long? = null,

    @Column(nullable = false)
    val isAskingForVolunteers: Boolean = false,

    val maxVolunteersAmount: Long? = null,

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "event_volunteers",
        joinColumns = [JoinColumn(name = "event_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val volunteers: MutableSet<User> = HashSet(),

    @Column(nullable = false)
    val price: Double,

    @Column(nullable = false)
    var isBlocked: Boolean = false,

    val isPrivate: Boolean = false,

    @Column(unique = true)
    var invitationCode: String? = null,

    @Column
    val url: String?,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)
