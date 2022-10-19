package mercury.salems.interfaces;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class SaleTopicConfig {
    
    @Bean
    public NewTopic SaleTopic(){
        return TopicBuilder.name("Sale Topic")
            .partitions(10)
            .replicas(3)
            .build();
    }
}
