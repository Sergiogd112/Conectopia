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
        assert user != null;
        List<Map<String, String>> servers = new ArrayList<>();

        List<Server> tempservers = Server.findAll();

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
}
