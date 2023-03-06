package com.books;

import java.util.HashMap;

public class UserService {
    private HashMap<String,User> userMap = new HashMap<>();

    public UserService() {
        initialUsers();
    }

    /**
     * Add User to Library Records
     * @param user User to be added to records
     */
    public boolean addUser(User user){
        if (userMap.containsKey(user.getEmail())){
            return false;
        }else {
            userMap.put(user.getEmail(),user);
            return true;
        }
    }

    private void initialUsers(){
        userMap.put("nikhil@gmail.com",new User("Nikhil","nikhil@gmail.com"));
        userMap.put("akshay@gmail.com",new User("Akshay","akshay@gmail.com"));
        userMap.put("gaurav@gmail.com",new User("Gaurav","gaurav@gmail.com"));
        userMap.put("mukesh@gmail.com",new User("Mukesh","mukesh@gmail.com"));
    }

    public User getUserByEmail(String email){
        return userMap.get(email);
    }
}
