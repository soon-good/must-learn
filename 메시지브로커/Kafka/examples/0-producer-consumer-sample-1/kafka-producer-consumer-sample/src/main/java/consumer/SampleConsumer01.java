package consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleConsumer01 {
	private final static Logger logger = LoggerFactory.getLogger(SampleConsumer01.class);
	private final static String TOPIC_NAME = "test";
	private final static String BOOTSTRAP_SERVERS = "ec2-gosgjung-hotmail:9092";
	private final static String GROUP_ID = "test-group";

	public static void main(String [] args){
		Properties config = new Properties();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(config);
		consumer.subscribe(Arrays.asList(TOPIC_NAME));

		while(true){
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
			for(ConsumerRecord<String, String> record : records){
				logger.info("레코드 : {} ", record);
			}
		}

	}
}
