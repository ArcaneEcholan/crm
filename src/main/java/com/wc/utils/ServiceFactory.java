package com.wc.utils;

import org.apache.ibatis.session.SqlSession;

public class ServiceFactory {
	
	public static Object getService(Object service){
		
		return new TransactionInvocationHandler(service).getProxy();
	}
}
