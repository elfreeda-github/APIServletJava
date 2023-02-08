package com.he.api;

import java.util.List;
import java.util.stream.Collectors;

public class JSON {
    public static String toJSONArray(List<User> users){
        String data = 
                users
                .stream()
                .map(user -> String.format("{\"id\":%d,"
                        + "\"firstName\":\"%s\","
                        + "\"lastName\":\"%s\","
                        + "\"city\":\"%s\"}",
                        user.getId(),user.getFirstName(),user.getLastName(),user.getCity()))
                .collect(Collectors.joining(","));
        return "[" + data +"]";
    }

    public static String toJSONObject(User user) {
        return String.format("{\"id\":%d,"
                        + "\"firstName\":\"%s\","
                        + "\"lastName\":\"%s\","
                        + "\"city\":\"%s\"}",
                        user.getId(),user.getFirstName(),user.getLastName(),user.getCity());
    }
}