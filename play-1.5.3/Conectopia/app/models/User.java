package models;

import org.hibernate.annotations.Entity;
import play.db.jpa.Model;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class User extends Model {
    public String username;
    public String email;
    public String password;
    public Date created;
    @OneToMany
    public List<Badge> badges= new ArrayList<Badge>();
    @OneToMany
    public List<Member> members= new ArrayList<Member>();
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.created = new Date();
    }
    public User() {
        this.username = "";
        this.email = "";
        this.password = "";
        this.created = new Date();
    }


}
