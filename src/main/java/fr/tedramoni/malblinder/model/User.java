package fr.tedramoni.malblinder.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Ted on 22/03/2016.
 */

@XmlRootElement(name = "myinfo")
public class User {
    private String id;
    private String username;

    public User() {
    }

    public String getId() {
        return id;
    }

    @XmlElement(name = "user_id")
    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @XmlElement(name = "user_name")
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
