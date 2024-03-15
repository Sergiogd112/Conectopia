package models;

import javax.persistence.Entity;
import play.db.jpa.Model;

import javax.persistence.ManyToOne;

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
