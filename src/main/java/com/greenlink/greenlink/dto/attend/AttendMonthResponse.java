package com.greenlink.greenlink.dto.attend;

import com.greenlink.greenlink.domain.attend.Attend;

import java.util.List;

public record AttendMonthResponse(
        Integer year,
        Integer month,
        Integer totalAttendCount,
        Integer currentStreakCount,
        List<AttendDayResponse> attends
) {

    public static AttendMonthResponse of(
            Integer year,
            Integer month,
            List<Attend> attends,
            Integer currentStreakCount
    ) {
        return new AttendMonthResponse(
                year,
                month,
                attends.size(),
                currentStreakCount,
                attends.stream()
                        .map(AttendDayResponse::from)
                        .toList()
        );
    }
}