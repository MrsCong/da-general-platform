package com.dgp.kafka.core;

import com.dgp.common.cache.Cache;
import com.dgp.kafka.config.KafkaProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaOffsetManager {
    private static final Logger logger = LoggerFactory.getLogger(KafkaOffsetManager.class);
    @Autowired
    private Cache offsetCache;
    @Autowired
    KafkaProperties kafkaProperties;

    /**
     * Overwrite the offset for the topic in an external storage.
     *
     * @param topic        - Topic name.
     * @param partitionNum - Partition of the topic.
     * @param offset       - offset to be stored.
     */
    public void saveOffsetInExternalStore(final String groupId, final String topic, final int partitionNum,
                                          final long offset) {

        try {
            String hashKey = storageKey(groupId, topic, partitionNum);
            logger.info("---saveOffsetInExternalStore key:{} hashKey:{},value:{}", getKey(), hashKey, offset);
            offsetCache.put(getKey(), hashKey, String.valueOf(offset));
        } catch (Exception e) {
            logger.error("***saveOffsetInExternalStore:", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @return he last offset + 1 for the provided topic and partition.
     */
    public long readOffsetFromExternalStore(String groupId, String topic, int partionNum) {

        try {
            String key = storageKey(groupId, topic, partionNum);
            String offset = offsetCache.get(getKey(), key);
            logger.info("---readOffsetFromExternalStore hashKey:{},value:{}", key, offset);
            if (StringUtils.isBlank(offset)) {
                return -1L;
            } else {
                return Long.parseLong(offset) + 1;
            }
        } catch (Exception e) {
            logger.error("***saveOffsetInExternalStore:", e);
            throw new RuntimeException(e);
        }
    }

    private String getKey() {
        return kafkaProperties.getConsumer().getOffsetCachePrefix() + "BPM_KAFKA_OFFSET_CACHE";
    }

    private String storageKey(String groupId, String topic, int partition) {
        return groupId + "#" + topic + "#" + partition;
    }

}
