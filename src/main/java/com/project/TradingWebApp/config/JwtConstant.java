package com.project.TradingWebApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConstant {

    /**
     * Secret key for signing JWT tokens.
     *
     * ⚠️ SECURITY WARNING:
     * - DO NOT hardcode the secret key in the source code.
     * - Use environment variables or an external configuration file.
     */

    public static final String SECRET_KEY = "kfjklasjdlfkasdfkjaskdjfklajsklfdjaslkdjfkljklajlkjsdaklfjkasldjflkasjdkfjklasjdkfljaskdlfjdflsjaslkdjlfkjkfjsdklfjaskdfksdjklfasjkdl";


    /**
     * Header key for passing JWT tokens in HTTP requests.
     */
    public static final String JWT_HEADER = "Authorization";
}
