package br.com.alura.javaawssqs;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import io.awspring.cloud.messaging.core.QueueMessageChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
@Service
public class MessageSender {
    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    private static final String QUEUE_NAME = "https://sqs.us-east-1.amazonaws.com/692820185333/app-poc-sqs-consumer-example";

    @Autowired
    private final AmazonSQSAsync amazonSqs;

    @Autowired
    public MessageSender(final AmazonSQSAsync amazonSQSAsync) {
        this.amazonSqs = amazonSQSAsync;
    }

    public boolean send(final String messagePayload) {
        MessageChannel messageChannel = new QueueMessageChannel(amazonSqs, QUEUE_NAME);

        Message<String> msg = MessageBuilder.withPayload(messagePayload)
                .setHeader("sender", "app1")
                .setHeaderIfAbsent("country", "AE")
                .build();

        long waitTimeoutMillis = 5000;
        boolean sentStatus = messageChannel.send(msg,waitTimeoutMillis);
        logger.info("message sent");
        return sentStatus;
    }

}
