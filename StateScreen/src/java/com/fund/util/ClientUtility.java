package com.fund.util;

import java.util.Properties;

import javax.naming.InitialContext;

public class ClientUtility {	
	public static InitialContext getContext(){
		try{
			Properties props = new Properties();			
                        props.setProperty("java.naming.factory.initial","com.sun.enterprise.naming.impl.SerialInitContextFactory");
			props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming.impl");
			props.setProperty("java.naming.provider.url", "remote://localhost:4848");
			props.setProperty("java.naming.security.principal", "admin");
			props.setProperty("java.naming.security.credentials", "admin");
			InitialContext initContext = new InitialContext(props);
			return initContext;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getLookupName(){
		try{
                    /*{
                        String stateJNDI = "java:global/StateEntityBeanProject/StateEntityBean";			
                        System.out.println("the Look up string is "+stateJNDI);
                        return stateJNDI;
                    }*/
                    {
                        String filterJNDI = "java:global/FilterRuleEntityBeanProject/FilterRuleEntityBean";			
                        System.out.println("the Look up string is "+filterJNDI);
                        return filterJNDI;
                    }
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
        
        public static String getStateLookupName(){
		try{
                    
                        String filterJNDI = "java:global/StateEntityBeanProject/StateEntityBean";			
                        System.out.println("the Look up string is "+filterJNDI);
                        return filterJNDI;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
