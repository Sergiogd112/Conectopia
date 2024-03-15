package models;

import javax.persistence.Entity;
import play.db.jpa.Model;

import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Badge extends Model {
    public String name;
    public String description;
    public String image;
    //timestamp
    public Date created;
    @ManyToOne
    public User user;

    public Badge(String name, String description, String image, User user) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.user = user;
        this.created = new Date();
    }

    public Badge() {
        this.name = "";
        this.description = "";
        this.image = "";
        this.user = null;
        this.created = new Date();
    }
}
