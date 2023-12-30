package fr.univrouen.deliverysystem;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = { "fr.univrouen.deliverysystem" })
// @EnableJpaRepositories(basePackages = { "fr.univrouen.deliverysystem*" })
// @EntityScan(basePackages = { "fr.univrouen.deliverysystem" })
public class TestConfig {

}
