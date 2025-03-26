package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.logs.service.ReactionService
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.model.rest.PossibleReactionListView
import com.quqee.backend.internship_hits.model.rest.PossibleReactionView
import com.quqee.backend.internship_hits.model.rest.ReactionListView
import com.quqee.backend.internship_hits.model.rest.ReactionView
import com.quqee.backend.internship_hits.public_interface.reaction.PossibleReactionDto
import com.quqee.backend.internship_hits.public_interface.reaction.ReactionDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class ReactionController(
    private val reactionService: ReactionService,
    private val reactionMapper: FromInternalToApiMapper<ReactionView, ReactionDto>,
    private val possibleReactionMapper: FromInternalToApiMapper<PossibleReactionView, PossibleReactionDto>
): ReactionApiDelegate {
    override fun reactionsAddLogIdPost(
        logId: UUID,
        reactionId: UUID
    ): ResponseEntity<ReactionView> {
        val reactionDto = reactionService.addReactionToLog(logId, reactionId)
        return ResponseEntity.ok(reactionMapper.fromInternal(reactionDto))
    }

    override fun reactionsDeleteLogIdDelete(
        logId: UUID,
        reactionId: UUID
    ): ResponseEntity<Unit> {
        reactionService.removeReactionFromLog(logId, reactionId)
        return ResponseEntity.ok().build()
    }

    override fun reactionsLogIdGet(logId: UUID): ResponseEntity<ReactionListView> {
        val reactions = reactionService.getLogReactions(logId).map { reactionMapper.fromInternal(it) }
        return ResponseEntity.ok(ReactionListView(reactions))
    }

    override fun reactionsPossibleGet(): ResponseEntity<PossibleReactionListView> {
        val possibleReactions = reactionService.getAllPossibleReactions().map { possibleReactionMapper.fromInternal(it) }
        return ResponseEntity.ok(PossibleReactionListView(possibleReactions))
    }
}