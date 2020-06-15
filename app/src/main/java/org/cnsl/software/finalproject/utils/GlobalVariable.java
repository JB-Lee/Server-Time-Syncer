package org.cnsl.software.finalproject.utils;

public class GlobalVariable {
    private static String serverUrl;

    public static String getServerUrl() {
        return serverUrl;
    }

    public static void setServerUrl(String serverUrl) {
        GlobalVariable.serverUrl = serverUrl;
    }
}
