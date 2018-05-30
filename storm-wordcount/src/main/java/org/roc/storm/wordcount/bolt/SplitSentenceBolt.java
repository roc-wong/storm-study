package org.roc.storm.wordcount.bolt;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * @author roc
 * @date 2018/05/29
 */
public class SplitSentenceBolt extends BaseBasicBolt {
    
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String sentence = input.getString(0);
        String[] words = sentence.split(" ");
        for (String word : words) {
            collector.emit(new Values(word));
        }
    }
}
