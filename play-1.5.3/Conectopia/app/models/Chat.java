package models;

import org.hibernate.annotations.Entity;
import play.db.jpa.Model;

import javax.persistence.OneToMany;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Chat extends Model {
    public String name;
    public String description;
    public Date created;
    @OneToMany
    public List<Message> messages= new ArrayList<Message>();
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
