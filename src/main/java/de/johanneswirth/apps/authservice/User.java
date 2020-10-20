package de.johanneswirth.apps.authservice;

import java.beans.ConstructorProperties;

public class User {
    public long id;
    public String username;

    @ConstructorProperties({"id", "username"})
    public User(long id, String username) {
        this.id = id;
        this.username = username;
    }
}
