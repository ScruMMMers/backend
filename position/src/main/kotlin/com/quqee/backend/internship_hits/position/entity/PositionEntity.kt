package com.quqee.backend.internship_hits.position.entity

import com.quqee.backend.internship_hits.public_interface.common.enums.PositionEnum
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "positions")
open class PositionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long,

    @Column(name = "name", nullable = false)
    open var name: String,

    @Column(name = "position", nullable = false)
    open var position: PositionEnum

) : Serializable {
    constructor() : this(0, "", PositionEnum.BACKEND)
}