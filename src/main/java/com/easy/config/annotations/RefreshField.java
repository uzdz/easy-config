package com.easy.config.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * {@link RefreshField} for config.
 *
 * @author uzdz
 * @date: 2019/3/16 14:30
 * @since 0.1.0
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = java.lang.annotation.ElementType.FIELD)
@Documented
public @interface RefreshField {

    String key();

    String propKey();

}
