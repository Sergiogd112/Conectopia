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
        render();
    }

}