import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 예제 5) 콜백을 이용해 데이터 전송 성공 여부를 리턴받기
 */
public class SampleProducer05 {
	private final static Logger logger = LoggerFactory.getLogger(SampleProducer01.class);
	private final static String TOPIC_NAME = "test";
	private final static String BOOTSTRAP_SERVERS = "ec2-gosgjung-hotmail:9092";

	public static void main(String [] args) {
		Properties config = new Properties();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(config);

		String msgKey = "샘플5-메시지";
		String msgValue = "테스트 메시지";

		ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, msgKey, msgValue);
		producer.send(record, new ProducerCallback());

		// try {
		// 	RecordMetadata recordMetadata = producer.send(record).get();
		// 	logger.info("{}", recordMetadata.toString());
		// } catch (InterruptedException | ExecutionException e) {
		// 	e.printStackTrace();
		// }

		producer.flush();
		producer.close();
	}
}
