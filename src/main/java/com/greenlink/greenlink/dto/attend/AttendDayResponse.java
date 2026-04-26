package com.greenlink.greenlink.dto.attend;

import com.greenlink.greenlink.domain.attend.Attend;

import java.time.LocalDate;

public record AttendDayResponse(
        LocalDate attendDate,
        Integer streakCount
) {

    public static AttendDayResponse from(Attend attend) {
        return new AttendDayResponse(
                attend.getAttendDate(),
                attend.getStreakCount()
        );
    }
}