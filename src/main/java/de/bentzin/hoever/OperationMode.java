package de.bentzin.hoever;

/**
 * @author Ture Bentzin
 * @since 31-10-2023
 */
public enum OperationMode {
    PRODUCTION,
    STAGING,
    TESTING;

    private boolean production;

    public boolean isProduction() {
        return this == PRODUCTION;
    }
}
