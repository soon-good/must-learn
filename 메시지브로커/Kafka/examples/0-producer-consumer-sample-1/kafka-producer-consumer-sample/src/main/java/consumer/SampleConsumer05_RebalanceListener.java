package consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleConsumer05_RebalanceListener {
	private final static Logger logger = LoggerFactory.getLogger(SampleConsumer01.class);
	private final static String TOPIC_NAME = "test";
	private final static String BOOTSTRAP_SERVERS = "ec2-gosgjung-hotmail:9092";
	private final static String GROUP_ID = "test-group";


	private static Map<TopicPartition, OffsetAndMetadata> currentOffset = new HashMap<>();
	private static KafkaConsumer<String, String> consumer;

	public static void main(String [] args){
		Properties config = new Properties();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

		// 자동 커밋(비명시 커밋) 옵션을 OFF 했다.
		config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

		consumer = new KafkaConsumer<String, String>(config);

		consumer.subscribe(Arrays.asList(TOPIC_NAME), new RebalanceListener());

		while(true){
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

			for(ConsumerRecord<String, String> record : records){
				logger.info("레코드 : {} ", record);
				currentOffset.put(
					new TopicPartition(record.topic(), record.partition()),
					new OffsetAndMetadata(record.offset() + 1, null)
				);
				consumer.commitSync(currentOffset);
			}
		}

	}
	private static class RebalanceListener implements ConsumerRebalanceListener {
		Logger logger = LoggerFactory.getLogger(ConsumerRebalanceListener.class);

		@Override
		public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
			logger.warn("파티션 회수 되었습니다.");
			consumer.commitSync(currentOffset);
		}

		@Override
		public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
			logger.warn("파티션이 할당완료되었습니다.");
		}
	}
}