package models;

import org.hibernate.annotations.Entity;
import play.db.jpa.Model;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Member extends Model {
    @ManyToOne
    public Role role;

    @ManyToOne
    public User user;
    @ManyToOne
    public Server server;

    public Member(Role role, User user, Server server) {
        this.role = role;
        this.user = user;
        this.server = server;
    }

    public Member() {
        this.role = null;
        this.user = null;
        this.server = null;
    }
}
