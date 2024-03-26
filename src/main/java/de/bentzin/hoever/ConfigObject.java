package de.bentzin.hoever;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Ture Bentzin
 * @since 26-03-2024
 */
public class ConfigObject {

    @NotNull
    private String sqlitePath;
    @NotNull
    private List<Long> adminIds;

    public static ConfigObject defaultConfig() {
        ConfigObject configObject = new ConfigObject();
        configObject.setSqlitePath("data.sqlite");
        configObject.setAdminIds(List.of());
        return configObject;
    }

    @NotNull
    public String getSqlitePath() {
        return sqlitePath;
    }

    public void setSqlitePath(@NotNull String sqlitePath) {
        this.sqlitePath = sqlitePath;
    }

    @NotNull
    public List<Long> getAdminIds() {
        return adminIds;
    }

    public void setAdminIds(@NotNull List<Long> adminIds) {
        this.adminIds = adminIds;
    }
}
