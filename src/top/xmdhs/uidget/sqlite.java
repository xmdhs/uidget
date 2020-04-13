package top.xmdhs.uidget;

import org.sqlite.SQLiteException;

import java.sql.*;

public class sqlite {
    /**
     * 创建数据库
     */
    public void creatSql() {
        try {
            String sql = null;
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:mcbbs.db");
            Statement stmt = c.createStatement();
            sql = "CREATE TABLE MCBBS " +
                    "(UID INT PRIMARY KEY     NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " credits            INT     NOT NULL )";
            stmt.executeUpdate(sql);

            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于向数据库写入数据
     * @param uid uid
     * @param name 名字（单引号大概要处理成 '' 就可以避免错误了吧
     * @param credits 积分
     */
    public void insertsql(String uid, String name, String credits) {
        //INSERT INTO TABLE_NAME VALUES (value1,value2,value3,...valueN);
        StringBuilder sql = new StringBuilder("INSERT INTO MCBBS VALUES(");
        sql.append(uid).append(",");
        sql.append(name).append(",");
        sql.append(credits).append(")");
        try {
            Connection c = DriverManager.getConnection("jdbc:sqlite:mcbbs.db");
            Statement stmt = c.createStatement();
            c.setAutoCommit(false);
            stmt.executeUpdate(sql.toString());
            stmt.close();
            c.commit();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 用 sql 储存目前爬取进入，方便崩溃后重新爬取
     * @param uid 目前爬取的进度
     */
    public void setUid(int uid){
        try {
            Connection c = DriverManager.getConnection("jdbc:sqlite:mcbbs.db");
            Statement stmt = c.createStatement();
            c.setAutoCommit(false);
            String sql = "UPDATE COMPANY set MCBBS = "+ uid +" where ID=23333333;";
            stmt.executeUpdate(sql);
            c.commit();

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}