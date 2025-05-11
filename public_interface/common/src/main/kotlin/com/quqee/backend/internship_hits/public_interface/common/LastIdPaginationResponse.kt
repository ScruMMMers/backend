package com.quqee.backend.internship_hits.public_interface.common

data class LastIdPaginationResponse<T : IHaveId<Id>, Id>(
    private val collection: Collection<T>,
    private val pagination: LastIdPaginationRequest<Id>,
    val fullSize: Int?,
) {
    val requestedPageSize: Int = pagination.size
    val responseCollection: Collection<T> = collection.take(requestedPageSize)
    val actualPageSize: Int = responseCollection.size
    val hasNextPage: Boolean = collection.size > requestedPageSize
    val lastId: Id? = responseCollection.lastOrNull()?.id
}
