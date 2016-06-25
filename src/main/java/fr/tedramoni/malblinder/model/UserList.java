package fr.tedramoni.malblinder.model;

import java.util.List;

/**
 * Created by Ted on 25/06/2016.
 */
public class UserList {

    private List<User> users;

    public UserList() {
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public User getUser(int index) {
        return this.users.get(index);
    }

    public void setUser(int index, User user) {
        users.set(index, user);
    }

    @Override
    public String toString() {
        return "UserList{" +
                "users=" + users +
                '}';
    }
}
