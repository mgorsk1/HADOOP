package pl.com.sages.hbase.api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static pl.com.sages.hbase.api.conf.HbaseConfigurationFactory.getConfiguration;

public class HbaseApiExternalTest {

    public static final TableName TEST_TABLE_NAME = TableName.valueOf("test_" + System.currentTimeMillis());
    public static final String TEST_FAMILY_NAME = "info";

    private Admin admin;
    private Connection connection;

    @Before
    public void createTestTable() throws Exception {
        Configuration  configuration = getConfiguration();
        connection = ConnectionFactory.createConnection(configuration);
        admin = connection.getAdmin();

        HTableDescriptor table = new HTableDescriptor(TEST_TABLE_NAME);

        HColumnDescriptor columnFamily = new HColumnDescriptor(TEST_FAMILY_NAME);
        columnFamily.setMaxVersions(1);
        table.addFamily(columnFamily);

        admin.createTable(table);
    }

    @After
    public void deleteTable() throws Exception {
        if (admin != null ) {
            admin.disableTable(TEST_TABLE_NAME);
            admin.deleteTable(TEST_TABLE_NAME);
            admin.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void shouldPutDataOnHbase() throws Exception {
        //given
        Table users = connection.getTable(TEST_TABLE_NAME);
        Put put = new Put(Bytes.toBytes("id"));
        put.addColumn(Bytes.toBytes(TEST_FAMILY_NAME),
                      Bytes.toBytes("cell"),
                      Bytes.toBytes("nasza testowa wartość"));

        //when
        users.put(put);

        //then
    }

}
