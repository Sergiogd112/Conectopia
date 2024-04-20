package controllers;

import play.Logger;
import play.mvc.*;

import java.util.*;

import models.*;

public class Search extends Controller {
    @Before
    static void logparams() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> entry : params.all().entrySet()) {
            sb.append(entry.getKey()).append(": ").append(Arrays.toString(entry.getValue())).append("|");
        }
        Logger.debug(sb.toString());
    }

    public static void user(String query, String excludeServer) {
        // Fetch all users whose username matches the query
        List<User> users = User.find("byUsernameLike", "%" + query + "%").fetch();
        Logger.debug("Users: " + users);
        // Fetch the server by its name
        Server server = Server.find("byName", excludeServer).first();

        // If the server exists, fetch its members and remove them from the users list
        if (server != null) {
            List<User> serverMembers = server.getUsers();
            users.removeAll(serverMembers);
        }

        // Generate a map of ids and usernames
        Map<Long, String> userMap = new HashMap<>();
        for (User user : users) {
            userMap.put(user.id, user.username);
        }

        renderJSON(userMap);
    }

}
