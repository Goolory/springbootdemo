package com.njust.springbootdemo.common;


import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HBaseService {
    private Logger log = LoggerFactory.getLogger(HBaseService.class);

    private Configuration conf = null;
    private Connection connection = null;

    public HBaseService(Configuration conf) {
        this.conf = conf;

        try {
            connection = ConnectionFactory.createConnection(conf);
            log.debug("获取HBASE连接成功");
        } catch (IOException e) {
            log.error("获取HBASE连接失败");
        }
    }
    /*查询num*/
    public String[] findnum_according_sid_roomid(String tableName,String sid,String roomid) throws IOException {

        System.out.print("start");
        boolean flag1=false;
        boolean flag2=false;
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        ResultScanner resultScanner = table.getScanner(scan);
        int[] temp;
        for (Result result : resultScanner) {
            String stationnum=null;
            String realitynum=null;
            String roomname=null;
            Cell[] cells = result.rawCells();
            for (Cell cell : cells) {
                if(new String(CellUtil.cloneFamily(cell)).equals("change")&&new String(CellUtil.cloneQualifier(cell)).equals("Sid")&&new String(CellUtil.cloneValue(cell)).equals(sid))
                    flag1=true;
                if(new String(CellUtil.cloneFamily(cell)).equals("change")&&new String(CellUtil.cloneQualifier(cell)).equals("Roomid")&&new String(CellUtil.cloneValue(cell)).equals(roomid))
                    flag2=true;
                if(new String(CellUtil.cloneFamily(cell)).equals("change")&&new String(CellUtil.cloneQualifier(cell)).equals("Stationnum"))
                    stationnum=new String(CellUtil.cloneValue(cell));
                if(new String(CellUtil.cloneFamily(cell)).equals("change")&&new String(CellUtil.cloneQualifier(cell)).equals("Realitynum"))
                    realitynum=new String(CellUtil.cloneValue(cell));
                if(new String(CellUtil.cloneFamily(cell)).equals("change")&&new String(CellUtil.cloneQualifier(cell)).equals("Roomname"))
                    roomname=new String(CellUtil.cloneValue(cell));
                System.out.println("RowName:" + new String(CellUtil.cloneRow(cell)) + " ");
                //System.out.println("Timetamp:" + cell.getTimestamp() + " ");
                //System.out.println("column Family:" + new String(CellUtil.cloneFamily(cell)) + " ");
                //System.out.println("column Name:" + new String(CellUtil.cloneQualifier(cell)) + " ");
                //System.out.println("value:" + new String(CellUtil.cloneValue(cell)) + " ");
            }
            if(flag1&&flag2)
            {
                return (new String[]{stationnum,realitynum,roomname});
            }
            else
            {
                flag1=false;
                flag2=false;
                stationnum=null;
                realitynum=null;
                roomname=null;
            }

        }
        close(null, null, table);
        return null;
    }
    /**
     * 创建表
     * @param tableName 表名
     * @param columnFamily 列族名
     * @return
     */

    public boolean createTable(String tableName, List<String> columnFamily) {
        Admin admin = null;
        try {
            admin = connection.getAdmin();

            List<ColumnFamilyDescriptor> familyDescriptors = new ArrayList<>(columnFamily.size());

            columnFamily.forEach(cf -> {
                familyDescriptors.add(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(cf)).build());
            });

            TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName))
                    .setColumnFamilies(familyDescriptors)
                    .build();
            if (admin.tableExists(TableName.valueOf(tableName))) {
                log.debug("table Exists!");
            } else {
                admin.createTable(tableDescriptor);
                log.debug("create table success!");
            }
        } catch (IOException e) {
            log.error(MessageFormat.format("创建表{0}失败", tableName), e);
            return false;
        }finally {
            close(admin, null, null);
        }
        return true;
    }


    /**
     * 遍历查询指定表中的所有数据
     * @param tableName
     * @return
     */
    public Map<String, Map<String, String>> getResultScanner(String tableName) {
        Scan scan = new Scan();
        return this.queryData(tableName, scan);
    }

    /**
     * 根据tableName，起始行，数量实现分页
     * @param tableName
     * @param startrowKey
     * @param count
     * @return
     */

    public Map<String, Map<String, String>> getResultByPageScanner(String tableName, String startrowKey, String count) {
        Filter filter = new PageFilter(Integer.parseInt(count));
        FilterList filterList = new FilterList();
        filterList.addFilter(filter);

        Scan scan = new Scan();
        if (StringUtils.isNoneBlank(startrowKey)){
            scan.withStartRow(Bytes.toBytes(startrowKey));
        }
        scan.setFilter(filter);
        return this.queryData(tableName, scan);

    }

    /**
     * 根据startRowKey 和stopRowKey遍历查询指定表中的所有数据
     * @param tableName 表名
     * @param startRowKey 起始rowkey
     * @param stopRowKey 结束rowkey
     * @return
     */

    public Map<String, Map<String, String>> getResultScanner(String tableName, String startRowKey, String stopRowKey) {
        Scan scan = new Scan();
        if (StringUtils.isNoneBlank(startRowKey) && StringUtils.isNoneBlank(stopRowKey)) {
            scan.withStartRow(Bytes.toBytes(startRowKey));
            scan.withStartRow(Bytes.toBytes(stopRowKey));
        }
        return this.queryData(tableName, scan);
    }

    /**
     * 获取table
     * @param tableName
     * @return
     * @throws IOException
     */

    private Table getTable(String tableName) throws IOException {
        return connection.getTable(TableName.valueOf(tableName));
    }

    /**
     * 查询行键中包含的特定字符的数据
     * @param tableName
     * @param keyword
     * @return
     */

    public Map<String, Map<String, String>> getReusltScannerRowFilter(String tableName, String keyword) {
        Scan scan = new Scan();

        if (StringUtils.isNoneBlank(keyword)) {
            Filter filter = new RowFilter(CompareOperator.GREATER_OR_EQUAL, new SubstringComparator(keyword));
            scan.setFilter(filter);

        }
        return this.queryData(tableName, scan);
    }


    /**
     * 查询列名中包含的特定字符的数据
     * @param tableName
     * @param keyword
     * @return
     */
    public Map<String,Map<String,String>> getResultScannerQualifierFilter(String tableName, String keyword){
        Scan scan = new Scan();

        if(StringUtils.isNoneBlank(keyword)){
            Filter filter = new QualifierFilter(CompareOperator.GREATER_OR_EQUAL,new SubstringComparator(keyword));
            scan.setFilter(filter);
        }

        return this.queryData(tableName,scan);
    }
    /**
     * 通过表名以及过滤条件查询数据
     * @param tableName
     * @param scan 过滤条件
     * @return
     */

    private Map<String, Map<String, String>> queryData (String tableName, Scan scan) {
        Map<String, Map<String, String>> result = new HashMap<>();

        ResultScanner rs = null;

        Table table = null;

        try {
            table = getTable(tableName);
            rs = table.getScanner(scan);
            for (Result r : rs) {
                Map<String, String> columnMap = new HashMap<>();
                String rowKey = null;
                for (Cell cell : r.listCells()) {
                    if (rowKey == null) {
                        rowKey = Bytes.toString(cell.getRowArray(),cell.getRowOffset(),cell.getRowLength());
                    }
                    columnMap.put(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()), Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                }

                if (rowKey != null) {
                    result.put(rowKey, columnMap);
                }
            }


        } catch (IOException e){
            log.error(MessageFormat.format("遍历查询指定表中的所有数据失败，tableName：{0}", tableName), e);
        } finally {
            close(null, rs, table);
        }
        return result;
    }


    /**
     * 根据tableName和rowKey精确查询一行数据
     * @param tableName
     * @param rowKey
     * @return
     */
    public Map<String,String> getRowData(String tableName, String rowKey){
        //返回的键值对
        Map<String,String> result = new HashMap<>();

        Get get = new Get(Bytes.toBytes(rowKey));
        // 获取表
        Table table= null;
        try {
            table = getTable(tableName);
            Result hTableResult = table.get(get);
            if (hTableResult != null && !hTableResult.isEmpty()) {
                for (Cell cell : hTableResult.listCells()) {
                    result.put(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()), Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                }
            }
        }catch (IOException e) {
            log.error(MessageFormat.format("查询一行的数据失败,tableName:{0},rowKey:{1}"
                    ,tableName,rowKey),e);
        }finally {
            close(null,null,table);
        }

        return result;
    }

    /**
     * 根据tableName、 rowKey、familyName、column查询指定单元格的数据
     * @param tableName
     * @param rowKey
     * @param familyName
     * @param columnName
     * @return
     */
    public String getColumnValue(String tableName, String rowKey, String familyName, String columnName){
        String str = null;
        Get get = new Get(Bytes.toBytes(rowKey));
        // 获取表
        Table table= null;
        try {
            table = getTable(tableName);
            Result result = table.get(get);
            if (result != null && !result.isEmpty()) {
                Cell cell = result.getColumnLatestCell(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
                if(cell != null){
                    str = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                }
            }
        } catch (IOException e) {
            log.error(MessageFormat.format("查询指定单元格的数据失败,tableName:{0},rowKey:{1},familyName:{2},columnName:{3}"
                    ,tableName,rowKey,familyName,columnName),e);
        }finally {
            close(null,null,table);
        }

        return str;
    }

    /**
     * 根据tableName、rowKey、familyName、column查询指定单元格多个版本的数据
     * @param tableName
     * @param rowKey
     * @param familyName
     * @param columnName
     * @param versions
     * @return
     */

    public List<String> getColumnValuesByVersion(String tableName, String rowKey, String familyName, String columnName,int versions) {
        //返回数据
        List<String> result = new ArrayList<>(versions);

        // 获取表
        Table table= null;
        try {
            table = getTable(tableName);
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
            //读取多少个版本
            get.readVersions(versions);
            Result hTableResult = table.get(get);
            if (hTableResult != null && !hTableResult.isEmpty()) {
                for (Cell cell : hTableResult.listCells()) {
                    result.add(Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                }
            }
        } catch (IOException e) {
            log.error(MessageFormat.format("查询指定单元格多个版本的数据失败,tableName:{0},rowKey:{1},familyName:{2},columnName:{3}"
                    ,tableName,rowKey,familyName,columnName),e);
        }finally {
            close(null,null,table);
        }

        return result;
    }


    /**
     * 为表添加or更新数据
     * @param tableName
     * @param rowKey
     * @param familyName
     * @param columns
     * @param values
     */
    public void putData(String tableName,String rowKey, String familyName, String[] columns, String[] values) {
        // 获取表
        Table table= null;
        try {
            table=getTable(tableName);

            putData(table,rowKey,tableName,familyName,columns,values);
        } catch (Exception e) {
            log.error(MessageFormat.format("为表添加 or 更新数据失败,tableName:{0},rowKey:{1},familyName:{2}"
                    ,tableName,rowKey,familyName),e);
        }finally {
            close(null,null,table);
        }
    }


    /**
     * 为表添加or更新数据
     * @param table
     * @param rowKey
     * @param tableName
     * @param familyName
     * @param columns
     * @param values
     */
    private void putData(Table table, String rowKey, String tableName, String familyName, String[] columns, String[] values) {
        try {
            //设置rowkey
            Put put = new Put(Bytes.toBytes(rowKey));

            if(columns != null && values != null && columns.length == values.length){
                for(int i=0;i<columns.length;i++){
                    if(columns[i] != null && values[i] != null){
                        put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columns[i]), Bytes.toBytes(values[i]));
                    }else{
                        throw new NullPointerException(MessageFormat.format("列名和列数据都不能为空,column:{0},value:{1}"
                                ,columns[i],values[i]));
                    }
                }
            }

            table.put(put);
            log.debug("putData add or update data Success,rowKey:" + rowKey);
            table.close();
        } catch (Exception e) {
            log.error(MessageFormat.format("1为表添加 or 更新数据失败,tableName:{0},rowKey:{1},familyName:{2}"
                    ,tableName,rowKey,familyName),e);
        }
    }

    /**
     * 为表的某个单元格赋值
     * @param tableName
     * @param rowKey
     * @param familyName
     * @param column1
     * @param value1
     */
    public void setColumnValue(String tableName, String rowKey, String familyName, String column1, String value1){
        Table table=null;
        try {
            // 获取表
            table=getTable(tableName);
            // 设置rowKey
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(column1), Bytes.toBytes(value1));

            table.put(put);
            log.debug("add data Success!");
        }catch (IOException e) {
            log.error(MessageFormat.format("为表的某个单元格赋值失败,tableName:{0},rowKey:{1},familyName:{2},column:{3}"
                    ,tableName,rowKey,familyName,column1),e);
        }finally {
            close(null,null,table);
        }
    }

    /**
     * s删除指定的单元格
     * @param tableName
     * @param rowKey
     * @param familyName
     * @param columnName
     * @return
     */
    public boolean deleteColumn(String tableName, String rowKey, String familyName, String columnName) {
        Table table=null;
        Admin admin = null;
        try {
            admin = connection.getAdmin();

            if(admin.tableExists(TableName.valueOf(tableName))){
                // 获取表
                table=getTable(tableName);
                Delete delete = new Delete(Bytes.toBytes(rowKey));
                // 设置待删除的列
                delete.addColumns(Bytes.toBytes(familyName), Bytes.toBytes(columnName));

                table.delete(delete);
                log.debug(MessageFormat.format("familyName({0}):columnName({1})is deleted!",familyName,columnName));
            }

        }catch (IOException e) {
            log.error(MessageFormat.format("删除指定的列失败,tableName:{0},rowKey:{1},familyName:{2},column:{3}"
                    ,tableName,rowKey,familyName,columnName),e);
            return false;
        }finally {
            close(admin,null,table);
        }
        return true;
    }


    /**
     * 根据rowKey删除指定的行
     * @param tableName
     * @param rowKey
     * @return
     */
    public boolean deleteRow(String tableName, String rowKey) {
        Table table=null;
        Admin admin = null;
        try {
            admin = connection.getAdmin();

            if(admin.tableExists(TableName.valueOf(tableName))){
                // 获取表
                table=getTable(tableName);
                Delete delete = new Delete(Bytes.toBytes(rowKey));

                table.delete(delete);
                log.debug(MessageFormat.format("row({0}) is deleted!",rowKey));
            }
        }catch (IOException e) {
            log.error(MessageFormat.format("删除指定的行失败,tableName:{0},rowKey:{1}"
                    ,tableName,rowKey),e);
            return false;
        }finally {
            close(admin,null,table);
        }
        return true;
    }


    /**
     * 根据columFamily删除指定的列族
     * @param tableName
     * @param columnFamily
     * @return
     */
    public boolean deleteColumnFamily(String tableName, String columnFamily) {
        Admin admin = null;
        try {
            admin = connection.getAdmin();

            if(admin.tableExists(TableName.valueOf(tableName))){
                admin.deleteColumnFamily(TableName.valueOf(tableName), Bytes.toBytes(columnFamily));
                log.debug(MessageFormat.format("familyName({0}) is deleted!",columnFamily));
            }
        }catch (IOException e) {
            log.error(MessageFormat.format("删除指定的列族失败,tableName:{0},columnFamily:{1}"
                    ,tableName,columnFamily),e);
            return false;
        }finally {
            close(admin,null,null);
        }
        return true;
    }

    /**
     * 删除表
     * @param tableName
     * @return
     */
    public boolean deleteTable(String tableName){
        Admin admin = null;
        try {
            admin = connection.getAdmin();

            if(admin.tableExists(TableName.valueOf(tableName))){
                admin.disableTable(TableName.valueOf(tableName));
                admin.deleteTable(TableName.valueOf(tableName));
                log.debug(tableName + "is deleted!");
            }
        }catch (IOException e) {
            log.error(MessageFormat.format("删除指定的表失败,tableName:{0}"
                    ,tableName),e);
            return false;
        }finally {
            close(admin,null,null);
        }
        return true;
    }


    /**
     * 关闭流
     * @param admin
     * @param rs
     * @param table
     */
    private void close(Admin admin, ResultScanner rs, Table table) {
        if (admin != null) {
            try {
                admin.close();
            } catch (IOException e) {
                log.error("关闭Admin失败", e);
            }
        }

        if (rs != null) {
            rs.close();
        }
        if (table != null) {
            try {
                table.close();
            } catch (IOException e) {
                log.error("关闭Table失败", e);
            }
        }
    }

}
