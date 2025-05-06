package com.aebong.store.annotation;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DataJpaTest
@ActiveProfiles("test")
@Tag("unit")
public @interface UnitTest {
}
