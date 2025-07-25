package com.spring.user_service.utils;

import org.springframework.test.context.jdbc.Sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Sql(scripts = "/sql/clean_profile.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public @interface CleanProfileAfterTest {}