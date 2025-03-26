package com.example.Task_management_system_test_task.tables;

import com.example.Task_management_system_test_task.enums.TaskPriorityEnum;
import com.example.Task_management_system_test_task.enums.TaskStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Table(name = "task")
@Entity
@NoArgsConstructor
@Setter
@Getter
@NamedEntityGraphs(value = {
        @NamedEntityGraph(
                name = "task.comments",
                attributeNodes = {
                        @NamedAttributeNode("comments"),
                        @NamedAttributeNode("creator"),
                        @NamedAttributeNode("implementer")
                })
})
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(value = EnumType.ORDINAL)
    private TaskStatusEnum status;

    @Column(name = "priority")
    @Enumerated(value = EnumType.ORDINAL)
    private TaskPriorityEnum priority;

    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @JoinColumn(name = "creator_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    @JoinColumn(name = "implementer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User implementer;
}
