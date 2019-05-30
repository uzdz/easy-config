package com.easy.config.annotations;

import com.easy.config.selectors.ConfigRegisterSelector;
import org.springframework.context.annotation.Import;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * {@link EnableXyyConfig} for config.
 *
 * @author uzdz
 * @date: 2019/3/16 14:30
 * @since 0.1.0
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = java.lang.annotation.ElementType.TYPE)
@Documented
@Import(ConfigRegisterSelector.class)
public @interface EnableXyyConfig {

    /**
     * 启动config配置中心中间件
     * @return the default value is false
     */
    boolean startup() default true;
}
