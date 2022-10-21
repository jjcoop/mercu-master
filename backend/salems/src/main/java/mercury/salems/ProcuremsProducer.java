package mercury.salems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProcuremsProducer {
    private static final Logger producerLogger = LoggerFactory.getLogger(ProcuremsProducer.class);
    private KafkaTemplate<String, String> procureKafkaTemplate;

    public ProcuremsProducer(KafkaTemplate<String, String> KTemplate){
        this.procureKafkaTemplate = KTemplate;
    }

    public void writeMessage(String message){
        producerLogger.info(String.format("Sale Topic Message: %s", message));
        procureKafkaTemplate.send("SaleTopic", message);
    }
}
