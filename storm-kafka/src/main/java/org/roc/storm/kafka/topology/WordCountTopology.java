/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.roc.storm.kafka.topology;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringMultiSchemeWithTopic;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.roc.storm.wordcount.bolt.LogInfoBolt;
import org.roc.storm.wordcount.bolt.SplitSentenceBolt;
import org.roc.storm.wordcount.bolt.WordCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;

import static org.roc.storm.wordcount.constant.TopologyConstants.BOLT_LOGGER;
import static org.roc.storm.wordcount.constant.TopologyConstants.BOLT_SPLIT;
import static org.roc.storm.wordcount.constant.TopologyConstants.BOLT_WORD_COUNT;
import static org.roc.storm.wordcount.constant.TopologyConstants.SPOUT_SENTENCE;

/**
 * This is a basic example of a Storm topology.
 */
public class WordCountTopology implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(WordCountTopology.class);

    public static void main(String[] args) throws Exception {
        WordCountTopology wordCountTopology = new WordCountTopology();

        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(1);

        String topologyName = "word-count";

        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology(topologyName, conf, wordCountTopology.getTopology(conf));
    }

    /**
     * 如果你有已经存在的Storm拓扑，你仍然可以用Flux来部署/运行/测试它们。
     * 这个特点允许你按照已有的拓扑类来改变Flux构造参数，引用，属性和拓扑配置声明。
     * 使用已有拓扑类最简单的方法就是通过下面的方法定义一个名为 getTopology() 的实例方法。
     * PS：非静态方法
     *
     * @param config
     * @return
     */
    public StormTopology getTopology(Config config) {
        TopologyBuilder builder = new TopologyBuilder();

        String brokerZkStr = "10.0.30.49:2181,10.0.30.50:2181,10.0.30.67:2181";
        String brokerZkPath = "/kafka/brokers";
        ZkHosts zkHosts = new ZkHosts(brokerZkStr, brokerZkPath);

        String topic = "roc_storm_kafka_test";

        //进度信息记录于zookeeper的哪个路径下
        String zkRoot = "/kafka-data/storm/kafka_spout";

        //进度记录的id，想要一个新的Spout读取之前的记录，应把它的id设为跟之前的一样。
        String id = "storm_kafka_spout_roc";

        SpoutConfig spoutConfig = new SpoutConfig(zkHosts, topic, zkRoot, id);
        spoutConfig.scheme = new StringMultiSchemeWithTopic();

        Map<String, String> hostAndIps = Splitter.on(",").withKeyValueSeparator(":").split(brokerZkStr);
        spoutConfig.zkServers = Lists.newArrayList(hostAndIps.keySet());
        spoutConfig.zkPort = 2181;
        spoutConfig.startOffsetTime = kafka.api.OffsetRequest.LatestTime();

        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

        builder.setSpout(SPOUT_SENTENCE, kafkaSpout, 1);
        builder.setBolt(BOLT_SPLIT, new SplitSentenceBolt(), 2).shuffleGrouping(SPOUT_SENTENCE);
        builder.setBolt(BOLT_WORD_COUNT, new WordCount(), 3).fieldsGrouping(BOLT_SPLIT, new Fields("word"));
        builder.setBolt(BOLT_LOGGER, new LogInfoBolt()).localOrShuffleGrouping(BOLT_WORD_COUNT);

        return builder.createTopology();
    }
}
