package org.roc.storm.jdbc.bolt.mapper;

import com.google.common.collect.Lists;
import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.mapper.SimpleJdbcMapper;

import java.sql.Types;
import java.util.List;

/**
 * @author roc
 * @date 2018/05/31
 */
public class TableJdbcMapper extends SimpleJdbcMapper {

    private static List<Column> SCHEMA_COLUMNS = Lists.newArrayList(
            new Column("word", Types.VARCHAR),
            new Column("count", Types.INTEGER)
    );

    public TableJdbcMapper() {
        super(SCHEMA_COLUMNS);
    }
}
