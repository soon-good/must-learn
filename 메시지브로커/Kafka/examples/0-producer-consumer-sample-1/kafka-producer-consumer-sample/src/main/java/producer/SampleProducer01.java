package producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * example01 : 킷값 없이 토픽만 지정해서 데이터를 전달하는 예제
 */
public class SampleProducer01 {
	private final static Logger logger = LoggerFactory.getLogger(SampleProducer01.class);
	private final static String TOPIC_NAME = "test";
	private final static String BOOTSTRAP_SERVERS = "ec2-gosgjung-hotmail:9092";

	public static void main(String [] args){
		Properties config = new Properties();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(config);

		String msgValue = "테스트 메시지";
		ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, msgValue);
		producer.send(record);
		logger.info("{}", record);

		producer.flush();
		producer.close();
	}
}
