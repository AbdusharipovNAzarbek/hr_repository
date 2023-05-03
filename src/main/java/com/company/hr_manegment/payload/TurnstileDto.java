package com.company.hr_manegment.payload;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class TurnstileDto {
    private UUID user_id;

    private Timestamp date_time;

}
