package com.quqee.backend.internship_hits.logs.service

import com.quqee.backend.internship_hits.model.rest.CreateLogRequestView
import com.quqee.backend.internship_hits.model.rest.CreatedLogView
import com.quqee.backend.internship_hits.model.rest.LogsListView
import com.quqee.backend.internship_hits.model.rest.UpdateLogRequestView
import java.util.*


interface TagService {
    fun getTags(): LogsListView
}

class TagService {
}