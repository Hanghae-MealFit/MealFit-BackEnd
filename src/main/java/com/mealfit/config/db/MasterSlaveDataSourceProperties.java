package com.mealfit.config.db;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

@Getter
@Setter
@Profile("stresstest")
@ConfigurationProperties(prefix = "datasource")
public class MasterSlaveDataSourceProperties {

    private final Map<String, Slave> slave = new HashMap<>();

    private String url;
    private String username;
    private String password;

    @Getter
    @Setter
    public static class Slave {

        private String name;
        private String url;
        private String username;
        private String password;
    }

}
