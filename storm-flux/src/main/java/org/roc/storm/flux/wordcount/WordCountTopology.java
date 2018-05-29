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
package org.roc.storm.flux.wordcount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.roc.storm.flux.wordcount.bolt.LogInfoBolt;
import org.roc.storm.flux.wordcount.bolt.SplitSentenceBolt;
import org.roc.storm.flux.wordcount.bolt.WordCount;
import org.roc.storm.flux.wordcount.spout.RandomSentenceSpout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.roc.storm.flux.wordcount.constant.TopologyConstants.BOLT_LOGGER;
import static org.roc.storm.flux.wordcount.constant.TopologyConstants.BOLT_SPLIT;
import static org.roc.storm.flux.wordcount.constant.TopologyConstants.BOLT_WORD_COUNT;
import static org.roc.storm.flux.wordcount.constant.TopologyConstants.SPOUT_SENTENCE;

/**
 * This is a basic example of a Storm topology.
 */
public class WordCountTopology {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordCountTopology.class);


    public static void main(String[] args) throws Exception {
        run(args);
    }

    protected static void run(String[] args) {
        Config conf = new Config();

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout(SPOUT_SENTENCE, new RandomSentenceSpout(), 1);
        builder.setBolt(BOLT_SPLIT, new SplitSentenceBolt(), 2).shuffleGrouping(SPOUT_SENTENCE);
        builder.setBolt(BOLT_WORD_COUNT, new WordCount(), 3).fieldsGrouping(BOLT_SPLIT, new Fields("word"));
        builder.setBolt(BOLT_LOGGER, new LogInfoBolt()).localOrShuffleGrouping(BOLT_WORD_COUNT);

        conf.setDebug(true);

        String topologyName = "word-count";

        conf.setNumWorkers(1);

        if (args != null && args.length > 0) {
            topologyName = args[0];
        }

        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology(topologyName, conf, builder.createTopology());
    }
}
