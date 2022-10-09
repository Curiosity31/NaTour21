package com.example.natour.Utente;

import androidx.annotation.NonNull;

public class Utente {

    private Long id;
    private String name;
    private String surname;
    private String nickname;
    private String email;
    private boolean loggedIn;

    public Utente (String name, String surname, String nickname, String email, boolean loggedIn) {
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.email = email;
        this.loggedIn = loggedIn;
    }


    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getNickname() { return nickname; }

    public String getEmail() { return email; }

    public Long getId() {
        return id;
    }

    public boolean getLoggedIn() {
        return loggedIn;
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

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    @NonNull
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