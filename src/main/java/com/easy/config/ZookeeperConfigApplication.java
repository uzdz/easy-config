package com.easy.config;

import com.easy.config.properties.ConfigProperties;
import com.easy.config.properties.ConfigZookeeperProperties;
import com.google.common.base.Preconditions;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * {@link ZookeeperConfigApplication} for config.
 *
 * @author uzdz
 * @date: 2019/3/16 14:28
 * @since 0.1.0
 */
@Configuration
@EnableConfigurationProperties({ConfigProperties.class, ConfigZookeeperProperties.class})
public class ZookeeperConfigApplication implements Ordered {

    private ConfigZookeeperProperties configZookeeperProperties;

    public ZookeeperConfigApplication(ConfigZookeeperProperties configZookeeperProperties) {
        this.configZookeeperProperties = configZookeeperProperties;
    }

    @Bean("configCurator")
    public CuratorFramework framework() {

        Preconditions.checkNotNull(configZookeeperProperties.getAddress(), "[config] sorry config zookeeper address can not be null.");
        Preconditions.checkNotNull(configZookeeperProperties.getRootPath(), "[config] sorry config zookeeper root path can not be null.");

        RetryNTimes retryNTimes = new RetryNTimes(5, 5000);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(configZookeeperProperties.getAddress())
                .retryPolicy(retryNTimes)
                .namespace(configZookeeperProperties.getRootPath())
                .build();

        client.start();

        return client;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
