package com.example.cooing.global.entity;

import com.example.cooing.global.enums.OAuthProvider;
import com.example.cooing.global.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "user_table")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;


    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    @Column(nullable = false)
    private String providerId;

    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
    private Role role; //USER(=Non Artist), ADMIN, ARTIST로 정의

    @JoinColumn(name = "user_id")
    @OneToMany(fetch = FetchType.EAGER)
    private List<Baby> babyList;


    @Builder
    public User(Long id, String email, Role role, String name, OAuthProvider oAuthProvider, String providerId) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.oAuthProvider = oAuthProvider;
        this.role = role;
        this.providerId = providerId;
    }

    public void updateRole(Role role){
        this.role = role;
    }


    public void addBaby(Baby baby) {
        babyList.add(baby);
    }

    @Override
    public ArrayList<GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
        auth.add(new SimpleGrantedAuthority(Role.USER.toString()));
        return auth;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}