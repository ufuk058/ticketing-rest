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
@AllArgsConstructor
@NoArgsConstructor
@Table(name="projects")
@Where(clause = "is_deleted=false")
public class Project extends BaseEntity{

    private String projectName;

    @Column(unique = true)
    private String projectCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User assignedManager;

    @Column(columnDefinition = "DATE")
    private LocalDate startDate;

    @Column(columnDefinition = "DATE")
    private LocalDate endDate;

    private String projectDetail;

    @Enumerated(EnumType.STRING)
    private Status projectStatus;
}
