package com.onehana.server_ilogu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Child extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String birth;

    @OneToOne(mappedBy = "child")
    private User parent;

    private Child(String name, String birth, User parent) {
        this.name = name;
        this.birth = birth;
        this.parent = parent;
    }

    public static Child of(String name, String birth, User parent) {
        return new Child(
                name,
                birth,
                parent
        );
    }
}
