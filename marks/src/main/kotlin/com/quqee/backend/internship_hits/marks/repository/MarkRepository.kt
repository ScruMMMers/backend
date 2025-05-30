package com.quqee.backend.internship_hits.marks.repository

import com.quqee.backend.internship_hits.marks.entity.MarkEntity
import com.quqee.backend.internship_hits.public_interface.common.StudentsMarksProjection
import com.quqee.backend.internship_hits.public_interface.common.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MarkRepository : JpaRepository<MarkEntity, UUID> {

    fun findByUserIdAndSemester(userId: UserId, semester: Int): Optional<MarkEntity>

    fun findAllByUserIdOrderBySemesterAsc(userId: UserId): List<MarkEntity>

    @Query(
        """
            SELECT
                s.user_id AS id,
                s.student_group AS group,
                s.student_course AS course,
                COALESCE(m5.diary_status, 'NONE') AS fifthSemesterDiary,
                m5.mark AS fifthSemesterMark,
                COALESCE(m6.diary_status, 'NONE') AS sixthSemesterDiary,
                m6.mark AS sixthSemesterMark,
                COALESCE(m7.diary_status, 'NONE') AS seventhSemesterDiary,
                m7.mark AS seventhSemesterMark,
                COALESCE(m8.diary_status, 'NONE') AS eighthSemesterDiary,
                m8.mark AS eighthSemesterMark
            FROM
                students s
                    LEFT JOIN
                mark m5 ON s.user_id = m5.user_id AND m5.semester = 5
                    LEFT JOIN
                mark m6 ON s.user_id = m6.user_id AND m6.semester = 6
                    LEFT JOIN
                mark m7 ON s.user_id = m7.user_id AND m7.semester = 7
                    LEFT JOIN
                mark m8 ON s.user_id = m8.user_id AND m8.semester = 8
            WHERE
                s.is_on_academic_leave = 'false'
              AND (:userIds IS NULL OR s.user_id IN (:userIds))
              AND (
                (:semester IS NULL OR :diaryStatus IS NULL)
                    OR
                (:semester = 5 AND COALESCE(m5.diary_status, 'NONE') = :diaryStatus)
                    OR
                (:semester = 6 AND COALESCE(m6.diary_status, 'NONE') = :diaryStatus)
                    OR
                (:semester = 7 AND COALESCE(m7.diary_status, 'NONE') = :diaryStatus)
                    OR
                (:semester = 8 AND COALESCE(m8.diary_status, 'NONE') = :diaryStatus)
                )
              AND (
                (:semester IS NULL OR :mark IS NULL)
                    OR
                (:semester = 5 AND m5.mark = :mark)
                    OR
                (:semester = 6 AND m6.mark = :mark)
                    OR
                (:semester = 7 AND m7.mark = :mark)
                    OR
                (:semester = 8 AND m8.mark = :mark)
                )
                AND (:studentGroups IS NULL OR s.student_group IN (:studentGroups))
            ORDER BY
                CASE WHEN :sortByGroup = 'ASCENDING' THEN s.student_group ELSE NULL END ASC,
                CASE WHEN :sortByGroup = 'DESCENDING' THEN s.student_group ELSE NULL END DESC,
                CASE
                    WHEN :semester IS NOT NULL AND :diaryDoneFirst = true THEN
                        CASE
                            WHEN :semester = 5 THEN
                                CASE COALESCE(m5.diary_status, 'NONE')
                                    WHEN 'APPROVED' THEN 1
                                    WHEN 'PENDING' THEN 2
                                    WHEN 'REJECTED' THEN 3
                                    ELSE 4
                                    END
                            WHEN :semester = 6 THEN
                                CASE COALESCE(m6.diary_status, 'NONE')
                                    WHEN 'APPROVED' THEN 1
                                    WHEN 'PENDING' THEN 2
                                    WHEN 'REJECTED' THEN 3
                                    ELSE 4
                                    END
                            WHEN :semester = 7 THEN
                                CASE COALESCE(m7.diary_status, 'NONE')
                                    WHEN 'APPROVED' THEN 1
                                    WHEN 'PENDING' THEN 2
                                    WHEN 'REJECTED' THEN 3
                                    ELSE 4
                                    END
                            WHEN :semester = 8 THEN
                                CASE COALESCE(m8.diary_status, 'NONE')
                                    WHEN 'APPROVED' THEN 1
                                    WHEN 'PENDING' THEN 2
                                    WHEN 'REJECTED' THEN 3
                                    ELSE 4
                                    END
                            ELSE 0
                            END
                    WHEN :semester IS NOT NULL AND :diaryDoneFirst = false THEN
                        CASE
                            WHEN :semester = 5 THEN
                                CASE COALESCE(m5.diary_status, 'NONE')
                                    WHEN 'APPROVED' THEN 4
                                    WHEN 'PENDING' THEN 3
                                    WHEN 'REJECTED' THEN 2
                                    ELSE 1
                                    END
                            WHEN :semester = 6 THEN
                                CASE COALESCE(m6.diary_status, 'NONE')
                                    WHEN 'APPROVED' THEN 4
                                    WHEN 'PENDING' THEN 3
                                    WHEN 'REJECTED' THEN 2
                                    ELSE 1
                                    END
                            WHEN :semester = 7 THEN
                                CASE COALESCE(m7.diary_status, 'NONE')
                                    WHEN 'APPROVED' THEN 4
                                    WHEN 'PENDING' THEN 3
                                    WHEN 'REJECTED' THEN 2
                                    ELSE 1
                                    END
                            WHEN :semester = 8 THEN
                                CASE COALESCE(m8.diary_status, 'NONE')
                                    WHEN 'APPROVED' THEN 4
                                    WHEN 'PENDING' THEN 3
                                    WHEN 'REJECTED' THEN 2
                                    ELSE 1
                                    END
                            ELSE 0
                            END
                    ELSE 0
                    END,
                CASE WHEN :sortByGroup IS NULL THEN s.student_group ELSE NULL END ASC,
                s.user_id
        """,
        nativeQuery = true
    )
    fun getStudentsWithMarks(
        @Param("userIds") userIds: List<UUID>,
        @Param("semester") semester: Int?,
        @Param("diaryDoneFirst") diaryDoneFirst: Boolean?,
        @Param("diaryStatus") diaryStatus: String?,
        @Param("mark") mark: Int?,
        @Param("sortByGroup") sortByGroup: String?,
        @Param("studentGroups") groups: List<String>,
    ): List<StudentsMarksProjection>

}