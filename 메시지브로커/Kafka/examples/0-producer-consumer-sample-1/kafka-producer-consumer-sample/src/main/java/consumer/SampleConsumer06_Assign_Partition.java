package consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleConsumer06_Assign_Partition {
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
		config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);	// 자동(비명시) 커밋 옵션을 OFF 했다.

		int partitionNumber = 0;

		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(config);

		// 파티션 넘버를 지정한 특정 파티션들에 대한 정보를 가진 토픽들을 저장하기 위한 컬렉션 객체 생성
		Set<TopicPartition> topics = Collections.singleton(new TopicPartition(TOPIC_NAME, partitionNumber));

		// 파티션 넘버가 지정된 토픽들을 지정
		consumer.assign(topics);
		// consumer.subscribe(Arrays.asList(TOPIC_NAME));

		while(true){
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
			for(ConsumerRecord<String, String> record : records){
				logger.info("레코드 : {} ", record);
			}
			consumer.commitSync();	// 소비가 완료된 것에 대해 카프카 브로커에 소비했음을 통보하는 commitSync() 메서드를 호출한다.
		}

	}
}
