package com.easy.config;

import com.google.common.base.Preconditions;
import com.easy.config.annotations.RefreshClass;
import com.easy.config.annotations.RefreshField;
import com.easy.config.properties.ConfigProperties;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * {@link RefreshedListener} for config.
 *
 * @author uzdz
 * @date: 2019/3/16 14:49
 * @since 0.1.0
 */
@Component
public class RefreshedListener implements ApplicationListener<ContextRefreshedEvent>, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(RefreshedListener.class);

    @Qualifier("configCurator")
    @Autowired(required = false)
    private CuratorFramework curatorFramework;

    @Autowired(required = false)
    private ConfigProperties configProperties;

    private static final String DEFAULT_SPLIT_CHARACTER = "/";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Preconditions.checkNotNull(curatorFramework, "[config] sorry config curator framework can not be null.");

        Preconditions.checkNotNull(configProperties, "[config] sorry config properties can not be null.");

        if (StringUtils.isEmpty(configProperties.getName())) {
            throw new RuntimeException("[config] sorry config server name can not be empty.");
        }

        ApplicationContext applicationContext = event.getApplicationContext();

        if (applicationContext.getParent() == null) {

            Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RefreshClass.class);

            for (Object bean : beans.values()) {

                Field[] fields = bean.getClass().getDeclaredFields();

                for (Field field : fields) {

                    if(field.isAnnotationPresent(RefreshField.class)) {

                        try {
                            RefreshField annotationObj = field.getAnnotation(RefreshField.class);
                            String key = annotationObj.key();
                            String propKey = annotationObj.propKey();

                            // 获取运行时yml或properties的属性值作为默认值
                            String defValue = SpringContextUtil.getPropertiesValue(propKey);

                            defValue = defValue == null ? "" : defValue;

                            if (StringUtils.isEmpty(key)) {
                                throw new RuntimeException("[config] sorry field key can not be empty.");
                            }

                            String fullPath = "/" + configProperties.getName() + "/" + key;

                            if (!checkNodeExists(fullPath)) {
                                try {
                                    curatorFramework.create()
                                            .creatingParentsIfNeeded()
                                            .withMode(CreateMode.PERSISTENT)
                                            .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                                            .forPath(fullPath, defValue.getBytes());
                                } catch (KeeperException.NodeExistsException e) {
                                    logger.info("[config] " + fullPath + " node already exists");
                                }
                            }

                            field.set(bean, queryFiledValue(fullPath, defValue));

                            NodeCache nodeCache = new NodeCache(curatorFramework, fullPath);

                            nodeCache.start(true);

                            nodeCache.getListenable().addListener(() -> {
                                if (nodeCache.getCurrentData() != null) {
                                    field.set(bean, new String(nodeCache.getCurrentData().getData()));
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new RuntimeException("[config] auto configure error, " + e.getMessage());
                        }
                    }
                }
            }
        }
    }

    private String queryFiledValue(String fullPath, String defValue) {

        try {
            return new String(curatorFramework
                    .getData()
                    .forPath(fullPath));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[config] query value error, " + e.getMessage());
            return defValue;
        }
    }

    private boolean checkNodeExists(String fullPath) {

        if (!fullPath.startsWith(DEFAULT_SPLIT_CHARACTER)) {
            logger.error("[config] check node fail, because path must start with / character");
            return false;
        }

        try {
            Stat stat = curatorFramework
                    .checkExists()
                    .forPath(fullPath);

            return stat != null;
        } catch (Exception e) {
            logger.error("[config] check node fail, because " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}