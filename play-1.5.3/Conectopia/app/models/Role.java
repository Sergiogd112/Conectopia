package models;

import javax.persistence.Entity;
import play.db.jpa.Model;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Role extends Model {
    public String name;
    public String color;
    @OneToMany(mappedBy = "role")
    public List<Member> members = new ArrayList<>();
    @ManyToOne
    public Server server;
    @ManyToMany
    public List<Chat> chats = new ArrayList<>();

    public Role(String name, String color, Server server) {
        this.name = name;
        this.color = color;
        this.server = server;
    }

    public Role() {
        this.name = "";
        this.color = "";
        this.server = null;
    }
}
