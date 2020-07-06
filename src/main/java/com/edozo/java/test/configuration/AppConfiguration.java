package com.edozo.java.test.configuration;

import com.edozo.java.test.datastores.H2MapRepository;
import com.edozo.java.test.ports.MapRepository;
import com.edozo.java.test.services.MapService;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public MapRepository mapRepository(DataSource dataSource) {
        return new H2MapRepository(dataSource);
    }

    @Bean
    public MapService mapService(MapRepository mapRepository) {
        return new MapService(mapRepository);
    }

}
