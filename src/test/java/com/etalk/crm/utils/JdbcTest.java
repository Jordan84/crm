package com.etalk.crm.utils;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;

/**
 * 添加课程信息，以监控课程微信消息提醒上课是否可用
 */
public class JdbcTest {
	public static void main(String[] args) {
		String[] format=new String[]{"yyyy-MM-dd","HH:mm:ss"};

		try{
			//调用Class.forName()方法加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			//生产库
			String url="jdbc:mysql://rm-wz92yzw0t6298vht3rw.mysql.rds.aliyuncs.com:3306/etalk?autoReconnect=true&amp;rewriteBatchedStatements=true&amp;failOverReadOnly=false&amp;useSSL=false";
			Connection conn=DriverManager.getConnection(url,"webroot","web@etalk365!jordan");
			//测试库
			/*String url="jdbc:mysql://rm-wz9n42j79i8a78609ho.mysql.rds.aliyuncs.com:3306/testetalk?autoReconnect=true&amp;rewriteBatchedStatements=true&amp;failOverReadOnly=false&amp;useSSL=false";
			Connection conn=DriverManager.getConnection(url,"testweb","testweb@etalk365");*/
			conn.setAutoCommit(false);
			Statement statement=conn.createStatement();
			Date startDay=DateUtils.parseDate("2018-06-30",format);
			Date endDay=DateUtils.parseDate("2019-06-30",format);
			while (startDay.getTime()<endDay.getTime()){
				Date startTime=DateUtils.parseDate("08:00:00",format);
				Date endTime=DateUtils.parseDate("23:00:00",format);
				String sql="";
				while (startTime.getTime()<endTime.getTime()){
					sql="INSERT INTO `lessons` ( `stores_id`, `teacher_login`, `teacher_level`, `release_time`, `student_login`, `student_stores_id`, `bespeak_time`, `class_way`, `order_id`, " +
							"`pay_money`, `state`, `evaluation`, `level`, `advice`, `tea_rank_id`, `show`, `class_type`, `record_time`, `recorder`, `update_time`, `update_recorder`, " +
							"`textbooks_name`, `remark`, `person_id`, `is_end`)" +
							" VALUES (" +
							"'1', 'ttestweixin', '4', '"+DateFormatUtils.format(startDay,"yyyy-MM-dd")+" "+ DateFormatUtils.format(startTime,"HH:mm:ss") +"', 'stestweixin', 1,now(), '4', " +
							"'PO201806062450', '0', '1', '', '0', '', '0', '0', '1', " +
							"now(), 'admin', now(), 'stestweixin', '02-1-1 Unit 1 Hello测试', '', '74355', '0');";
					//System.out.println(sql);
					statement.addBatch(sql);
					startTime=DateUtils.addMinutes(startTime,20);
				}

				System.out.println(statement.executeBatch().length);
				conn.commit();
				startDay=DateUtils.addDays(startDay,1);
				statement.clearBatch();
			}
			statement.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
