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

    public static void server(Long idserver) {
        User user = connected();
        Server server_res = Server.findById(idserver);
        System.out.println("Server ID: " + idserver);
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
        assert user != null;
        user.members.add(member);

        user.save();
        newserver.save();
        role.save();
        member.save();

        Dashboard.server(newserver.id);
    }

    public static void serverPut(Long idserver, String serverName, String serverDescription) {
        System.out.println("Server ID: " + idserver);
        System.out.println("Server Name: " + serverName);
        System.out.println("Server Description: " + serverDescription);
        User user = connected();
        if (user == null) {
            flash.error("Please log in first");
            Application.index();
        }
        Server server = Server.findById(idserver);
        if (server == null) {
            flash.error("Server not found");
            Dashboard.servers();
        }
        // check if the user is the owner of the server
        boolean isOwner = false;
        assert server != null;
        assert user != null;
        for (Member member : server.members) {
            if (member.user.id == user.id && member.role.name.equals("Owner")) {
                isOwner = true;
                break;
            }
        }
        if (!isOwner) {
            flash.error("You are not the owner of this server");
            Dashboard.server(server.id);
        }
        server.name = serverName;
        server.description = serverDescription;
        server.save();
        Dashboard.server(server.id);
    }


}
