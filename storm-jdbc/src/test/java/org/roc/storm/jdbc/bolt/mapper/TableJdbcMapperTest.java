package org.roc.storm.jdbc.bolt.mapper;

import org.testng.annotations.Test;

/**
 * @author roc
 * @date 2018/05/31
 */
public class TableJdbcMapperTest {

    @Test
    public void testChildInit() throws Exception {
        Children c = new Children();
        /* Parent pc=new Children();
        Parent pp=new Parent();
        pc.hh();
        pp.hh();*/
    }

    @Test
    public void testAllInit() throws Exception {
        Parent pc = new Children();
        Parent pp = new Parent();
        pc.hh();
        pp.hh();
    }
}