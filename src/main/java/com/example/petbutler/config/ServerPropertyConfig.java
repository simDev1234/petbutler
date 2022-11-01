package com.example.petbutler.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/* 이메일 인증 시 서버 주소 필요하여 별도로 서버 설정 */
/* rf. https://www.baeldung.com/spring-boot-configuration-metadata */
@Configuration
@ConfigurationProperties(prefix = "server")
@Getter
@Setter
public class ServerPropertyConfig {

  private String ip = "127.0.0.1";

  private int port = 8080;

  private String address = String.format("http://%s:%d", ip, port);

  private String petThumbnailLocalRoot = "C:\\sebinSample\\petbutler\\src\\main\\resources\\static\\files";

  private String petThumbnailUrlRoot = "/files";

}
