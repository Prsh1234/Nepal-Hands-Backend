package com.example.nepalhandsbackend.model;

import com.example.nepalhandsbackend.states.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Kyc kyc;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = true)
    private String password; // Nullable for Google users

    private String firstName;
    private String lastName;
    private String Bio;
    private String Location;

    @Lob
    @Column(name = "profilePic", columnDefinition = "LONGBLOB")
    private byte[] profilePic;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>(Set.of(Role.ROLE_VOLUNTEER)); // Default



    @ElementCollection
    @CollectionTable(
            name = "user_skills",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "skill")
    @Builder.Default
    private List<String> skills = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "user_causes",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "cause")
    @Builder.Default
    private List<String> causes = new ArrayList<>();
    @CreationTimestamp
    private LocalDateTime joinedAt;
    @CreationTimestamp
    private LocalDateTime updatedAt;
    // ─── Role helpers ─────────────────────────────────────────────────────────

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public boolean hasRole(Role role) {
        return this.roles.contains(role);
    }
}