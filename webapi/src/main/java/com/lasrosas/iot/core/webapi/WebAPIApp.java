package com.lasrosas.iot.core.webapi;
import java.util.List;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.lasrosas.iot.core.database.IOTDatabaseConfig;

// @Profile("dev")
// @EnableAutoConfiguration
@SpringBootApplication
// @ComponentScan({ "com.lasrosas.iot" })
@Import({IOTDatabaseConfig.class, WebAPIConfig.class})
public class WebAPIApp implements WebMvcConfigurer {

	public static void main(String[] args) {
        new SpringApplicationBuilder(WebAPIApp.class)
        .web(WebApplicationType.SERVLET)
        .run(args);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof org.springframework.http.converter.json.MappingJackson2HttpMessageConverter) {
                ObjectMapper mapper = ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
                mapper.registerModule(new Hibernate5Module());
            }
        }
    }
}