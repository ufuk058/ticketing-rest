package com.rest.ticketing_rest.entity;

import com.rest.ticketing_rest.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tasks")
@Where(clause = "is_deleted=false")
public class Task extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private User assignedEmployee;

    private String taskSubject;

    private String taskDetail;

    @Column(columnDefinition = "DATE")
    private LocalDate assignedDate;

    @Enumerated(EnumType.STRING)
    private Status taskStatus;
}
