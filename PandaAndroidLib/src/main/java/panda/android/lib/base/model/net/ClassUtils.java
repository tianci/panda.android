package panda.android.lib.base.model.net;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import panda.android.lib.base.util.Log;

/**
 * 获取任意java类实例的方法和属性，包括父类的方法和属性
 * @author
 */
@SuppressWarnings("rawtypes")
public class ClassUtils {

	private static final String TAG = ClassUtils.class.getSimpleName();

	public static void main (String[] args) {
		
		
		/*
		ClassUtils student = new ClassUtils();
		// 获取属性
		Map<String, Class> map = getClassFields (student.getClass(), true);
		for (Object key : map.keySet())	{
			System.out.println ("<field=" + key.toString() + "> <Type=" + map.get (key) + ">");
		}
		// 获取方法
		List<Method> methods = getClassMothds (student.getClass(), true);
		for (Method method : methods) {
			System.out.println (method.getName());
		}
		System.out.println ("方法总数：" + methods.size());
		*/
//		UpdateKnowledgelibModel model = new UpdateKnowledgelibModel();
//		List<String> list = new ArrayList<String>();
//		list.add("url1");
//		list.add("url2");
//		model.setCreateUserId(1);
//		model.setDescription("lalalalalla");
//		model.setTitle("tilelelelel");
//		model.setType(1);
//		model.setPicUrlList(list);
//		String res = obj2PostParams(model);
//		System.out.println(res);
	}
	
	/**
	 * 获取类实例的属性值
	 * @param clazz
	 *            类名
	 * @param includeParentClass
	 *            是否包括父类的属性值
	 * @return 类名.属性名=属性类型
	 */

