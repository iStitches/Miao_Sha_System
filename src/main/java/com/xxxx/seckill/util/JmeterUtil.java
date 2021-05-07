package com.xxxx.seckill.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxxx.seckill.constant.ResultObj;
import com.xxxx.seckill.pojo.User;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * JMeter 测试数据生成工具类
 */
public class JmeterUtil {
    private static void createUser(int count) throws Exception{
        //生成count个User数据
        List<User> userList = new ArrayList<>();
        for(int i=0;i<count;i++){
            User user = new User();
            user.setId(13000000000L+i);
            user.setNickname("user"+i);
            user.setSalt("1a2b3c4d");
            user.setPassword(Md5Util.inputToDbPass("12345678",user.getSalt()));
            userList.add(user);
        }
        System.out.println("create user finish");

        //Mysql插入
        Connection conn = null;
        conn = getConn();
        String sql = "insert into t_user(id,nickname,password,salt,register_date,last_login_date) values(?,?,?,?,?,?)";
        PreparedStatement prst = conn.prepareStatement(sql);
        for(int i=0;i<userList.size();i++){
            User user = userList.get(i);
            prst.setLong(1,user.getId());
            prst.setString(2,user.getNickname());
            prst.setString(3,user.getPassword());
            prst.setString(4,user.getSalt());
            prst.setTimestamp(5,new Timestamp(LocalDateTime.now().atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli()));
            prst.setTimestamp(6,new Timestamp(LocalDateTime.now().atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli()));
            prst.addBatch();
        }
        prst.executeBatch();
        prst.clearParameters();
        conn.close();


        File file = new File("src/main/resources/static/config.txt");
        if(file.exists())
            file.delete();
        RandomAccessFile raf = new RandomAccessFile(file,"rw");
        raf.seek(0);

        //模拟浏览器请求，获取响应信息
        for(User user:userList){
            //获取连接
            URL url = new URL("http://localhost:8080/login/login");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            //设置请求体
            OutputStream outputStream = connection.getOutputStream();
            String params = "account="+user.getId()+"&password="+Md5Util.inputPassToFormPass("12345678");
            outputStream.write(params.getBytes());
            outputStream.flush();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = reader.readLine())!=null)
                System.out.println(line);

            //获取请求头中的token,使用IO流写入文件
            String authorization = connection.getHeaderField("Authorization");
            raf.seek(raf.length());
            if(StringUtils.isNotBlank(authorization)){
                raf.write(authorization.getBytes());
                raf.write("\r\n".getBytes());
            }
            outputStream.close();
        }
        raf.close();
        System.out.println("over");
    }

    //获取连接
    private static Connection getConn() throws Exception {
        String url = "jdbc:mysql://39.100.119.221:3306/seckill?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serveTimezone=Asia/Shanghai";
        String driver = "com.mysql.cj.jdbc.Driver";
        String username = "root";
        String password = "123456";
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection;
    }



    public static void main(String[] args) throws Exception{
        createUser(300);
    }
}
