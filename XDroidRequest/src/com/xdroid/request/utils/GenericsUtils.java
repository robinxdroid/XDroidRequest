package com.xdroid.request.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Parse helper
 * @author Robin
 * @since 2015-11-10 14:50:21
 *
 */
public class GenericsUtils {

	/**
	 * Parsing the generic type
	 * @param cls 
	 * @return
	 */
	public static<T> Type parseGenericityType(T obj){
		Type mySuperClass = obj.getClass().getGenericSuperclass();
		Type type = ((ParameterizedType) mySuperClass).getActualTypeArguments()[0];
		return type;
	}
	
    /**   
     * 通过反射,获得定义Class时声明的父类的范型参数的类型.   
     * 如public BookManager extends GenricManager<Book>   
     *   
     * @param clazz The class to introspect   
     * @return the first generic declaration, or <code>Object.class</code> if cannot be determined   
     */  
    public static Class<?> getSuperClassGenricType(Class<?> clazz) {  
        return getSuperClassGenricType(clazz, 0);  
    }  
  
    /**   
     * 通过反射,获得定义Class时声明的父类的范型参数的类型.   
     * 如public BookManager extends GenricManager<Book>   
     *   
     * @param clazz clazz The class to introspect   
     * @param index the Index of the generic ddeclaration,start from 0.   
     */  
    public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) throws IndexOutOfBoundsException {  
  
        Type genType = clazz.getGenericSuperclass();  
  
        if (!(genType instanceof ParameterizedType)) {  
            return Object.class;  
        }  
  
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
  
        if (index >= params.length || index < 0) {  
            return Object.class;  
        }  
        if (!(params[index] instanceof Class)) {  
            return Object.class;  
        }  
        return (Class<?>) params[index];  
    }  
    
    @SuppressWarnings("unchecked")
	public static <T> Class<T> getViewClass(Class<?> klass) {
        Type type = klass.getGenericSuperclass();
        if(type == null || !(type instanceof ParameterizedType)) return null;
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        if(types == null || types.length == 0) return null;
        return (Class<T>) types[0];
    }
    
	public static <T> Type getBeanType(T listener) {
        Type type;
        try {
            Type[] typs = GenericsUtils.getGenericInterfaces(listener.getClass());
            if (typs != null) {
                type = typs[0];
            } else {
                type = GenericsUtils.getGenericSuperclass(listener.getClass())[0];
            }
        } catch (Exception e) {
            throw new RuntimeException("unknow type");
        }
        return type;
    }
    
    /**
     * Take the parent class generic
     * @param clazz
     * @return 
     */
    public static Type[] getGenericSuperclass(Class<?> clazz) {
        try {
            Type typeGeneric = clazz.getGenericSuperclass();
            if (typeGeneric != null) {
                if (typeGeneric instanceof ParameterizedType) {
                    return getGeneric((ParameterizedType) typeGeneric);
                }
            }
        } catch (Exception e) {
            CLog.e("ClassUtils", e);
        }
        return null;
    }
    /**
     * Take the parent interface generic
     * @param clazz
     * @return 
     */
    public static Type[] getGenericInterfaces(Class<?> clazz) {
        try {
            Type typeGeneric = clazz.getGenericInterfaces()[0];
            if (typeGeneric != null) {
                if (typeGeneric instanceof ParameterizedType) {
                    return getGeneric((ParameterizedType) typeGeneric);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
    /**
     * Take a generic
     * @param type
     * @return 
     */
    public static Type[] getGeneric(ParameterizedType type) {
        try {
            if (type != null) {
                Type[] typeArgs = type.getActualTypeArguments();
                if (typeArgs != null && typeArgs.length > 0) {
                    Type[] args = new Type[typeArgs.length];
                    for (int i=0; i<typeArgs.length; i++) {
//                        if ("libcore.reflect.ParameterizedTypeImpl".equals(arg.getClass().getName())) {
//                            arg = ((ParameterizedType) arg).getRawType();
//                        }
                        args[i] = typeArgs[i];
                    }
                    return args;
                }
            }
        } catch (Exception e) {
            CLog.e("GenericsUtils", e);
        }
        return null;
    }

}
