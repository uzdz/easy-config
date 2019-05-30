package com.easy.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigProperties} for config.
 *
 * @author uzdz
 * @date: 2019/3/16 14:39
 * @since 0.1.0
 */
@ConfigurationProperties(prefix = "config.server", ignoreUnknownFields = true)
public class ConfigProperties {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ConfigProperties{" +
                "name='" + name + '\'' +
                '}';
    }
}
