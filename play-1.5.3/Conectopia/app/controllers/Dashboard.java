package controllers;

import play.Logger;
import play.mvc.*;
import helpers.*;

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
        render(user);
    }

    public static void servers() {
        User user = connected();

        render(user);
    }

    public static void server(Long idServer) {
        User user = connected();

        assert user != null;
        if (idServer == null) {
            flash.error("Server ID is required");
            Dashboard.servers();
        } else {
            Server server_res = Server.findById(idServer);
            Logger.debug("Server ID: " + idServer + " Server: " + server_res.name);
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

        assert user != null;
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

        assert user != null;
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

        assert user != null;
        Server server = Server.findById(idServer);
        if (server == null) {
            flash.error("Server not found");
            Dashboard.servers();
        }
        // check if the user is the owner of the server
        assert server != null;

        if (server.DeleteServer(user)) {
            flash.success("Server deleted");
        } else {
            flash.error("You are not the owner of the server");
        }
        Dashboard.servers();
    }

    public static void serverMembers(Long idServer) {
        User user = connected();

        System.out.println("Server ID: " + idServer);
        assert user != null;
        Server server = Server.findById(idServer);
        if (server == null) {
            flash.error("Server not found");
            Dashboard.servers();
        }
        // check if the user is the owner of the server
        assert server != null;
        renderArgs.put("server", server);
        // TODO: view for server members
        render(user);
    }

    public static void serverMembersPost(Long idServer, String username, String roleName) {
        User user = connected();
        assert user != null;
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
        assert user != null;
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

    public static void serverChat(Long idServer, Long idChat) {
        User user = connected();
        assert user != null;
        Server server = Server.findById(idServer);
        if (server == null) {
            flash.error("Server not found");
            Dashboard.servers();
        }
        // check if the user is the owner of the server
        assert server != null;
        Chat chat = Chat.findById(idChat);
        if (chat == null) {
            flash.error("Chat not found");
            Dashboard.server(server.id);
        }
        renderArgs.put("chat", chat);
        renderArgs.put("server", server);
        renderArgs.put("user", user);
        renderTemplate("Dashboard/chat.html");
    }

    public static void serverChatMessagePost(Long idServer, Long idChat, String messageContent) {
        User user = connected();
        assert user != null;
        Server server = Server.findById(idServer);
        if (server == null) {
            flash.error("Server not found");
            Dashboard.servers();
        }
        // check if the user is the owner of the server
        assert server != null;
        Chat chat = Chat.findById(idChat);
        if (chat == null) {
            flash.error("Chat not found");
            Dashboard.server(server.id);
        }
        Message newMessage = new Message(messageContent, user, chat);

        chat.messages.add(newMessage);
        user.messages.add(newMessage);
        chat.save();
        user.save();
        newMessage.save();
        Dashboard.serverChat(server.id, chat.id);
    }
    public static void invitePost(Long userid, String servername){
        User user = connected();
        assert user != null;
        assert userid != null;
        User userToInvite = User.findById(userid);
        if (userToInvite == null) {
            flash.error("User not found");
            Dashboard.servers();
        }
        Server server = Server.find("byName", servername).first();
        if (server == null) {
            flash.error("Server not found");
            Dashboard.servers();
        }
        Role role =new Role();
        role.name = "Member";
        role.color = "blue";
        role.save();
        assert server != null;

        if (server.addMember(user, userToInvite, role)) {
            flash.success("User added to server");
        } else {
            flash.error("You are not the owner of the server");
        }
        Dashboard.server(server.id);
    }
    public static void profile() {
        User user = connected();
        if (user != null) {

            render();
        } else {
            // Redirige al usuario a la página de inicio de sesión si no está conectado
            login();
        }
    }
    public static void updateProfile(String newUsername, String confirmNewUsername, String newEmail, String confirmNewEmail, String newPassword, String confirmNewPassword) {
        User user = connected();
        if (user != null) {
            if (newUsername != null && newUsername.equals(confirmNewUsername)) {
                System.out.println("newUsername: " + newUsername);
                user.username = newUsername;
            }
            if (newEmail != null && newEmail.equals(confirmNewEmail)) {
                System.out.println("newEmail: " + newEmail);
                user.email = newEmail;
            }
            if (newPassword != null && newPassword.equals(confirmNewPassword)) {
                System.out.println("newPassword: " + newPassword);
                user.password = newPassword; // Asegúrate de encriptar la contraseña antes de guardarla
            }
            user.save();
            index(); // Redirige al usuario a la página de inicio después de actualizar el perfil
        }
    }

}
