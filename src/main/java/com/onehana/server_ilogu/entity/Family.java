package com.onehana.server_ilogu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Family extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String familyName;

    @Column(unique = true)
    private String inviteCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "child_id", referencedColumnName = "id")
    private Child child;

    @OneToMany(mappedBy = "family")
    private List<User> members = new ArrayList<>();

    private Family(String familyName, String inviteCode, Child child) {
        this.familyName = familyName;
        this.inviteCode = inviteCode;
        this.child = child;
    }

    public static Family of(String familyName, Child child) {
        Random r = new Random();
        String random = String.valueOf(r.nextInt(900000) + 100000);
        return new Family(
                familyName,
                familyName + random,
                child
        );
    }
}
