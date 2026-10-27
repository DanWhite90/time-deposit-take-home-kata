package org.ikigaidigital;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

/**
 * Shared setup for the integration tests. Every class using it gets the same configuration, so Spring caches a single
 * application context and Testcontainers starts a single PostgreSQL container for all of them.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest
@AutoConfigureMockMvc
@Import({PostgresTestcontainersConfiguration.class, TimeDepositTestData.class})
@TestConstructor(autowireMode = AutowireMode.ALL)
public @interface IntegrationTest {
}
