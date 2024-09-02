package com.dgp.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
    private boolean enable = false;
    private boolean rebalanced = true;
    private Consumer consumer = new Consumer();
    private Producer producer = new Producer();

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public static class Consumer {
        private String fetchMinSize;
        private String offsetCachePrefix = "";
        private String fetchMaxSize = "10485760";
        private String fetchMaxWait = "1000";
        private String maxPartitionFetchSize;
        private boolean enableAutoCommit = true;
        private String autoCommitInterval = "100";
        private String autoOffsetReset = "latest";
        private String maxPollRecords = "1000";
        private String maxPollInterval;
        private String sessionTimeout;
        private String groupId = "GK-DEFAULT";
        private String heartbeatInterval;
        private String keyDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
        private String valueDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
        private Long pollTimeOut = 60000l;
        private Integer dataQueueSize = 1000;
        private Integer count = 1;
        private Map<String, String> receive = new HashMap<>();
        private Map<String, String> topicProcess = new HashMap<>();
        private List<String> topicList = new ArrayList<>();
        private int pollCount = 100;

        public List<String> getTopicList() {
            return topicList;
        }

        public void setTopicList(List<String> topicList) {
            this.topicList = topicList;
        }

        public Map<String, String> getTopicProcess() {
            return topicProcess;
        }

        public void setTopicProcess(Map<String, String> topicProcess) {
            this.topicProcess = topicProcess;
        }

        private String bootstrapServers;

        public String getBootstrapServers() {
            return bootstrapServers;
        }

        public void setBootstrapServers(String bootstrapServers) {
            this.bootstrapServers = bootstrapServers;
        }


        public Map<String, String> getReceive() {
            return receive;
        }

        public void setReceive(Map<String, String> receive) {
            this.receive = receive;
        }

        public Integer getDataQueueSize() {
            return dataQueueSize;
        }

        public void setDataQueueSize(Integer dataQueueSize) {
            this.dataQueueSize = dataQueueSize;
        }

        public long getPollTimeOut() {
            return pollTimeOut;
        }

        public void setPollTimeOut(long pollTimeOut) {
            this.pollTimeOut = pollTimeOut;
        }

        public String getFetchMaxSize() {
            return fetchMaxSize;
        }

        public void setFetchMaxSize(String fetchMaxSize) {
            this.fetchMaxSize = fetchMaxSize;
        }

        public String getMaxPartitionFetchSize() {
            return maxPartitionFetchSize;
        }

        public void setMaxPartitionFetchSize(String maxPartitionFetchSize) {
            this.maxPartitionFetchSize = maxPartitionFetchSize;
        }

        public String getMaxPollInterval() {
            return maxPollInterval;
        }

        public void setMaxPollInterval(String maxPollInterval) {
            this.maxPollInterval = maxPollInterval;
        }

        public String getSessionTimeout() {
            return sessionTimeout;
        }

        public void setSessionTimeout(String sessionTimeout) {
            this.sessionTimeout = sessionTimeout;
        }

        public String getHeartbeatInterval() {
            return heartbeatInterval;
        }

        public void setHeartbeatInterval(String heartbeatInterval) {
            this.heartbeatInterval = heartbeatInterval;
        }

        public String getFetchMinSize() {
            return fetchMinSize;
        }

        public void setFetchMinSize(String fetchMinSize) {
            this.fetchMinSize = fetchMinSize;
        }

        public String getFetchMaxWait() {
            return fetchMaxWait;
        }

        public void setFetchMaxWait(String fetchMaxWait) {
            this.fetchMaxWait = fetchMaxWait;
        }

        public boolean isEnableAutoCommit() {
            return enableAutoCommit;
        }

        public void setEnableAutoCommit(boolean enableAutoCommit) {
            this.enableAutoCommit = enableAutoCommit;
        }

        public String getOffsetCachePrefix() {
            return offsetCachePrefix;
        }

        public void setOffsetCachePrefix(String offsetCachePrefix) {
            this.offsetCachePrefix = offsetCachePrefix;
        }

        public String getAutoCommitInterval() {
            return autoCommitInterval;
        }

        public void setAutoCommitInterval(String autoCommitInterval) {
            this.autoCommitInterval = autoCommitInterval;
        }

        public String getAutoOffsetReset() {
            return autoOffsetReset;
        }

        public void setAutoOffsetReset(String autoOffsetReset) {
            this.autoOffsetReset = autoOffsetReset;
        }

        public String getMaxPollRecords() {
            return maxPollRecords;
        }

        public void setMaxPollRecords(String maxPollRecords) {
            this.maxPollRecords = maxPollRecords;
        }

        public String getKeyDeserializer() {
            return keyDeserializer;
        }

        public void setKeyDeserializer(String keyDeserializer) {
            this.keyDeserializer = keyDeserializer;
        }

        public String getValueDeserializer() {
            return valueDeserializer;
        }

        public void setValueDeserializer(String valueDeserializer) {
            this.valueDeserializer = valueDeserializer;
        }

        public int getPollCount() {
            return pollCount;
        }

        public void setPollCount(int pollCount) {
            this.pollCount = pollCount;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public void setPollTimeOut(Long pollTimeOut) {
            this.pollTimeOut = pollTimeOut;
        }


    }

    public static class Producer {
        private String batchSize = "16384";
        private String bufferMemory;
        private String retries = "0";
        private String linger = "1";
        private String requestTimeoutMs = "10000";
        private String maxBlockTimeMs = "60000";
        private String acks = "1";
        private String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";
        private String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";
        private String partitionerClass = "com.dgp.kafka.core.KafkaMyPartition";
        private String bootstrapServers;
        private int count = 1;
        private Map<String, String> send = new HashMap<>();

        public String getRequestTimeoutMs() {
            return requestTimeoutMs;
        }

        public void setRequestTimeoutMs(String requestTimeoutMs) {
            this.requestTimeoutMs = requestTimeoutMs;
        }

        public String getBootstrapServers() {
            return bootstrapServers;
        }

        public void setBootstrapServers(String bootstrapServers) {
            this.bootstrapServers = bootstrapServers;
        }

        public Map<String, String> getSend() {
            return send;
        }

        public void setSend(Map<String, String> send) {
            this.send = send;
        }

        public String getPartitionerClass() {
            return partitionerClass;
        }

        public void setPartitionerClass(String partitionerClass) {
            this.partitionerClass = partitionerClass;
        }

        public String getLinger() {
            return linger;
        }

        public String getAcks() {
            return acks;
        }

        public void setAcks(String acks) {
            this.acks = acks;
        }

        public String getBufferMemory() {
            return bufferMemory;
        }

        public void setBufferMemory(String bufferMemory) {
            this.bufferMemory = bufferMemory;
        }

        public String getRetries() {
            return retries;
        }

        public void setRetries(String retries) {
            this.retries = retries;
        }

        public void setLinger(String linger) {
            this.linger = linger;
        }

        public String getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(String batchSize) {
            this.batchSize = batchSize;
        }

        public String getKeySerializer() {
            return keySerializer;
        }

        public void setKeySerializer(String keySerializer) {
            this.keySerializer = keySerializer;
        }

        public String getValueSerializer() {
            return valueSerializer;
        }

        public void setValueSerializer(String valueSerializer) {
            this.valueSerializer = valueSerializer;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getMaxBlockTimeMs() {
            return maxBlockTimeMs;
        }

        public void setMaxBlockTimeMs(String maxBlockTimeMs) {
            this.maxBlockTimeMs = maxBlockTimeMs;
        }

    }

    public static class Listener {
        private Integer concurrency;
        private String ackMode;
        private boolean batchListener;

        public Integer getConcurrency() {
            return concurrency;
        }

        public void setConcurrency(Integer concurrency) {
            this.concurrency = concurrency;
        }

        public String getAckMode() {
            return ackMode;
        }

        public void setAckMode(String ackMode) {
            this.ackMode = ackMode;
        }

        public boolean isBatchListener() {
            return batchListener;
        }

        public void setBatchListener(boolean batchListener) {
            this.batchListener = batchListener;
        }

    }

    public boolean isRebalanced() {
        return rebalanced;
    }

    public void setRebalanced(boolean rebalanced) {
        this.rebalanced = rebalanced;
    }

}
