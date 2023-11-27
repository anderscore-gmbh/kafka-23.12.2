package com.anderscore;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

public class MyPartitionerB implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes,
                         Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();

        if (numPartitions == 1) {
            return 0;
        }

        if ((Integer) key == 1111) {
            return 0; // This key will always go to Partition 0
        }
        // Other records will go to the rest of the Partitions using a hashing function
        return (Math.abs(Utils.murmur2(keyBytes)) % (numPartitions - 1)) + 1;
    }

    @Override
    public void close() {
        //Nothing to close
    }

    @Override
    public void configure(Map<String, ?> map) {
        //Nothing to configure
    }
}