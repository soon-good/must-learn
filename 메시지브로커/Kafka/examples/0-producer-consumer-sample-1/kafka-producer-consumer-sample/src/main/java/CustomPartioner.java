import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

public class CustomPartioner implements Partitioner {
	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		if(keyBytes == null){
			throw new InvalidRecordException("Need Message Key");
		}

		// 메시지의 키가 "샘플4-메시지" 일 경우 0번 파티션으로 지정되도록 0을 리턴
		if(((String)key).equals("샘플4-메시지")){
			return 0;
		}

		List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
		int numPartitions = partitions.size();
		return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions;
	}

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> configs) {

	}
}
