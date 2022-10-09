package com.example.demo.Utente;

import javax.persistence.*;

@Entity(name = "Utente")
@Table(name = "utente", uniqueConstraints = {
        @UniqueConstraint(name = "utente_email_unique", columnNames = "email")
})
public class Utente {

    @Id
    @SequenceGenerator(
        name = "utente_sequence",
        sequenceName = "utente_sequence",
        allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "utente_sequence"
    )

    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "family_name", nullable = false, columnDefinition = "TEXT")
    private String surname;

    @Column(name = "nickname", nullable = false, columnDefinition = "TEXT")
    private String nickname;

    @Column(name = "email", nullable = false, columnDefinition = "TEXT")
    private String email;

    @Column(name = "logged_in", nullable = false)
    private boolean loggedIn;

    public Utente() {}

    public Utente(String name, String surname, String nickname, String email, boolean loggedIn) {
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.email = email;
        this.loggedIn = loggedIn;
    }

    public String getName() { return name; }

    public String getSurname() { return surname; }

    public String getNickname() { return nickname; }

    public String getEmail() { return email; }

    public boolean getLoggedIn() { return loggedIn; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLoggedIn(boolean loggedIn) { this.loggedIn = loggedIn; }

    @Override
    public String toString() {
        return "Utente{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", loggedIn='" + loggedIn + '\'' +
                '}';
    }
}
