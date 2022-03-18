package org.billing;

import org.billing.configuration.EnvironmentConfig;

import java.io.IOException;

public class MainClass {
    public static void main(String[] args) throws IOException {
        EnvironmentConfig environmentConfig = new EnvironmentConfig();
        environmentConfig.setDefaultPathsAndInfo();


        App.main(args);
    }
}
