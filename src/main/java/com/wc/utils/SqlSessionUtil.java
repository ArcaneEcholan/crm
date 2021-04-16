package com.wc.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionUtil {
	
	private SqlSessionUtil(){}
	
	private static SqlSessionFactory factory;
	
	static{
		
		String resource = "mybatis-config.xml";
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		factory =
		 new SqlSessionFactoryBuilder().build(inputStream);
		
	}
	
	public static ThreadLocal<SqlSession> threadLocal = new ThreadLocal<SqlSession>();
	
	public static SqlSession getSqlSession(){

		SqlSession session = threadLocal.get();
		
		if(session==null){
			
			session = factory.openSession();
			set(session);
		}
		
		return session;
		
	}

	public static void set(SqlSession session) {
		System.out.println("===============================sqlsession set===============================");
		System.out.println(session);
		threadLocal.set(session);
	}

	public static void remove() {
		SqlSession session = threadLocal.get();
		System.out.println(session);
		threadLocal.remove();
		System.out.println("===============================sqlsession destroy===============================");
	}
	
	public static void myClose(SqlSession session){
		
		if(session!=null){
			session.close();
			remove();
		}
		
	}
	
	
}
