package org.roc.storm.kafka.spout.scheme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hbec.flume.domain.EventPayload;
import org.apache.storm.spout.Scheme;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author roc
 * @date 2018/05/30
 */
public class FlumeEventScheme implements Scheme {

    private static final Logger LOG = LoggerFactory.getLogger(FlumeEventScheme.class);

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    public static Gson SERIALIZE_GSON = new GsonBuilder()
            .enableComplexMapKeySerialization() //当Map的key为复杂对象时,需要开启该方法
            .serializeNulls() //当字段值为空或null时，依然对该字段进行转换
            .disableHtmlEscaping() //防止特殊字符出现乱码
            .create();

    @Override
    public List<Object> deserialize(ByteBuffer byteBuffer) {
        String message = new String(Utils.toByteArray(byteBuffer), UTF_8);
        try {
            EventPayload eventPayload = SERIALIZE_GSON.fromJson(message, EventPayload.class);
            return new Values(eventPayload);
        } catch (Exception e) {
            LOG.error("deserialize message from kafka error, value=" + message, e);
            return null;
        }
    }

    @Override
    public Fields getOutputFields() {
        return new Fields("sentence");
    }
}
