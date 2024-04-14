package controllers;

import play.mvc.*;

import java.util.*;

import models.*;

public class Dashboard extends Application{
    @Before
    static void checkUser() {
        if(connected() == null) {
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
}
