package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }
    public static void loginGet()
    {
        render();
    }
    public static void loginPost()
    {
        render();
    }
    public static void registerGet()
    {
        render();
    }
    public static void registerPost()
    {
        User u = User.find("byName").first();
        render();
    }

}