package justpil.messagingredis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@SpringBootApplication
public class MessagingRedisApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessagingRedisApplication.class);

	@Bean
	public RedisMessageListenerContainer container(RedisConnectionFactory conn, MessageListenerAdapter listener) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(conn);
		container.addMessageListener(listener, new PatternTopic("chat"));
		return container;
	}

	@Bean
	public MessageListenerAdapter listener(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receive");
	}

	@Bean
	public Receiver receiver() {
		return new Receiver();
	}

	@Bean
	public StringRedisTemplate redis(RedisConnectionFactory factory) {
		return new StringRedisTemplate(factory);
	}

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext context = SpringApplication.run(MessagingRedisApplication.class, args);
		StringRedisTemplate template = context.getBean(StringRedisTemplate.class);
		Receiver receiver = context.getBean(Receiver.class);
		while(receiver.getCount() == 0) {
			LOGGER.info("Sending message...");
			template.convertAndSend("chat", "Hello from Redis!");
			Thread.sleep(500L);
		}
		System.exit(0);
	}
}
