package controllers;
import play.Logger;
import play.mvc.*;

import java.util.*;

import models.*;
public class Search extends Controller{
    @Before
    static void logparams() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> entry : params.all().entrySet()) {
            sb.append(entry.getKey()).append(": ").append(Arrays.toString(entry.getValue())).append("|");
        }
        Logger.debug(sb.toString());
    }
    public static void users(String query) {
        //fuzzy search for users
        List<User> users = User.find("byUsernameLike", "%" + query + "%").fetch();
        // generate a map of ids and useranames
        Map<Long, String> userMap = new HashMap<>();
        for (User user : users) {
            userMap.put(user.id, user.username);
        }
        renderJSON(userMap);
    }

}
