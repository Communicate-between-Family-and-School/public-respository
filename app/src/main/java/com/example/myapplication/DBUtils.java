package com.example.myapplication;

import static com.example.myapplication.IDENTITY.IdentityType.LEADER;
import static com.example.myapplication.IDENTITY.IdentityType.PARENT;
import static com.example.myapplication.IDENTITY.IdentityType.STUDENT;
import static com.example.myapplication.IDENTITY.IdentityType.TEACHER;

import android.nfc.Tag;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DBUtils {
    private static String driver = "com.mysql.jdbc.Driver";// MySql驱动
    //private static String url = "jdbc:mysql://localhost:3306/database";
    private static String user = "root";// 用户名
    private static String password = "uestc2022!";// 密码
    private static String username = "android";//数据库名
    private static Connection getConnection(){
        Connection connection = null;
        try{
            Class.forName(driver);// 动态加载类
            String ip = "124.71.219.185";// 写成本机地址，不能写成localhost，同时手机和电脑连接的网络必须是同一个
            // 尝试建立到给定数据库URL的连接
            int port = 3306;
            String url = "jdbc:mysql://" + ip + ":" + port
                            + "/" + username + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true";
            connection = DriverManager.getConnection(url, user, password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }

    public static HashMap<String, Object> getInfoByName(String name){
        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = getConnection();
        try {
            String sql = "select * from student where name = ?";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    // 设置上面的sql语句中的？的值为name
                    ps.setString(1, name);
                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
                    if (rs != null){
                        int count = rs.getMetaData().getColumnCount();
                        Log.d("DBUtils","列总数：" + count);
                        while (rs.next()){
                            // 注意：下标是从1开始的
                            for (int i = 1;i <= count;i++){
                                String field = rs.getMetaData().getColumnName(i);
                                map.put(field, rs.getString(field));
                            }
                        }
                        connection.close();
                        ps.close();
                        return  map;
                    }else Log.e("DBUtils","1.查询条件不满足\n2.数据库中无匹配数据\n3.查询语句错误");
                }else Log.e("DBUtils","1.SQL语句错误\n2.连接断开");
            }else Log.e("DBUtils","数据库连接失败");
            return null;
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBUtils","异常：" + e.getMessage());
            return null;
        }
    }


    public static IDENTITY LoginById(String account_text, String password_text) {
        IDENTITY identity = new IDENTITY();
        Connection connection = getConnection();
        try {
            String sql = "SELECT r.rname " +
                    "FROM users u " +
                    "JOIN user_role ur ON u.uid = ur.uid " +
                    "JOIN role r ON ur.rid = r.rid " +
                    "WHERE u.uid = ? " +
                    "AND u.password = ?;";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    ps.setString(1, account_text);
                    ps.setString(2, password_text);
                    ResultSet rs = ps.executeQuery();
                    if (rs != null){
                        if (rs.last()) {//int rowCount = 0;rowCount = rs.getRow();// 获取结果集的当前行数 rs.beforeFirst(); // 将结果集的指针重置到第一行之前
                            String roleName = rs.getString("rname");
                            if(roleName.equals("student")){
                                identity.setIdentity(STUDENT);
                            }
                            if(roleName.equals("teacher")){
                                identity.setIdentity(TEACHER);
                            }
                            if(roleName.equals("parent")){
                                identity.setIdentity(PARENT);
                            }
                            if(roleName.equals("leader")){
                                identity.setIdentity(LEADER);
                            }
                        }//Log.e("DBUtils","行总数：" + rowCount);
                        ps.close();
                    }else Log.e("DBUtils","1.查询条件不满足\n2.数据库中无匹配数据\n3.查询语句错误");
                }else Log.e("DBUtils","1.SQL语句错误\n2.连接断开");
            }else Log.e("DBUtils","数据库连接失败");
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBUtils","异常：" + e.getMessage());
        }
        return identity;
    }


    public static ResultSet Read(String sql) {

        Connection connection = getConnection();
        try {
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    ResultSet rs = ps.executeQuery();
                    if (rs != null){
                        if (rs.last()) {//int rowCount = 0;rowCount = rs.getRow();// 获取结果集的当前行数 rs.beforeFirst(); // 将结果集的指针重置到第一行之前
                            String roleName = rs.getString("rname");
                        }
                        ps.close();
                    }else Log.e("DBUtils","1.查询条件不满足\n2.数据库中无匹配数据\n3.查询语句错误");
                }else Log.e("DBUtils","1.SQL语句错误\n2.连接断开");
            }else Log.e("DBUtils","数据库连接失败");
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBUtils","异常：" + e.getMessage());
        }

//        Connection connection = getConnection();
        ResultSet resultSet = null;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            resultSet = ps.executeQuery();

            if (resultSet != null)resultSet.close();
            if (ps != null)ps.close();
            if (connection != null)connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
