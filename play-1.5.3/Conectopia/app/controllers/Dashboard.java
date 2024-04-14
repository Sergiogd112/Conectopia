package controllers;

import play.mvc.*;

import java.util.*;

import models.*;

public class Dashboard extends Application {
    @Before
    static void checkUser() {
        if (connected() == null) {
            flash.error("Please log in first");
            Application.index();
        }
    }

    public static void index() {
        User user = connected();
        render(user);
    }

    public static void servers() {
        User user = connected();
        render(user);
    }

    public static void server(Long id) {
        User user = connected();
        Server server_res = Server.findById(id);
        System.out.println("Server ID: " + id);
        System.out.println("Server Name: " + server_res.name);
        System.out.println("Server Description: " + server_res.description);
        renderArgs.put("server", server_res);
        render(user);
    }

    public static void serverPost(String serverName, String serverDescription) {
        System.out.println("Server Name: " + serverName);
        System.out.println("Server Description: " + serverDescription);
        User user = connected();
        if (user == null) {
            flash.error("Please log in first");
            Application.index();
        }
        Role role = new Role();
        Member member = new Member();
        Server newserver = new Server();
        newserver.name = serverName;
        newserver.description = serverDescription;
        newserver.created = new Date();

        role.name = "Owner";
        role.color = "magenta";

        member.user = user;
        member.role = role;
        member.server = newserver;

        newserver.members.add(member);
        newserver.roles.add(role);
        role.members.add(member);
        user.members.add(member);

        user.save();
        newserver.save();
        role.save();
        member.save();

        Dashboard.server(newserver.id);
    }
}
