package com.dgp.kafka.core;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class KafkaMyPartition implements Partitioner {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMyPartition.class);

    @Override
    public void configure(Map<String, ?> configs) {

    }

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();

        if (key == null) {
            ThreadLocalRandom current = ThreadLocalRandom.current();
            return current.nextInt(numPartitions);
        } else {
            int partitionNum;
            try {
                partitionNum = Integer.parseInt((String) key);
            } catch (Exception e) {
                partitionNum = key.hashCode();
            }
            return Math.abs(partitionNum % numPartitions);
        }
    }

    @Override
    public void close() {
        logger.info("---close!");

    }

}