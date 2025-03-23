package com.example.Task_management_system_test_task.tables;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@NoArgsConstructor
@Setter
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "value")
    private String value;

    @JoinColumn(name = "task_id")
    @ManyToOne
    private Task task;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;
}
