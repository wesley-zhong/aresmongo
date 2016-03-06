package com.ares.framework.rpc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ares.framework.service.IService;
import com.ares.framework.service.ServiceMgr;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;



@Controller
public class WebRequestRpc {
	@Inject
	private ServiceMgr  serviceMgr;
	
	public WebRequestRpc()
	{
		
	}
	
	@RequestMapping(value="/view/{serviceName}/{methodName}",method = RequestMethod.POST )
	public String  CallView(@PathVariable String serviceName,
			@PathVariable String  methodName,Model model,HttpServletRequest req ) throws JsonParseException, JsonMappingException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, InstantiationException
	{
		IService service = serviceMgr.GetService(serviceName);
		if(service == null){
			model.addAttribute("errormsg", "can not find the service name :"+serviceName);
		    return "404";
		}
		
		Method method = this.GetMethod(service, methodName);
		if(method == null){
			model.addAttribute("errormsg", "can not find the method:"+methodName+"in the service: "+serviceName);
			 return "404";
		}
		return  CallObjMethod(service, method, req.getParameterMap(),model);
	}
	
	
	@RequestMapping(value="/rpc/{serviceName}/{methodName}",method = RequestMethod.POST )
	@ResponseBody
	public Object  CallRpc(@PathVariable String serviceName,
			@PathVariable String  methodName,Model model,HttpServletRequest req ) throws JsonParseException, JsonMappingException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, InstantiationException
	{
		HttpSession session= req.getSession(false);
		Cookie[]  cookies =  req.getCookies();
		if(cookies != null){
			Cookie cookie = cookies[0];
			System.out.println("VVVVVVVVVVVVVVV   value = "+cookie.getValue()+"  name ="+cookie.getName());
		}
//		if(session == null){
//			session = req.getSession(true);
//			session.setAttribute("name", "wesley");
//		}
//		else{
//			String name = (String)session.getAttribute("name");
//			System.out.println("get name = "+name);
//		}

		IService service = serviceMgr.GetService(serviceName);
		if(service == null){
			model.addAttribute("errormsg", "can not find the service name :"+serviceName);
		    return "404";
		}
		
		Method method = this.GetMethod(service, methodName);
		if(method == null){
			model.addAttribute("errormsg", "can not find the method:"+methodName+"in the service: "+serviceName);
			 return "404";
		}
		return  CallObjMethod(service, method, req.getParameterMap());
	}
	
	private Method GetMethod(Object obj, String methodName)
	{
		Method[] methods = obj.getClass().getMethods();
		for(int i = 0 ; i < methods.length ; ++i){
			if(methods[i].getName().equals(methodName)){
				return methods[i];
			}
		}
		return null;
	}
	
	
	private  String CallObjMethod(IService service, Method method, Map<String,String[]> params,Model model) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, JsonParseException, JsonMappingException, IOException, InstantiationException
	{
		 Class<?> methosParamType = method.getParameterTypes()[0];  
		 
		  Object obj = methosParamType.newInstance();
	       try {
				BeanUtils.populate(obj, params);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}  
	   return  (String)method.invoke(service, obj,model);        
	}
	
	private  Object CallObjMethod(IService service, Method method, Map<String,String[]> params) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, JsonParseException, JsonMappingException, IOException, InstantiationException
	{
		int paramCount =  method.getParameterCount();
		if(paramCount > 0){
			 Class<?> methosParamType = method.getParameterTypes()[0]; 
	   
			  Object obj = methosParamType.newInstance();
			     if(IsStringType(methosParamType)){
			    	 Iterator<String[]>  stringParam = params.values().iterator();
			    	 if(stringParam.hasNext()){
			    	    String paramValue =  stringParam.next()[0];
			    	    return method.invoke(service, paramValue);   		
			    	 }
		          }
		       try {
					BeanUtils.populate(obj, params);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}  
		   return method.invoke(service, obj);   		
		}
		return method.invoke(service);	
	}
	
	private  boolean IsStringType(Class<?> clazz) 
	 {   
	     return clazz.equals(String.class);      
	 }
}
