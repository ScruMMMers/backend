package com.quqee.backend.internship_hits.position.entity

import com.quqee.backend.internship_hits.public_interface.common.enums.Position
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "positions")
class PositionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "position", nullable = false)
    @Enumerated(EnumType.STRING)
    var position: Position

) : Serializable {
    constructor() : this(0, "", Position.BACKEND)
}