package models;

import org.hibernate.annotations.Entity;
import play.db.jpa.Model;

import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Message extends Model {
    public String content;
    public Date created;
    @ManyToOne
    public User user;
    @ManyToOne
    public Chat chat;

    public Message(String content, User user, Chat chat) {
        this.content = content;
        this.user = user;
        this.chat = chat;
        this.created = new Date();
    }
public Message() {
        this.content = "";
        this.user = null;
        this.chat = null;
        this.created = new Date();
    }
}
