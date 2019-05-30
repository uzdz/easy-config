package com.easy.config.selectors;

import com.easy.config.SpringContextUtil;
import com.easy.config.ZookeeperConfigApplication;
import com.easy.config.RefreshedListener;
import com.easy.config.annotations.EnableXyyConfig;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * {@link ConfigRegisterSelector} for config.
 *
 * @author uzdz
 * @date: 2019/3/16 14:31
 * @since 0.1.0
 */
public class ConfigRegisterSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(EnableXyyConfig.class.getName()));

        boolean startup = attributes.getBoolean("startup");

        String[] component = new String[3];
        if (startup) {
            component[0] = ZookeeperConfigApplication.class.getName();
            component[1] = RefreshedListener.class.getName();
            component[2] = SpringContextUtil.class.getName();
        }

        return component;
    }

}
