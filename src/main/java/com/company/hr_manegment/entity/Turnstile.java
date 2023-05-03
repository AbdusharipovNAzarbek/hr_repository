package com.company.hr_manegment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Turnstile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Timestamp date_time;
    @ManyToOne
    private User user;

    public Turnstile(Timestamp date_time, User user) {
        this.date_time = date_time;
        this.user = user;
    }
}
