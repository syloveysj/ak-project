package com.yunjian.ak.dao.mybatis.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 10:25
 * @Version 1.0
 */
public abstract class JDBCUtils {
    public JDBCUtils() {
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static void closeResultset(ResultSet resultset) {
        if (resultset != null) {
            try {
                resultset.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
