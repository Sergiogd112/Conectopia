package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void login() {

        render();
    }

    public static void loginPost(String emailuname, String password) {
        // check if emailUname is email or username if it contains @ then it is email
        if (emailuname.contains("@")) {
            User u = User.find("byEmailAndPassword", emailuname, password).first();
            if (u == null) {
                renderText("Usuario no encontrado");
            } else {
                renderText("Usuario encontrado");
            }
        } else {
            User u = User.find("byNameAndPassword", emailuname, password).first();
            if (u == null) {
                renderText("Usuario no encontrado");
            } else {
                renderText("Usuario encontrado");
            }
        }
    }

    public static void register() {

        render();
    }

    public static void registerPost() {
        User u = User.find("byName").first();
        if (u == null) {
            u = new User();
            u.username = params.get("InputUname");
            u.email = params.get("InputEmail1");
            u.password = params.get("InputPassword1");
            if ((params.get("InputPassword1") != (params.get("InputPassword2")))){
                renderText("Las contraseñas no coinciden");
                return ;
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