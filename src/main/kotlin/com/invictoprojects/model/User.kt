package com.invictoprojects.model

import com.invictoprojects.model.Role
import javax.persistence.*


@Entity
@Table(name = "users")
class User(
    @Column(unique = true, nullable = false)
    val username: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    @Column(nullable = false)
    val email: String,

    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @Column(name = "last_name", nullable = false)
    val lastName: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)