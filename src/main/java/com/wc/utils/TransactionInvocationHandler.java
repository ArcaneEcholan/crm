package com.wc.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.ibatis.session.SqlSession;

public class TransactionInvocationHandler implements InvocationHandler{
	
	private Object target;
	
	public TransactionInvocationHandler(Object target){
		this.target = target;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		SqlSession session = null;
		Object obj = null;
		try{
			session = SqlSessionUtil.getSqlSession();
			System.out.println("<<<<<<<service:" + method.getName() +" start>>>>>>>>>>>");
			obj = method.invoke(target, args);
			System.out.println("<<<<<<<service:"+method.getName()+" end>>>>>>>>>>>");
			System.out.println("-----session commit------");
			session.commit();
		}catch(Exception e){
			System.out.println(e.getCause().getMessage());
			System.out.println("-----session rollback------");
			session.rollback();
			//处理的是什么异常，继续往上抛什么异常
			throw e.getCause();
		}finally{
			SqlSessionUtil.myClose(session);
		}
		return obj;
	}
	
	public Object getProxy(){
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),this);
	}
	
}
