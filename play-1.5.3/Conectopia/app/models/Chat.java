package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Chat extends Model {
    public String name;
    public String description;
    public Date created;
    @OneToMany(mappedBy = "chat")
    public List<Message> messages = new ArrayList<>();

    @ManyToOne
    public Server server;

    public Chat(String name, String description) {
        this.name = name;
        this.description = description;
        this.created = new Date();
    }

    public Chat() {
        this.name = "";
        this.description = "";
        this.created = new Date();
    }
}