	public static Map<String, Class> getClassFields (Class clazz, boolean includeParentClass) {
		
		Map<String, Class> map = new HashMap<String, Class>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			map.put (field.getName(), field.getType());
		}
		if (includeParentClass) {
			getParentClassFields (map, clazz.getSuperclass());
		}
		return map;
	}
	
	
	/**
	 * 获取类实例的父类的属性值
	 * @param map
	 *            类实例的属性值Map
	 * @param clazz
	 *            类名
	 * @return 类名.属性名=属性类型
	 */
	private static Map<String, Class> getParentClassFields (Map<String, Class> map, Class clazz) {
		
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields)
		{
			map.put (field.getName(), field.getType());
		}
		if (clazz.getSuperclass() == null)
		{
			return map;
		}
		getParentClassFields (map, clazz.getSuperclass());
		return map;
	}
	
	/**
	 * 获取类实例的方法
	 * @param clazz
	 *            类名
	 * @param includeParentClass
	 *            是否包括父类的方法
	 * @return List
	 */
	public static List<Method> getClassMothds (Class clazz, boolean includeParentClass)	{
		
		List<Method> list = new ArrayList<Method>();
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods)
		{
			list.add (method);
		}
		if (includeParentClass)
		{
			getParentClassMothds (list, clazz.getSuperclass());
		}
		return list;
	}
	
	/**
	 * 获取类实例的父类的方法
	 * @param list
	 *            类实例的方法List
	 * @param clazz
	 *            类名
	 * @return List
	 */
	private static List<Method> getParentClassMothds (List<Method> list, Class clazz)	{
		
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods)
		{
			list.add (method);
		}
		if (clazz.getSuperclass() == Object.class)
		{
			return list;
		}
		getParentClassMothds (list, clazz.getSuperclass());
		return list;
	}
	
	
	//******************************************************************************************************************//
	
	
	/**
	 * 获取类实例的属性值
	 * @param clazz 类名
	 * @param includeParentClass 是否包括父类的属性值
	 * @return 类名.属性名=属性类型
	 */
	public static Map<String, Object> getClassFieldsAndValue (Object obj, Class clazz, boolean includeParentClass) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (isNull(obj)) {
			return map;
		}
		
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			String name = field.getName();
			Object value = getFieldValueByName(field.getName(), obj);
			if (!isNull(value)) {
				map.put (name, value);
			}
		}
		if (includeParentClass) {
			getParentClassFieldsAndValue (map, obj, clazz);
		}
		return map;
	}
	
	
	/**默认方法
	 * 
	 */
	public static Map<String, Object> setObjParamsToMap(Object preParams) {
		
		Map<String, Object> mapParams = new HashMap<String, Object>();

		mapParams = getClassFieldsAndValue(preParams, preParams.getClass(), true);

		return mapParams;
	}
	
	/**
	 * 获取类实例的父类的属性值
	 * @param map
	 *            类实例的属性值Map
	 * @param clazz
	 *            类名
	 * @return 类名.属性名=属性类型
	 */
	private static Map<String, Object> getParentClassFieldsAndValue (Map<String, Object> map, Object obj, Class clazz) {
			
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String name = field.getName();
			Object value = getFieldValueByName(field.getName(), obj);
			Log.d(TAG, "name = " + name + ", value = " + value);
			if (!isNull(value)) {
				map.put (name, value);
			}
		}
		
		if (clazz.getSuperclass() == null)	{
			return map;
		}
		
		getParentClassFieldsAndValue (map, obj, clazz.getSuperclass());
		return map;
	}
	
	/**
	 * 通过类变量名获取类变量值
	 * @param fieldName
	 * @param o
	 * @return
	 */
	public static Object getFieldValueByName(String fieldName, Object o) { 
		
	    try {
	    	// 判断是否是第二个字母大写
	    	String firstLetter = fieldName.substring(0, 1);
	        if (!(fieldName.length() > 1 && Character.isUpperCase(fieldName.substring(1, 2).charAt(0)))) {
	        	firstLetter = firstLetter.toUpperCase();
	        }
	        
	        String getter = "get" + firstLetter + fieldName.substring(1);
	        Method method = o.getClass().getMethod(getter, new Class[] {});
	        Object value = method.invoke(o, new Object[] {});  
	        
	        return value;
	    } catch (Exception e) {
	        return null;
	    }
	}
	
	/**
	 * 判断Object是否为空
	 * @param pInput
	 * @return
	 */
	public static boolean isNull (Object pInput) {
		
		// 判断参数是否为空或者''
		if (pInput == null || "''".equals(pInput)) {
			return true;
		} 
		else if ("java.lang.String".equals(pInput.getClass().getName())){
			// 判断传入的参数的String类型的
			// 替换各种空格
			String tmpInput = Pattern.compile("[\\r|\\n|\\u3000]").matcher((String)pInput).replaceAll("");
			// 匹配空
			return Pattern.compile("^(\\s)*$").matcher(tmpInput).matches();
		} 
		else {
			// 方法类
			Method method = null;
			try {
				// 访问传入参数的size方法
				method = pInput.getClass().getMethod("size");
				// 判断size大小
				// size为0的场合
				if (Integer.parseInt(String.valueOf(method.invoke(pInput))) == 0) {
					return true;
				} else {
					return false;
				}
			} 
			catch (Exception e) {
			
				// 访问失败
				try {
					// 访问传入参数的getItemCount方法
					method = pInput.getClass().getMethod("getItemCount");
					// 判断size大小
					// getItemCount为0的场合
					if (Integer.parseInt(String.valueOf(method.invoke(pInput))) == 0) {
						return true;
					} else {
						return false;
					}
				} catch (Exception ex) {
					// 访问失败
					try{
						// 判断传入参数的长度
						// 长度为0的场合
						if (Array.getLength(pInput) == 0) {
							return true;
						} else {
							return false;
						}
					} catch (Exception exx) {
						// 访问失败
						try{
							// 访问传入参数的hasNext方法
							method = Iterator.class.getMethod("hasNext");
							// 转换hasNext的值
							return !Boolean.valueOf(String.valueOf(method.invoke(pInput)))? true : false;
						} catch (Exception exxx) {
							// 以上场合不满足返回假
							return false;
						}
					}
				}
			}
		}
	}

	public static String obj2PostParams (Object objInput) {
		
		
		if (objInput == null) {
			return "";
		}
		
		Map<String,Object> maps =  setObjParamsToMap(objInput);
		
		if (maps == null || maps.isEmpty()) {
			return "";
		}
		
		String res = "";
		for (String key : maps.keySet()) {
			try {
				res += (key + "=" + URLEncoder.encode(maps.get(key).toString(),"utf-8") + "&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
        }
		res = res.substring(0, res.length() - 1);
		return res;
	}
	
}