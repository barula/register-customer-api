package com.huawei.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
/**
 * Configuracion de Jackson con el setup del object mapper con las convenciones que utilizamos.
 * Formato de fechas: yyyy-MM-dd'T'HH:mm:ssZ
 * (Ej: 2020-01-01T00:00:00-0300 primero de Enero del 2020 a las cero horas, horario argentina)
 * No incluye campos nulos o vacios
 */
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // configuración del formato de fecha ISO 8601 time zone
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));

        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JacksonAnnotationIntrospector jacksonAnnotationIntrospector = new JacksonAnnotationIntrospector();
        mapper.getDeserializationConfig().withAppendedAnnotationIntrospector
                (jacksonAnnotationIntrospector);
        mapper.getSerializationConfig().withAppendedAnnotationIntrospector
                (jacksonAnnotationIntrospector);

        return mapper;
    }
}
