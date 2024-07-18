package cn.czcps.oa.dataSource;


import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
//alibabaDruid线程池
public class DruidDataSourceFactory extends UnpooledDataSourceFactory {
    public DruidDataSourceFactory(){
        this.dataSource = new DruidDataSource();
    }

    @Override
    public DataSource getDataSource() {
        try {
            ((DruidDataSource)this.dataSource).init();//初始化Druid数据源
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this.dataSource;
    }
}
