package com.mballen.demoparkapi.config;
import org.springframework.context.annotation.Configuration;
import java.util.TimeZone;

@Configuration
public class SpringTimezoneConfig {


    public void timezoneConfig(){
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }
}
