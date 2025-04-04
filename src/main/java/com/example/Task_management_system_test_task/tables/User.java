package com.example.Task_management_system_test_task.tables;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "\"user\"")
@NoArgsConstructor
@Setter
@Getter
@NamedEntityGraphs(
        value = {
                @NamedEntityGraph(
                        name = "user.role",
                        attributeNodes = @NamedAttributeNode("role")
                ),
        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @JoinColumn(name = "role_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;
}
