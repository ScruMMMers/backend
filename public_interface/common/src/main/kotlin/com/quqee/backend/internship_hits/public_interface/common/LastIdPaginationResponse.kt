package com.quqee.backend.internship_hits.public_interface.common

data class LastIdPaginationResponse<T : IHaveId<Id>, Id>(
    private val collection: Collection<T>,
    private val pagination: LastIdPaginationRequest<Id>,
) {
    val requestedPageSize: Int = pagination.size
    val actualPageSize: Int = collection.size
    val hasNextPage: Boolean = actualPageSize > requestedPageSize
    val responseCollection: Collection<T> = collection.take(pagination.size)
    val lastId: Id? = responseCollection.lastOrNull()?.id
}
