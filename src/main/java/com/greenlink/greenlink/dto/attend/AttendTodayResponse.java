package com.greenlink.greenlink.dto.attend;

import com.greenlink.greenlink.domain.attend.Attend;

import java.time.LocalDate;

public record AttendTodayResponse(
        Long attendId,
        LocalDate attendDate,
        Integer streakCount
) {

    public static AttendTodayResponse from(Attend attend) {
        return new AttendTodayResponse(
                attend.getId(),
                attend.getAttendDate(),
                attend.getStreakCount()
        );
    }
}