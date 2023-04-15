package ru.smmassistant.smmbackend.integration.integration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.smmassistant.smmbackend.SmmBackendApplicationTest;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = SmmBackendApplicationTest.class)
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = { OAuth2ClientAutoConfiguration.class})
@Transactional
public @interface IntegrationTest {

}
