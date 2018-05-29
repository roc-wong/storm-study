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
package org.roc.storm.study.wordcount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.testing.TestWordSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * This is a basic example of a Storm topology.
 */
public class ExclamationTopology {

    private static final Logger LOGGER = LoggerFactory.getLogger("FILE");

    public static class ExclamationBolt extends BaseRichBolt {
        OutputCollector _collector;

        @Override
        public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
            _collector = collector;
        }

        @Override
        public void execute(Tuple tuple) {
            long threadId = Thread.currentThread().getId();
            String threadName = Thread.currentThread().getName();
            String value = tuple.getString(0) + "--->!!!";
            LOGGER.error("threadId={}, threadName{}, this={}, value={}", threadId, threadName, this, value);
            _collector.emit(tuple, new Values( value ));
            _collector.ack(tuple);
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("word"));
        }

    }

    public static void main(String[] args) throws Exception {
        run(args);
    }

    protected static void run(String[] args) {
        Config conf = new Config();

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("word", new TestWordSpout(), 2);
        builder.setBolt("exclaim1", new ExclamationBolt(), 2).allGrouping("word");
//        builder.setBolt("exclaim2", new ExclamationBolt(), 2).shuffleGrouping("exclaim1");

        conf.setDebug(true);

        String topologyName = "test";

        conf.setNumWorkers(2);

        if (args != null && args.length > 0) {
            topologyName = args[0];
        }

        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology(topologyName, conf, builder.createTopology());
    }
}
