package com.dodlu.core.app.configuration;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@ConditionalOnProperty(prefix = "email.provider", name = "name", havingValue = "mail-trap")
@Configuration
@ConfigurationProperties(prefix = "email.provider.mailtrap")
@Data
public class MailTrapConfiguration {
  private String host;
  private String port;
  private String username;
  private String password;
  private String templatePath;
  private String ssl;
  private String tls;

  @Primary
  @Bean
  public FreeMarkerConfigurationFactoryBean factoryBean() {
    FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
    bean.setTemplateLoaderPath(templatePath);
    return bean;
  }

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(host);
    mailSender.setPort(
        Integer.parseInt(port)); // Example port, change as per your SMTP server configuration
    mailSender.setUsername(username);
    mailSender.setPassword(password);
    return mailSender;
  }
}
