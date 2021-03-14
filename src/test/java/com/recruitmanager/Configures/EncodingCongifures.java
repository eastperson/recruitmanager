package com.recruitmanager.Configures;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@AutoConfigureMockMvc
@Import({
        EncodingCongifures.Encoder.class,
})
public @interface EncodingCongifures {

    class Encoder {
        @Bean
        MockMvcBuilderCustomizer utf8Config() {
            return builder ->
                    builder.addFilters(new CharacterEncodingFilter("UTF-8", true));
        }
    }
}
