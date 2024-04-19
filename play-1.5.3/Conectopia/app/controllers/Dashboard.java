package controllers;

import play.Logger;
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
    @Before
    static void logparams() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> entry : params.all().entrySet()) {
            sb.append(entry.getKey()).append(": ").append(Arrays.toString(entry.getValue())).append("|");
        }
        Logger.debug(sb.toString());
    }


    public static void index() {
        User user = connected();
        render( user);
    }

    public static void servers() {
        User user = connected();

        render( user);
    }

    public static void server(Long idServer) {
        User user = connected();

        assert  user!= null;
        if (idServer == null) {
            flash.error("Server ID is required");
            Dashboard.servers();
        } else {
            Server server_res = Server.findById(idServer);
            Logger.debug("Server ID: " + idServer+" Server: "+server_res.name);
            if (server_res == null) {
                flash.error("Server not found");
                Dashboard.servers();
            }
            assert server_res != null;

            renderArgs.put("server", server_res);
//            Role role = user.getRole(server_res);
//            if (role == null) {
//                flash.error("You are not a member of this server");
//                Dashboard.servers();
//            }
//            assert role != null;
//            List<Chat> chats = Chat.find("byServerAndRole", server_res,role).fetch();
            renderArgs.put("chats", server_res.chats);
            render(user);
        }
    }

    public static void serverPost(String serverName, String serverDescription) {
        User user = connected();

        assert  user!= null;
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

    public static void serverPut(Long idServer, String serverName, String serverDescription) {
        User user = connected();

        assert  user!= null;
        Server server = Server.findById(idServer);
        if (server == null) {
            flash.error("Server not found");
            Dashboard.servers();
        }
        // check if the user is the owner of the server
        assert server != null;
        if (server.UpdateServer(user, serverName, serverDescription)) {
            flash.success("Server updated");
        } else {
            flash.error("You are not the owner of the server");
        }

        Dashboard.server(server.id);
    }

    public static void serverDelete(Long idServer) {
        User user = connected();

        assert  user!= null;
        Server server = Server.findById(idServer);
        if (server == null) {
            flash.error("Server not found");
            Dashboard.servers();
        }
        // check if the user is the owner of the server
        assert server != null;

        if (server.DeleteServer(  user)) {
            flash.success("Server deleted");
        } else {
            flash.error("You are not the owner of the server");
        }
        Dashboard.servers();
    }

    public static void serverMembers(Long idServer) {
        User user = connected();

        System.out.println("Server ID: " + idServer);
        assert  user!= null;
        Server server = Server.findById(idServer);
        if (server == null) {
            flash.error("Server not found");
            Dashboard.servers();
        }
        // check if the user is the owner of the server
        assert server != null;
        renderArgs.put("server", server);
        // TODO: view for server members
        render(  user);
    }

    public static void serverMembersPost(Long idServer, String username, String roleName) {
        User user = connected();
        assert   user != null;
        Server server = Server.findById(idServer);
        if (server == null) {
            flash.error("Server not found");
            Dashboard.servers();
        }
        // check if the user is the owner of the server
        assert server != null;
        User userToAdd = User.find("byUsername", username).first();
        if (userToAdd == null) {
            flash.error("User not found");
            Dashboard.serverMembers(server.id);
        }
        Role role = Role.find("byName", roleName).first();
        if (role == null) {
            flash.error("Role not found");
            Dashboard.serverMembers(server.id);
        }
        if (server.addMember(user, userToAdd, role)) {
            flash.success("User added to server");
        } else {
            flash.error("You are not the owner of the server");
        }
        Dashboard.serverMembers(server.id);
    }

    public static void serverChatPost(Long idServer, String chatName, String chatDescription) {
        User user = connected();
        assert   user != null;
        Server server = Server.findById(idServer);
        if (server == null) {
            flash.error("Server not found");
            Dashboard.servers();
        }
        // check if the user is the owner of the server
        assert server != null;
        Chat chat = new Chat(chatName, chatDescription);
        chat.server = server;
        chat.save();
        Dashboard.server(server.id);
    }

}
