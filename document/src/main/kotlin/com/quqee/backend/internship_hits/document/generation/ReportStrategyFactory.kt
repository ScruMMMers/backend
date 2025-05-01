package com.quqee.backend.internship_hits.document.generation

import com.quqee.backend.internship_hits.document.generation.implementation.InternshipReportStrategy
import com.quqee.backend.internship_hits.document.model.ReportType

object ReportStrategyFactory {
    fun <T> getStrategy(type: ReportType): DocumentGenerationStrategy<T> {
        @Suppress("UNCHECKED_CAST")
        return when (type) {
            ReportType.INTERNSHIP -> InternshipReportStrategy() as DocumentGenerationStrategy<T>
        }
    }
}