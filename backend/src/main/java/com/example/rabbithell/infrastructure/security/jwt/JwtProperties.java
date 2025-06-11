package com.example.rabbithell.infrastructure.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@ConfigurationProperties(prefix = "spring.jwt")
@Getter
public class JwtProperties {

    private String secret;
    private final Token token = new Token();

    @Getter
    public static class Token {
        private String header;
        private String prefix;
        private final Access access = new Access();
        private final Refresh refresh = new Refresh();

        @Getter
        public static class Access {
            private long minute;
        }

        @Getter
        public static class Refresh {
            private long minute;
        }
    }
}
