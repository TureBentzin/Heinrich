package de.bentzin.heinrich;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Ture Bentzin
 * @since 26-03-2024
 */
public class ConfigObject {

    @NotNull
    private List<Long> adminIds;
    @NotNull
    private String faktsFile = "fakts.json";
    private long faktCacheTTL = 1000 * 60 * 10; //10min

    private boolean writeEnabled = true;

    @NotNull
    public static ConfigObject defaultConfig() {
        ConfigObject configObject = new ConfigObject();
        configObject.setAdminIds(List.of());
        return configObject;
    }

    public long getFaktCacheTTL() {
        return faktCacheTTL;
    }

    public void setFaktCacheTTL(long faktCacheTTL) {
        this.faktCacheTTL = faktCacheTTL;
    }

    @NotNull
    public String getFaktsFile() {
        return faktsFile;
    }

    public void setFaktsFile(@NotNull String faktsFile) {
        this.faktsFile = faktsFile;
    }

    @NotNull
    public List<Long> getAdminIds() {
        return adminIds;
    }

    public void setAdminIds(@NotNull List<Long> adminIds) {
        this.adminIds = adminIds;
    }

    public boolean isWriteEnabled() {
        return writeEnabled;
    }

    public void setWriteEnabled(boolean writeEnabled) {
        this.writeEnabled = writeEnabled;
    }
}
