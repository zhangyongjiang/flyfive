package com.gaoshin.fbobuilder.client.resourcemanager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Registry {
	public static final String DEFAULT = "";
	String name() default DEFAULT;
}
