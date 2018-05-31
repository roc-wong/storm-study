package org.roc.storm.jdbc.bolt;

import org.apache.storm.jdbc.bolt.JdbcInsertBolt;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.mapper.JdbcMapper;
import org.roc.storm.jdbc.bolt.mapper.TableJdbcMapper;

/**
 * @author roc
 * @date 2018/05/31
 */
public class TableJdbcInsertBolt extends JdbcInsertBolt {

    private static JdbcMapper TABLE_JDBC_MAPPER = new TableJdbcMapper();

    public TableJdbcInsertBolt(ConnectionProvider connectionProvider) {
        super(connectionProvider, TABLE_JDBC_MAPPER);
        withInsertQuery("insert into storm_jdbc_test(word, count) values(?, ?) on duplicate key update count=count + 1");
    }

}
