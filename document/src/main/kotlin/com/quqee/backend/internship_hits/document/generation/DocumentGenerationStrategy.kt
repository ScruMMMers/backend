package com.quqee.backend.internship_hits.document.generation

import org.apache.poi.xssf.usermodel.XSSFWorkbook

interface DocumentGenerationStrategy<T> {
    fun generate(params: Map<String, List<T>>): XSSFWorkbook
}
