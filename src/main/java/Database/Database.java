package Database;

import Model.Constants.Constant;
import Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;

import java.io.*;
import java.util.*;

public interface Database {

    //list map oqish yozish json un servislar
    Set<User> users = new LinkedHashSet<>();

    Map<String, List> map = new HashMap<>();


    public static void writeJson(String name) {
        File file = new File("src/main/resources/users.json");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(gson.toJson(map.get(name)));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readJson() {
        Gson gson = new Gson();
        File file = new File("src/main/resources/users.json");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            Set<User> userSet = gson.fromJson(reader, new TypeToken<Set<User>>() {
            }.getType());
            users.addAll(userSet);
            map.put(Constant.USER, Collections.singletonList(userSet));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static List<User> readerJson() {
        Gson gson = new Gson();
        File file = new File("src/main/resources/users.json");
            BufferedReader reader = new BufferedReader(new FileReader(file));

            List<User> users = gson.fromJson(reader, new TypeToken<List<User>>() {}.getType());
            return users;

    }
    @SneakyThrows
    public static List<User> writerJson(List<User> userList) {
        File file = new File("src/main/resources/users.json");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            writer.close();
        return userList;
    }
}
