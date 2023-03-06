package com.books;

/**
 * This is a User entity used to store User Details *  like username, user-email and user status
 */
public class User {

    private static final String VALIDATE_NAME = "^[a-zA-Z ]*$";
    private static final String VALIDATE_EMAIL = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String INVALID_NAME = "Invalid Name";
    private static final String INVALID_EMAIL = "Invalid Email";

    /**
     * userName should be valid Name
     * which can be displayed everywhere
     */
    private String name;
    /**
     * userEmail should be valid email
     */
    private String email;

    /**
     * Indicates if user is active or not
     */
    private UserStatus userStatus;

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        if (!name.matches(VALIDATE_NAME) || name==null){
            throw new IllegalArgumentException(INVALID_NAME);
        }

        this.email = email;
        if (!email.matches(VALIDATE_EMAIL) || email==null){
            throw new IllegalArgumentException(INVALID_EMAIL);
        }

        this.userStatus = UserStatus.AVAILABLE;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userStatus=" + userStatus +
                '}';
    }
}
