package com.technoride.server.databaseupgrade.configuration;


import com.technoride.server.databaseupgrade.loader.Loader;
import com.technoride.server.databaseupgrade.utils.EncodeDecodeMode;
import com.technoride.server.databaseupgrade.utils.MySQLProcess;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicConfiguration {

    @Bean
    public Loader loader()
    {
        return new Loader();
    }

    @Bean
    public EncodeDecodeMode encodeDecodeMode()
    {
        return new EncodeDecodeMode();
    }

    @Bean
    public MySQLProcess mySQLProcess()
    {
        return new MySQLProcess();
    }
}
