package com.company.hr_manegment.payload;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class TaskDto {

private String name;
private String description;
private Timestamp deadline;
private UUID executorId;


}
