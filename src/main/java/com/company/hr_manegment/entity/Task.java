package com.company.hr_manegment.entity;

import com.company.hr_manegment.entity.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Timestamp deadline;
    private TaskStatus status = TaskStatus.YANGI;
    @ManyToMany
    private Set<User> users;
    @ManyToOne
    private User responsible;

}
