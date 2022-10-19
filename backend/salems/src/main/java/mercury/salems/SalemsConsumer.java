package mercury.salems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SalemsConsumer {
    private static final Logger consumerLogger = LoggerFactory.getLogger(SalemsConsumer.class);

    @KafkaListener(topics = "SaleTopic", groupId = "SaleID")
    public void readMessage(String message){
        consumerLogger.info(String.format("Sale Topic Message: %s", message));
    }
}
