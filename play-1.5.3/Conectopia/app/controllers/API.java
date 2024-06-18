package controllers;

import play.mvc.Controller;
import play.Logger;
import play.mvc.*;

import java.util.*;

import models.*;

import static controllers.Application.connected;

public class API extends Controller {
    // This class returns json objects instead of rendering templates
    @Before
    static void addUser() {
        User user = connected();
        if (user != null) {
            renderArgs.put("user", user);
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

    public static User connected() {
        if (renderArgs.get("user") != null) {
            return renderArgs.get("user", User.class);
        }
        String username = session.get("user");
        if (username != null) {
            return User.find("byUsername", username).first();
        }
        return null;
    }

    public static void index() {
        renderJSON("{\"error\": \"No autorizado\"}");
    }

    public static void servers() {
        User user = connected();

        List<Map<String, String>> servers = new ArrayList<>();

        List<Server> tempservers;
        if (user != null) {
            System.out.println("User: " + user.username);
            // get the servers where the user is a member
            List<Member> members = Member.find("byUser", user).fetch();
            tempservers = new ArrayList<>();
            for (Member member : members) {
                tempservers.add(member.server);
            }
        } else {
            tempservers = Server.findAll();
        }

        // Generate a map of server names and descriptions
        for (Server server : tempservers) {
            Map<String, String> serverMap = new HashMap<>();
            serverMap.put("id", server.id.toString());
            serverMap.put("description", server.description);
            serverMap.put("name", server.name);
            servers.add(serverMap);
        }
        renderJSON(servers);
    }


    public static void login(String emailuname, String password) {
        // check if emailUname is email or username if it contains @ then it is email
        // print evrything
        System.out.println("emailuname: " + emailuname);
        System.out.println("password: " + password);
        if (emailuname.contains("@")) {
            User u = User.find("byEmail", emailuname).first();
            if (u == null || !Objects.equals(u.password, password)) {
                renderJSON("{\"error\": \"Usuario o contraseña incorrectos\"}");
            } else {
                session.put("user", u.username);

                renderJSON("{\"success\": \"Usuario logueado\"}");
            }
        } else {
            User u = User.find("byUsername", emailuname).first();
            if (u == null || !Objects.equals(u.password, password)) {
                renderJSON("{\"error\": \"Usuario o contraseña incorrectos\"}");
            } else {
                session.put("user", u.username);
                renderJSON("{\"success\": \"Usuario logueado\"}");
            }
        }
    }


    public static void logout() {
        session.clear();
        renderJSON("{\"success\": \"Usuario deslogueado\"}");
    }

    public static void register(String email, String username, String password) {
        User u = User.find("byEmail", email).first();
        if (u != null) {
            renderJSON("{\"error\": \"Email ya registrado\"}");
        }
        u = User.find("byUsername", username).first();
        if (u != null) {
            renderJSON("{\"error\": \"Username ya registrado\"}");
        }
        u = new User(email, username, password);
        u.save();
        session.put("user", u.username);
        renderJSON("{\"success\": \"Usuario registrado\"}");
    }

    public static void server(Long idServer) {
        Server server_res = Server.findById(idServer);
        if (server_res == null) {
            renderJSON("{\"error\": \"Server no encontrado\"}");
        }
        /*
        Generate a json with this format:
        {
            "name": "Server_Name",
            "description": "Server_Description",
            "id": "Server_ID",
            "members": [
                "member1",
                "member2",
                "member3"
            ],
            "chats": [
                {
                    "name": "Chat_Name",
                    "description": "Chat_Description",
                    "id": "Chat_ID"
                }
            ]
        }
        */
        Map<String, Object> server = new HashMap<>();
        server.put("name", server_res.name);
        server.put("description", server_res.description);
        server.put("id", server_res.id);
        List<String> members = new ArrayList<>();
        for (Member member : server_res.members) {
            members.add(member.user.username);
        }
        server.put("members", members);
        List<Map<String, String>> chats = new ArrayList<>();
        for (Chat chat : server_res.chats) {
            Map<String, String> chatMap = new HashMap<>();
            chatMap.put("name", chat.name);
            chatMap.put("description", chat.description);
            chatMap.put("id", chat.id.toString());
            chats.add(chatMap);
        }
        server.put("chats", chats);
        renderJSON(server);
    }

    public static void createServer(String serverName, String serverDescription) {
        User user = connected();
        if (user == null) {
            renderJSON("{\"error\": \"Usuario no logueado\"}");
        }
        if (serverName == null || serverDescription == null) {
            renderJSON("{\"error\": \"Nombre y descripción del servidor son requeridos\"}");
        }
        if (Server.find("byName", serverName).first() != null) {
            renderJSON("{\"error\": \"Nombre de servidor ya existe\"}");
        }
        Server newserver = new Server();
        newserver.name = serverName;
        newserver.description = serverDescription;
        newserver.save();

        Role role = new Role();
        role.name = "Owner";
        role.color = "magenta";

        Member member = new Member();
        member.user = user;
        member.role = role;
        member.server = newserver;

        newserver.members.add(member);
        newserver.roles.add(role);
        role.members.add(member);
        user.members.add(member);
        newserver.save();
        role.save();
        member.save();
        server(newserver.id);
    }

    public static void chat(Long idChat) {
        Chat chat_res = Chat.findById(idChat);
        if (chat_res == null) {
            renderJSON("{\"error\": \"Chat no encontrado\"}");
        }
        /*
        Generate a json with this format:
        {
            "name": "Chat_Name",
            "description": "Chat_Description",
            "id": "Chat_ID",
            "messages": [
                {
                    "content": "Message_Content",
                    "date": "Message_Date",
                    "user": {
                        "username": "User_Username",
                        "role": "User_Role"
                    }
                }
            ]
        }
        */
        Map<String, Object> chat = new HashMap<>();
        chat.put("name", chat_res.name);
        chat.put("description", chat_res.description);
        chat.put("id", chat_res.id);
        List<Map<String, Object>> messages = new ArrayList<>();
        for (Message message : chat_res.messages) {
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("content", message.content);
            messageMap.put("date", message.created.toString());
            Map<String, String> userMap = new HashMap<>();
            userMap.put("username", message.user.username);
            userMap.put("role", message.user.getRole(chat_res.server).name);
            messageMap.put("user", userMap);
            messages.add(messageMap);
        }
        chat.put("messages", messages);

        renderJSON(chat);
    }

    public static void createChat(Long idServer, String chatName, String chatDescription) {
        User user = connected();
        if (user == null) {
            renderJSON("{\"error\": \"Usuario no logueado\"}");
        }
        Server server_res = Server.findById(idServer);
        if (server_res == null) {
            renderJSON("{\"error\": \"Server no encontrado\"}");
        }
        Chat chat = new Chat(chatName, chatDescription);
        chat.server = server_res;
        chat.save();
        server_res.chats.add(chat);
        server_res.save();
        chat(chat.id);
    }

    public static void createMessage(Long idChat, String content) {
        User user = connected();
        if (user == null) {
            renderJSON("{\"error\": \"Usuario no logueado\"}");
        }
        Chat chat_res = Chat.findById(idChat);
        if (chat_res == null) {
            renderJSON("{\"error\": \"Chat no encontrado\"}");
        }
        Message message = new Message();
        message.user = user;
        message.chat = chat_res;
        message.content = content;
        message.save();
        chat_res.messages.add(message);
        chat_res.save();
        chat(chat_res.id);
    }

    public static void user() {
        User user = connected();
        if (user == null) {
            renderJSON("{\"error\": \"Usuario no logueado\"}");
        }
        /*
        Generate a json with this format:
        {
            "username": "User_Username",
            "email": "User_Email",
            "servers": [
                {
                    "name": "Server_Name",
                    "description": "Server_Description",
                    "id": "Server_ID"
                }
            ]
        }
        */
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", user.username);
        userMap.put("email", user.email);
        List<Map<String, Object>> servers = new ArrayList<>();
        for (Member member : user.members) {
            Map<String, Object> serverMap = new HashMap<>();
            serverMap.put("name", member.server.name);
            serverMap.put("description", member.server.description);
            serverMap.put("id", member.server.id);
            servers.add(serverMap);
        }
        userMap.put("servers", servers);
        renderJSON(userMap);
    }

    public static void updateUser(String newUsername, String confirmNewUsername,
                                   String newEmail, String confirmNewEmail,
                                   String oldPassword, String newPassword, String confirmNewPassword) {
        User user = connected();
        if (user == null) {
            renderJSON("{\"error\": \"Usuario no logueado\"}");
        }
        if (newUsername != null && confirmNewUsername != null) {
            if (!newUsername.equals(confirmNewUsername)) {
                renderJSON("{\"error\": \"Los nuevos nombres de usuario no coinciden\"}");
            }
            user.username = newUsername;
            user.save();
        }
        if (newEmail != null && confirmNewEmail != null) {
            if (!newEmail.equals(confirmNewEmail)) {
                renderJSON("{\"error\": \"Los nuevos correos no coinciden\"}");
            }
            user.email = newEmail;
            user.save();
        }
        if (oldPassword != null && newPassword != null && confirmNewPassword != null) {
            if (!newPassword.equals(confirmNewPassword)) {
                renderJSON("{\"error\": \"Las nuevas contraseñas no coinciden\"}");
            }
            if (!user.password.equals(oldPassword)) {
                renderJSON("{\"error\": \"Contraseña incorrecta\"}");
            }
            user.password = newPassword;
            user.save();
        }
        renderText("{\"success\": \"Usuario actualizado\"}");
    }

}
