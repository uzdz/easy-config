package com.easy.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigZookeeperProperties} for config.
 *
 * @author uzdz
 * @date: 2019/3/16 14:39
 * @since 0.1.0
 */
@ConfigurationProperties(prefix = "config.zookeeper", ignoreUnknownFields = true)
public class ConfigZookeeperProperties {

    public static final String DEFAULT_ROOT_PATH = "config";

    private String address;

    private String rootPath = DEFAULT_ROOT_PATH;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String toString() {
        return "ConfigZookeeperProperties{" +
                "address='" + address + '\'' +
                ", rootPath='" + rootPath + '\'' +
                '}';
    }
}
