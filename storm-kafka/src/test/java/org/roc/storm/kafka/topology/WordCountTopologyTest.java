package org.roc.storm.kafka.topology;


import com.google.common.base.Splitter;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * @author roc
 * @date 2018/05/30
 */
public class WordCountTopologyTest {
    @Test
    public void testSplitter() throws Exception {

        String brokerZkStr = "10.0.30.49:2181,10.0.30.50:2181,10.0.30.67:2181";

        Map<String, String> split = Splitter.on(",").withKeyValueSeparator(Splitter.on(":")).split(brokerZkStr);
        System.out.println(split);

        Map<String, String> split1 = Splitter.on(",").withKeyValueSeparator(":").split(brokerZkStr);
        System.out.println(split1.keySet());
    }

}