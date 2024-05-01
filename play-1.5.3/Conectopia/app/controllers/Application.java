package controllers;

import play.Logger;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {
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
        if (connected() != null) {
            Dashboard.index();
        }
        renderTemplate("Application/index.html");
    }

    public static void login() {

        render("../views/Application/login.html");
    }

    public static void loginPost(String emailuname, String password) {
        // check if emailUname is email or username if it contains @ then it is email
        // print evrything
        System.out.println("emailuname: " + emailuname);
        System.out.println("password: " + password);
        if (emailuname.contains("@")) {
            User u = User.find("byEmail", emailuname).first();
            if (u == null || !Objects.equals(u.password, password)) {
                flash("error", "Usuario o contraseña incorrectos");
                Application.login();
            } else {
                session.put("user", u.username);
                Dashboard.index();
            }
        } else {
            User u = User.find("byUsername", emailuname).first();
            if (u == null || !(u.password.equals(password))) {
                flash("error", "Usuario o contraseña incorrectos");
                Application.login();
            } else {
                System.out.println("u: " + u.username);
                session.put("user", u.username);
                Dashboard.index();

            }
        }
    }

    public static void register() {

        render("../views/Application/register.html");
    }

    public static void registerPost(String username, String email, String password1, String password2) {
        User u = User.find("byUsername", username).first();
        // print evrything
        System.out.println("username: " + username);
        System.out.println("email: " + email);
        System.out.println("password1: " + password1);
        System.out.println("password2: " + password2);

        if (u == null) {
            u = new User();
            u.username = username;
            u.email = email;
            u.password = password1;
            if (!password1.equals(password2)) {
                renderText("Las contraseñas no coinciden");
                return;
            }
            u.save();
            session.put("user", u.username);
            Dashboard.index();
        } else {
            flash("error", "El usuario ya existe");
            Application.login();
        }
        render();
    }

    public static void ServerByUser(String username) {
        // get all the servers that a user is member off
        //Return a coma separated list of the server names

        User u = User.find("byName", username).first();
        List<Member> members = Member.find("byUser", u).fetch();
        String serverNames = "";
        for (Member m : members) {
            serverNames += m.server.name + ",";
        }
        renderText(serverNames);

    }

    public static void about() {
        int a = 1;
        renderTemplate("Application/about.html");
    }

    public static void logout() {
        session.clear();
        index();
    }
}