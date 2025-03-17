package com.mockingbird.Springbootcafe;

import com.mockingbird.Springbootcafe.util.PortUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@EnableElasticsearchRepositories(basePackages = "com.mockingbird.Springbootcafe.es")
@EnableJpaRepositories(basePackages = {"com.mockingbird.Springbootcafe.dao", "com.mockingbird.Springbootcafe.pojo"})
public class Application {

    static {
        PortUtil.checkPort(6379,"Redis 服务端",true);
        PortUtil.checkPort(9300,"ElasticSearch 服务端",true);
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
