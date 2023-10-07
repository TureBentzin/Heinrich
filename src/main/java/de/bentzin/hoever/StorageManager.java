package de.bentzin.hoever;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ture Bentzin
 * @since 07-10-2023
 */
public class StorageManager {
    @NotNull
    private final Gson gson = new Gson();
    private String data = "{}";

    public StorageManager() {
        File file = new File("data.json");
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() throws IOException {
        File file = new File("data.json");
        file.createNewFile();
        FileWriter writer = new FileWriter(file, false);
        writer.write(data);
    }

    public void parse(@NotNull Map<String, String> stringStringMap) {
        data = gson.toJson(stringStringMap);
    }

    @NotNull
    public Map<String, String> read() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("data.json")), StandardCharsets.UTF_8);
            data = content;
            HashMap<String, String> hashMap = gson.fromJson(data, HashMap.class);
            return hashMap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
