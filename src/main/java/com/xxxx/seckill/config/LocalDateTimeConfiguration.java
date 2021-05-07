package com.xxxx.seckill.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * 配置LocalDateTime 问题
 */

@Configuration
public class LocalDateTimeConfiguration {
    private String pattern = "yyyy-MM-dd hh:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(){
        return builder->{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

            //服务器接收前端的数据并反序列化为 LocalDateTime
            builder.serializerByType(LocalDateTime.class,new LocalDateTimeSerializer(formatter));
            //服务端将LocalDateTime序列化为流返回给前端
            builder.deserializerByType(LocalDateTime.class,new LocalDateTimeDeserializer(formatter));
        };
    }
}
