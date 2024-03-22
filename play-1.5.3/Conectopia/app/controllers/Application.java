package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        render("Application/index.html");
    }

    public static void login() {

        render("Application/login.html");
    }

    public static void loginPost(String emailuname, String password) {
        // check if emailUname is email or username if it contains @ then it is email
        if (emailuname.contains("@")) {
            User u = User.find("byEmail", emailuname).first();
            if (u == null || u.password != password) {
                renderText("Usuario no encontrado");
            } else {
                renderText("Usuario encontrado");
            }
        } else {
            User u = User.find("byUsername", emailuname).first();
            if (u == null || u.password != password) {
                renderText("Usuario no encontrado");
            } else {
                renderText("Usuario encontrado");
            }
        }
    }

    public static void register() {

        render("Application/register.html");
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
            renderText("Usuario registrado");
        } else {
            renderText("Usuario ya existe");
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
}