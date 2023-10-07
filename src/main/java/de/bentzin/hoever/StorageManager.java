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
import java.util.logging.Logger;

/**
 * @author Ture Bentzin
 * @since 07-10-2023
 */
public class StorageManager {

    private final Logger logger = Logger.getLogger("Storage");

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
        logger.info("writing new data: " + data);
        writer.write(data);
        writer.close();
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
            if(hashMap == null) {
                //no data
                logger.info("Cant read data from json: " + data);
                return new HashMap<>();
            }
            return hashMap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
