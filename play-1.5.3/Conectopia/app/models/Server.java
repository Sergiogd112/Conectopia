package models;

import javax.persistence.Entity;
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
    public List<Member> members = new ArrayList<>();
    @OneToMany(mappedBy = "server")
    public List<Role> roles = new ArrayList<>();
    @OneToMany(mappedBy = "server")
    public List<Chat> chats = new ArrayList<>();

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
    public Boolean isOwner(User user) {
        for (Member member : members) {
            if (member.user.equals(user) && member.role.name.equals("Owner")) {
                return true;
            }
        }
        return false;
    }
    public Boolean UpdateServer(User user, String serverName, String serverDescription) {
        if (!isOwner(user)) {
            return false;
        }
        this.name = serverName;
        this.description = serverDescription;
        this.save();
        return true;
    }
    public Boolean DeleteServer(User user) {
        if (!isOwner(user)) {
            return false;
        }
        for (Member member : members) {
            member.user.members.remove(member);
            member.delete();
        }
        for (Role role : roles) {
            role.delete();
        }
        for (Chat chat : chats) {
            chat.delete();
        }
        this.delete();
        return true;
    }

    public Boolean addMember(User owner, User user, Role role) {
        if (!isOwner(owner)) {
            return false;
        }
        Member member = new Member();
        member.user = user;
        member.role = role;
        member.server = this;
        member.save();
        return true;
    }

}
