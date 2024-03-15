package models;

import org.hibernate.annotations.Entity;
import play.db.jpa.Model;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Server extends Model {
    public String name;
    public String description;
    public Date created;
    @OneToMany(mappedBy = "server")
    public List<Member> members = new ArrayList<Member>();
    @OneToMany(mappedBy = "server")
    public List<Role> roles = new ArrayList<Role>();
    @OneToMany(mappedBy = "server")
    public List<Chat> chats = new ArrayList<Chat>();

    public Server(String name, String description) {
        this.name = name;
        this.description = description;
        this.created = new Date();


    }

    public Server() {
        this.name = "";
        this.description = "";
        this.created = new Date();
    }

}
