package com.quqee.backend.internship_hits.public_interface.common

data class LastIdPaginationRequest<IdType>(
    val lastId: IdType? = null,
    private val pageSize: Int? = DEFAULT_PAGE_SIZE,
    val sorting: SortingStrategy = SortingStrategy.CREATED_AT_DESC,
) {
    val size: Int = DEFAULT_PAGE_SIZE
    val sizeForSelect get() = size + 1
    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }
}
