package org.roc.storm.wordcount.constant;

/**
 * @author roc
 * @date 2018/05/29
 */
public final class TopologyConstants {

    public static final String SPOUT_SENTENCE = "spout_sentence";

    public static final String BOLT_SPLIT = "bolt_split";
    public static final String BOLT_WORD_COUNT = "bolt_word_count";
    public static final String BOLT_LOGGER = "bolt_logger";

    /*JDBC Integration*/
    public static final String BOLT_JDBC_INSERT = "bolt_jdbc_insert";

}
