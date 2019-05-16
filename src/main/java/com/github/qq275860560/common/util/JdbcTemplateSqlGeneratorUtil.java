package com.github.qq275860560.common.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Slf4j
public class JdbcTemplateSqlGeneratorUtil {

	public static String url;
	public static String username;
	public static String password;
	public static String driverClassName;
	public static String schemaName;

	public static String tableName;
	public static String modelName;

	public static void main(String[] args) throws Exception {
		JdbcTemplateSqlGeneratorUtil.url = "jdbc:mysql://10.18.96.50:3306/rest_home_hz?autoReconnect=true&useUnicode=true&characterEncoding=utf-8";
		JdbcTemplateSqlGeneratorUtil.username = "resthome";
		JdbcTemplateSqlGeneratorUtil.password = "resthome&*()";
		JdbcTemplateSqlGeneratorUtil.driverClassName = "com.mysql.jdbc.Driver";
		JdbcTemplateSqlGeneratorUtil.schemaName = "rest_home_hz";
		JdbcTemplateSqlGeneratorUtil.tableName = "t_user";
		JdbcTemplateSqlGeneratorUtil.modelName = "User";
		JdbcTemplateSqlGeneratorUtil.generate();
	}

	private static void generate() throws Exception {
		log.info(countInterface(schemaName, tableName, modelName) + "\n"
				+ checkInterface(schemaName, tableName, modelName) + "\n"
				+ deleteInterface(schemaName, tableName, modelName) + "\n"
				+ getInterface(schemaName, tableName, modelName) + "\n"
				+ getByKeyValueInterface(schemaName, tableName, modelName) + "\n"
				+ saveInterface(schemaName, tableName, modelName) + "\n"
				+ updateInterface(schemaName, tableName, modelName) + "\n"
				+ listInterface(schemaName, tableName, modelName) + "\n"
				+ pageInterface(schemaName, tableName, modelName));

		log.info("\n"+"@Autowired"+"\n"+"private JdbcTemplate jdbcTemplate;"+"\n"+countImplement(schemaName, tableName, modelName) + "\n"
				+ checkImplement(schemaName, tableName, modelName) + "\n"
				+ deleteImplement(schemaName, tableName, modelName) + "\n"
				+ getImplement(schemaName, tableName, modelName) + "\n"
				+ getByKeyValueImplement(schemaName, tableName, modelName) + "\n"
				+ saveImplement(schemaName, tableName, modelName) + "\n"
				+ updateImplement(schemaName, tableName, modelName) + "\n"
				+ listImplement(schemaName, tableName, modelName) + "\n"
				+ pageImplement(schemaName, tableName, modelName));

		log.info("" + assignNull(schemaName, tableName));
		log.info("" + assignFromMap(schemaName, tableName));
	}

	private static StringBuilder assignNull(String schemaName, String tableName) throws Exception {
		List<String[]> list = getColumn(schemaName, tableName);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");
		for (String[] array : list) {
			sb1.append(array[0].equals("Date") ? "String" : array[0]).append(" ").append(array[1]).append("=")
					.append("null").append(";").append("\n");
		}
		return sb1;
	}

	private static StringBuilder assignFromMap(String schemaName, String tableName) throws Exception {
		List<String[]> list = getColumn(schemaName, tableName);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");
		for (String[] array : list) {
			sb1.append(array[0].equals("Date") ? "String" : array[0]).append(" ").append(array[1]).append("=")
					.append("(" + (array[0].equals("Date") ? "String" : array[0]) + ")map.get(\"" + array[1] + "\")")
					.append(";").append("\n");
		}
		return sb1;
	}

	private static StringBuilder pageInterface(String schemaName, String tableName, String modelName) throws Exception {
		List<String[]> list = getColumn(schemaName, tableName);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");

		if (modelName == null) {
			sb1.append("public Map<String,Object> page( ");
		} else {
			sb1.append("public Map<String,Object> page" + modelName + "( ");
		}
		sb1.append(")  throws Exception ; ");
		StringBuilder sb3 = new StringBuilder();
		return sb3;
	}

	private static StringBuilder pageImplement(String schemaName, String tableName, String modelName) throws Exception {
		List<String[]> list = getColumn(schemaName, tableName);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");
		//sb1.append("@Override").append("\n");
		if (modelName == null) {
			sb1.append("public Map<String,Object> page( ");
		} else {
			sb1.append("public Map<String,Object> page" + modelName + "( ");
		}

		for (int i = 0; i < list.size(); i++) {
			String[] array = list.get(i);
			if (array[0].equals("Date")) {
				sb1.append("String").append(" ")
						.append("start" + array[1].substring(0, 1).toUpperCase() + array[1].substring(1)).append(",");
				sb1.append("String").append(" ")
						.append("end" + array[1].substring(0, 1).toUpperCase() + array[1].substring(1)).append(",");
			} else {
				sb1.append(array[0]).append(" ").append(array[1]).append(",");
			}

		}
		sb1.append("Integer").append(" ").append("pageNum").append(",");
		sb1.append("Integer").append(" ").append("pageSize").append(",");
		sb1.delete(sb1.length() - 1, sb1.length());
		sb1.append(") throws Exception  {").append("\n");

		sb1.append("    if(pageNum==null) pageNum=1;//取名pageNum为了兼容mybatis-pageHelper中的page对象的pageNum,注意spring的PageRequest使用page表示页号,综合比较，感觉pageNum更加直观,不需要看上下文能猜出字段是页号").append("\n");
		sb1.append("    if(pageSize==null)pageSize=10;//取名pageSize为了兼容mybatis-pageHelper中的page对象的pageSize,注意spring的PageRequest使用size表示页数量，综合比较，感觉pageSize会更加直观,不需要看上下文能猜出字段是分页时当前页的数量").append("\n");
		sb1.append("    int from = (pageNum-1)*pageSize;").append("\n");
		sb1.append("    int size = pageSize;").append("\n");

		sb1.append("    StringBuilder sb  = new StringBuilder();").append("\n");
		sb1.append("    List<Object> condition = new ArrayList<Object>();").append("\n");
		sb1.append("    sb.append(\" SELECT ");

		for (String[] array : list) {
			if (array[0].equals("Date")) {
				sb1.append("date_format(" + array[1] + ",	'%Y-%m-%d %H:%i:%s') " + array[1]).append(",");
			} else {
				sb1.append(array[1]).append(",");
			}

		}
		sb1.delete(sb1.length() - 1, sb1.length());
		sb1.append(" from " + tableName + " where 1=1 \"); ").append("\n");

		for (String[] array : list) {
			if (array[0].equals("Date")) {

				sb1.append("    if (StringUtils.isNotBlank(start" + array[1].substring(0, 1).toUpperCase()
						+ array[1].substring(1) + ")) {").append("\n");
				sb1.append("    	sb .append(\" and " + array[1] + " >=  ?  \");").append("\n");
				sb1.append("    	condition.add(start" + array[1].substring(0, 1).toUpperCase()
						+ array[1].substring(1) + ");").append("\n");
				sb1.append("    }").append("\n");
				sb1.append("    if (StringUtils.isNotBlank(end" + array[1].substring(0, 1).toUpperCase()
						+ array[1].substring(1) + ")) {").append("\n");
				sb1.append("    	sb .append(\" and " + array[1] + " <=  ?  \");").append("\n");
				sb1.append("    	condition.add(end" + array[1].substring(0, 1).toUpperCase() + array[1].substring(1)
						+ ");").append("\n");
				sb1.append("    }").append("\n");

			} else if (array[0].equals("String")) {

				sb1.append("    if (StringUtils.isNotBlank(" + array[1] + ")) {").append("\n");
				sb1.append("    	sb .append(\" and " + array[1] + " like ? \");").append("\n");
				sb1.append("    	condition.add(\"%\"+" + array[1] + "+\"%\");").append("\n");
				sb1.append("    }").append("\n");
			} else if (array[0].equals("Integer")) {
				sb1.append("    if (" + array[1] + "!=null) {").append("\n");
				sb1.append("    	sb .append(\" and " + array[1] + " = ? \");").append("\n");
				sb1.append("    	condition.add(" + array[1] + ");").append("\n");
				sb1.append("    }").append("\n");
			} else if (array[0].equals("Double")) {
				sb1.append("    if (" + array[1] + "!=null) {").append("\n");
				sb1.append("    	sb .append(\" and " + array[1] + " = ? \");").append("\n");
				sb1.append("    	condition.add(" + array[1] + ");").append("\n");
				sb1.append("    }").append("\n");
			}

		}

		sb1.append("    String countSql = \"select count(1) count from ( \" + sb.toString()+\") t\";").append("\n");
		sb1.append("    int count = jdbcTemplate.queryForObject(countSql, condition.toArray(),Integer.class);")
				.append("\n");

		boolean b = false;
		for (String[] array : list) {
			if (array[0].equals("Date")) {
				sb1.append("    sb.append(\" order by " + array[1] + " desc \");").append("\n");
				b = true;
				break;
			}
		}
		if (b == false) {
			sb1.append("    sb.append(\" order by id desc \");").append("\n");
		}
		sb1.append("    sb.append(\" limit ? ,?  \");").append("\n");
		sb1.append("    condition.add(from);").append("\n");
		sb1.append("    condition.add(size);").append("\n");

		sb1.append("    log.info(\"sql=\" + sb.toString());").append("\n");
		sb1.append("    log.info(\"condition=\" + Arrays.deepToString(condition.toArray()));").append("\n");
		sb1.append(
				"    List<Map<String, Object>> list = jdbcTemplate.queryForList( sb.toString(), condition.toArray());")
				.append("\n");

		sb1.append("    Map<String, Object> map = new HashMap<String, Object>();").append("\n");
		sb1.append("    map.put(\"total\", count);//取名total为了兼容mybatis-pageHelper中的page对象的total,spring框架的PageImpl也使用total").append("\n");
		sb1.append("    map.put(\"list\", list);//不同的框架取名不一样，可以把list改成array,rows,data,content,result等,spring框架使用的是content,mybatis因为page是继承ArrayList，字段命名乱七八糟，有时pages，有时pageList，有时result，综上感觉list会更加直观和简洁,不需要看上下文能猜出字段是列表").append("\n");
		sb1.append("    return map;").append("\n");

		sb1.append("\n").append("}");
		return sb1;
	}

	private static StringBuilder listInterface(String schemaName, String tableName, String modelName) throws Exception {
		List<String[]> list = getColumn(schemaName, tableName);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");

		if (modelName == null) {
			sb1.append("public List<Map<String,Object>> list( ");
		} else {
			sb1.append("public List<Map<String,Object>> list" + modelName + "( ");
		}
		sb1.append(")  throws Exception ; ");

		return sb1;
	}

	private static StringBuilder listImplement(String schemaName, String tableName, String modelName) throws Exception {
		List<String[]> list = getColumn(schemaName, tableName);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");
		//sb1.append("@Override").append("\n");
		if (modelName == null) {
			sb1.append("public List<Map<String,Object>> list( ");
		} else {
			sb1.append("public List<Map<String,Object>> list" + modelName + "( ");
		}

		for (int i = 0; i < list.size(); i++) {
			String[] array = list.get(i);
			if (array[0].equals("Date")) {
				sb1.append("String").append(" ")
						.append("start" + array[1].substring(0, 1).toUpperCase() + array[1].substring(1)).append(",");
				sb1.append("String").append(" ")
						.append("end" + array[1].substring(0, 1).toUpperCase() + array[1].substring(1)).append(",");
			} else {
				sb1.append(array[0]).append(" ").append(array[1]).append(",");
			}

		}
		sb1.delete(sb1.length() - 1, sb1.length());
		sb1.append(") throws Exception  {").append("\n");

		sb1.append("    StringBuilder sb  = new StringBuilder();").append("\n");
		sb1.append("    List<Object> condition = new ArrayList<Object>();").append("\n");
		sb1.append("    sb.append(\" SELECT ");

		for (String[] array : list) {
			if (array[0].equals("Date")) {
				sb1.append("date_format(" + array[1] + ",	'%Y-%m-%d %H:%i:%s') " + array[1]).append(",");
			} else {
				sb1.append(array[1]).append(",");
			}

		}
		sb1.delete(sb1.length() - 1, sb1.length());
		sb1.append(" from " + tableName + " where 1=1 \"); ").append("\n");

		for (String[] array : list) {
			if (array[0].equals("Date")) {

				sb1.append("    if (StringUtils.isNotBlank(start" + array[1].substring(0, 1).toUpperCase()
						+ array[1].substring(1) + ")) {").append("\n");
				sb1.append("    	sb .append(\" and " + array[1] + " >=  ?  \");").append("\n");
				sb1.append("    	condition.add(start" + array[1].substring(0, 1).toUpperCase()
						+ array[1].substring(1) + ");").append("\n");
				sb1.append("    }").append("\n");
				sb1.append("    if (StringUtils.isNotBlank(end" + array[1].substring(0, 1).toUpperCase()
						+ array[1].substring(1) + ")) {").append("\n");
				sb1.append("    	sb .append(\" and " + array[1] + " <=  ?  \");").append("\n");
				sb1.append("    	condition.add(end" + array[1].substring(0, 1).toUpperCase() + array[1].substring(1)
						+ ");").append("\n");
				sb1.append("    }").append("\n");

			} else if (array[0].equals("String")) {

				sb1.append("    if (StringUtils.isNotBlank(" + array[1] + ")) {").append("\n");
				sb1.append("    	sb .append(\" and " + array[1] + " like ? \");").append("\n");
				sb1.append("    	condition.add(\"%\"+" + array[1] + "+\"%\");").append("\n");
				sb1.append("    }").append("\n");
			} else if (array[0].equals("Integer")) {
				sb1.append("    if (" + array[1] + "!=null) {").append("\n");
				sb1.append("    	sb .append(\" and " + array[1] + " = ? \");").append("\n");
				sb1.append("    	condition.add(" + array[1] + ");").append("\n");
				sb1.append("    }").append("\n");
			} else if (array[0].equals("Double")) {
				sb1.append("    if (" + array[1] + "!=null) {").append("\n");
				sb1.append("    	sb .append(\" and " + array[1] + " = ? \");").append("\n");
				sb1.append("    	condition.add(" + array[1] + ");").append("\n");
				sb1.append("    }").append("\n");
			}

		}

	 
		sb1.append("    log.info(\"sql=\" + sb.toString());").append("\n");
		sb1.append("    log.info(\"condition=\" + Arrays.deepToString(condition.toArray()));").append("\n");
		sb1.append("    return jdbcTemplate.queryForList( sb.toString(), condition.toArray());").append("\n");
		sb1.append("\n").append("}");
		return sb1;
	}

	private static StringBuilder getInterface(String schemaName, String tableName, String modelName) throws Exception {
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");

		if (modelName == null) {
			sb1.append("public Map<String,Object> get(Object id) throws Exception ; ").append("\n");
		} else {
			sb1.append("public Map<String,Object> get" + modelName + "(Object id) throws Exception ; ").append("\n");
		}
		return sb1;
	}

	private static StringBuilder getImplement(String schemaName, String tableName, String modelName) throws Exception {
		List<String[]> list = getColumn(schemaName, tableName);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");

		//sb1.append("@Override").append("\n");
		if (modelName == null) {
			sb1.append("public Map<String,Object> get(Object id) throws Exception { ").append("\n");
		} else {
			sb1.append("public Map<String,Object> get" + modelName + "(Object id) throws Exception { ").append("\n");
		}
		sb1.append("    StringBuilder sb  = new StringBuilder();").append("\n");
		sb1.append("    List<Object> condition = new ArrayList<Object>();").append("\n");
		sb1.append("    sb.append(\" SELECT ");

		for (String[] array : list) {
			if (array[0].equals("Date")) {
				sb1.append("date_format(" + array[1] + ",	'%Y-%m-%d %H:%i:%s') " + array[1]).append(",");
			} else {
				sb1.append(array[1]).append(",");
			}

		}
		sb1.delete(sb1.length() - 1, sb1.length());
		sb1.append(" from " + tableName + " where 1=1 \"); ").append("\n");

		sb1.append("    if (StringUtils.isNotBlank(id)) {").append("\n");
		sb1.append("    	sb .append(\" and id = ? \");").append("\n");
		sb1.append("    	condition.add(id);").append("\n");
		sb1.append("    }").append("\n");
		sb1.append("    sb.append(\" limit ? ,?  \");").append("\n");
		sb1.append("    condition.add(0);").append("\n");
		sb1.append("    condition.add(1);").append("\n");
		sb1.append("    log.info(\"sql=\" + sb.toString());").append("\n");
		sb1.append("    log.info(\"condition=\" + Arrays.deepToString(condition.toArray()));").append("\n");
		sb1.append("    Map<String,Object> map = Collections.EMPTY_MAP;").append("\n");
		sb1.append("    try{").append("\n");
		sb1.append("    	map =jdbcTemplate.queryForMap( sb.toString(), condition.toArray());").append("\n");
		sb1.append("    }catch (Exception e) {").append("\n");
		sb1.append("    }").append("\n");
		sb1.append("    return map;").append("\n");
		sb1.append("}");
		return sb1;
	}

	private static StringBuilder getByKeyValueInterface(String schemaName, String tableName, String modelName)
			throws Exception {
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");

		if (modelName == null) {
			sb1.append("public Map<String,Object> getByKeyValue(String key,Object value) throws Exception ; ")
					.append("\n");
		} else {
			sb1.append("public Map<String,Object> get" + modelName
					+ "ByKeyValue(String key,Object value) throws Exception ; ").append("\n");
		}
		return sb1;
	}

	private static StringBuilder getByKeyValueImplement(String schemaName, String tableName, String modelName)
			throws Exception {
		List<String[]> list = getColumn(schemaName, tableName);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");

		//sb1.append("@Override").append("\n");
		if (modelName == null) {
			sb1.append("public Map<String,Object> getByKeyValue(String key,Object value) throws Exception { ")
					.append("\n");
		} else {
			sb1.append("public Map<String,Object> get" + modelName
					+ "ByKeyValue(String key,Object value) throws Exception { ").append("\n");
		}
		sb1.append("    StringBuilder sb  = new StringBuilder();").append("\n");
		sb1.append("    List<Object> condition = new ArrayList<Object>();").append("\n");
		sb1.append("    sb.append(\" SELECT ");

		for (String[] array : list) {
			if (array[0].equals("Date")) {
				sb1.append("date_format(" + array[1] + ",	'%Y-%m-%d %H:%i:%s') " + array[1]).append(",");
			} else {
				sb1.append(array[1]).append(",");
			}

		}
		sb1.delete(sb1.length() - 1, sb1.length());
		sb1.append(" from " + tableName + " where 1=1 \"); ").append("\n");

		sb1.append("   	sb .append(\" and \"+key+\" = ? \");").append("\n");
		sb1.append("   	condition.add(value);").append("\n");

		sb1.append("    sb.append(\" limit ? ,?  \");").append("\n");
		sb1.append("    condition.add(0);").append("\n");
		sb1.append("    condition.add(1);").append("\n");
		sb1.append("    log.info(\"sql=\" + sb.toString());").append("\n");
		sb1.append("    log.info(\"condition=\" + Arrays.deepToString(condition.toArray()));").append("\n");
		sb1.append("    Map<String,Object> map = Collections.EMPTY_MAP;").append("\n");
		sb1.append("    try{").append("\n");
		sb1.append("    	map =jdbcTemplate.queryForMap( sb.toString(), condition.toArray());").append("\n");
		sb1.append("    }catch (Exception e) {").append("\n");
		sb1.append("    }").append("\n");
		sb1.append("    return map;").append("\n");
		sb1.append("}");
		return sb1;
	}

	private static StringBuilder countInterface(String schemaName, String tableName, String modelName)
			throws Exception {
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");

		if (modelName == null) {
			sb1.append("public int count(String name) throws Exception ; ").append("\n");
		} else {
			sb1.append("public int count" + modelName + "(String name) throws Exception ; ").append("\n");
		}
		return sb1;
	}

	private static StringBuilder countImplement(String schemaName, String tableName, String modelName)
			throws Exception {
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");

		//sb1.append("@Override").append("\n");
		if (modelName == null) {
			sb1.append("public int count(String name) throws Exception { ").append("\n");
		} else {
			sb1.append("public int count" + modelName + "(String name) throws Exception { ").append("\n");
		}
		sb1.append("    StringBuilder sb  = new StringBuilder();").append("\n");
		sb1.append("    List<Object> condition = new ArrayList<Object>();").append("\n");
		sb1.append("    sb.append(\" SELECT count(1) count");

		sb1.append(" from " + tableName + " where 1=1 \"); ").append("\n");
		sb1.append("    sb .append(\" and name = ? \");").append("\n");
		sb1.append("    condition.add(name);").append("\n");
		sb1.append("    log.info(\"sql=\" + sb.toString());").append("\n");
		sb1.append("    log.info(\"condition=\" + Arrays.deepToString(condition.toArray()));").append("\n");
		sb1.append("    return jdbcTemplate.queryForObject( sb.toString(), condition.toArray(),Integer.class);")
				.append("\n");
		sb1.append("}");
		return sb1;
	}

	private static StringBuilder checkInterface(String schemaName, String tableName, String modelName)
			throws Exception {
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");

		if (modelName == null) {
			sb1.append("public boolean check(String id,String name) throws Exception ; ").append("\n");
		} else {
			sb1.append("public boolean check" + modelName + "(String id,String name) throws Exception ; ").append("\n");
		}
		return sb1;
	}

	private static StringBuilder checkImplement(String schemaName, String tableName, String modelName)
			throws Exception {// 如果id为null，说明是新增界面调用，否则为更新界面调用
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");

		//sb1.append("@Override").append("\n");
		if (modelName == null) {
			sb1.append("public boolean check(String id,String name) throws Exception { ").append("\n");
		} else {
			sb1.append("public boolean check" + modelName + "(String id,String name) throws Exception { ").append("\n");
		}
		sb1.append("    StringBuilder sb  = new StringBuilder();").append("\n");
		sb1.append("    List<Object> condition = new ArrayList<Object>();").append("\n");
		sb1.append("    sb.append(\" SELECT count(1) count ");

		sb1.append(" from " + tableName + " where 1=1 \"); ").append("\n");

		sb1.append("    if (StringUtils.isNotBlank(id)) {").append("\n");
		sb1.append("    	sb .append(\" and id != ? \");").append("\n");
		sb1.append("    	condition.add(id);").append("\n");
		sb1.append("    }").append("\n");

		sb1.append("    sb .append(\" and name= ? \");").append("\n");
		sb1.append("    condition.add(name);").append("\n");

		sb1.append("    log.info(\"sql=\" + sb.toString());").append("\n");
		sb1.append("    log.info(\"condition=\" + Arrays.deepToString(condition.toArray()));").append("\n");
		sb1.append("    int count = jdbcTemplate.queryForObject( sb.toString(), condition.toArray(),Integer.class);")
				.append("\n");
		sb1.append("    if(count>0) return false;").append("\n");
		sb1.append("    else return true;").append("\n");
		sb1.append("}");
		return sb1;
	}

	private static StringBuilder deleteInterface(String schemaName, String tableName, String modelName)
			throws Exception {
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");

		if (modelName == null) {
			sb1.append("public int delete(String id) throws Exception ; ").append("\n");
		} else {
			sb1.append("public int delete" + modelName + "(String id) throws Exception ; ").append("\n");
		}
		return sb1;
	}

	private static StringBuilder deleteImplement(String schemaName, String tableName, String modelName)
			throws Exception {
		List<String[]> list = getColumn(schemaName, tableName);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");

		//sb1.append("@Override").append("\n");
		if (modelName == null) {
			sb1.append("public int delete(String id) throws Exception { ").append("\n");
		} else {
			sb1.append("public int delete" + modelName + "(String id) throws Exception { ").append("\n");
		}
		sb1.append("    StringBuilder sb  = new StringBuilder();").append("\n");
		sb1.append("    List<Object> condition = new ArrayList<Object>();").append("\n");
		sb1.append("    sb.append(\" delete  from " + tableName + " where 1=1 \"); ").append("\n");

		sb1.append("    sb .append(\" and id = ? \");").append("\n");
		sb1.append("    condition.add(id);").append("\n");

		sb1.append("    log.info(\"sql=\" + sb.toString());").append("\n");
		sb1.append("    log.info(\"condition=\" + Arrays.deepToString(condition.toArray()));").append("\n");
		sb1.append("    return jdbcTemplate.update( sb.toString(), condition.toArray());").append("\n");
		sb1.append("}");
		return sb1;
	}

	private static StringBuilder saveInterface2(String schemaName, String tableName, String modelName)
			throws Exception {
		List<String[]> list = getColumn(schemaName, tableName);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");
		if (modelName == null) {
			sb1.append("public int save( ");
		} else {
			sb1.append("public int save" + modelName + "( ");
		}
		for (int i = 0; i < list.size(); i++) {
			String[] array = list.get(i);
			sb1.append(array[0].equals("Date") ? "String" : array[0]).append(" ").append(array[1]).append(",");

		}
		sb1.delete(sb1.length() - 1, sb1.length());
		sb1.append(") throws Exception  ;");
		return sb1;
	}

	private static StringBuilder saveImplement2(String schemaName, String tableName, String modelName)
			throws Exception {
		List<String[]> list = getColumn(schemaName, tableName);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");
		//sb1.append("@Override").append("\n");
		if (modelName == null) {
			sb1.append("public int save( ");
		} else {
			sb1.append("public int save" + modelName + "( ");
		}
		for (int i = 0; i < list.size(); i++) {
			String[] array = list.get(i);
			sb1.append((array[0].equals("Date") ? "String" : array[0])).append(" ").append(array[1]).append(",");

		}
		sb1.delete(sb1.length() - 1, sb1.length());
		sb1.append(") throws Exception  {").append("\n");

		sb1.append("    ").append("StringBuilder sb1 = new StringBuilder();").append("\n");
		sb1.append("    ").append("StringBuilder sb2 = new StringBuilder();").append("\n");
		sb1.append("    ").append("List<Object> condition = new ArrayList<Object>();").append("\n");

		for (int i = 0; i < list.size(); i++) {
			String[] array = list.get(i);

			sb1.append("    ").append("sb1.append(\"" + array[1] + "\").append(\",\");").append("\n");
			sb1.append("    ").append("sb2.append(\"?,\");").append("\n");
			sb1.append("    ").append("condition.add(" + array[1] + ");").append("\n");
			sb1.append("\n");

		}

		sb1.append("    ").append("if (sb1.length() > 0)").append("\n");
		sb1.append("    ").append("    sb1.deleteCharAt(sb1.length() - 1);").append("\n");
		sb1.append("    ").append("if (sb2.length() > 0)").append("\n");
		sb1.append("    ").append("    sb2.deleteCharAt(sb2.length() - 1);").append("\n");

		sb1.append("    ").append("String sql = \"insert into " + tableName
				+ "(\" + sb1.toString() + \") values(\" + sb2.toString() + \")\";").append("\n");
		sb1.append("    ").append("log.info(\"sql=\" + sql);").append("\n");
		sb1.append("    ").append("log.info(\"condition=\" + Arrays.deepToString(condition.toArray()));").append("\n");
		sb1.append("    ").append("return jdbcTemplate.update( sql, condition.toArray());").append("\n");

		sb1.append("\n").append("}");

		return sb1;
	}

	private static StringBuilder saveInterface(String schemaName, String tableName, String modelName) throws Exception {
		List<String[]> list = getColumn(schemaName, tableName);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");
		if (modelName == null) {
			sb1.append("public int save( ");
		} else {
			sb1.append("public int save" + modelName + "( ");
		}

		sb1.append("Map<String,Object> map) throws Exception  ;");
		return sb1;
	}

	private static StringBuilder saveImplement(String schemaName, String tableName, String modelName) throws Exception {
		List<String[]> list = getColumn(schemaName, tableName);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");
		//sb1.append("@Override").append("\n");
		if (modelName == null) {
			sb1.append("public int save( ");
		} else {
			sb1.append("public int save" + modelName + "( ");
		}
		sb1.append("Map<String,Object> map)  throws Exception  {").append("\n");

		sb1.append("    ").append("StringBuilder sb1 = new StringBuilder();").append("\n");
		sb1.append("    ").append("StringBuilder sb2 = new StringBuilder();").append("\n");
		sb1.append("    ").append("List<Object> condition = new ArrayList<Object>();").append("\n");

		for (int i = 0; i < list.size(); i++) {
			String[] array = list.get(i);

			sb1.append("    ").append("sb1.append(\"" + array[1] + "\").append(\",\");").append("\n");
			sb1.append("    ").append("sb2.append(\"?,\");").append("\n");
			sb1.append("    ").append("condition.add(map.get(\"" + array[1] + "\"));").append("\n");
			sb1.append("\n");

		}

		sb1.append("    ").append("if (sb1.length() > 0)").append("\n");
		sb1.append("    ").append("    sb1.deleteCharAt(sb1.length() - 1);").append("\n");
		sb1.append("    ").append("if (sb2.length() > 0)").append("\n");
		sb1.append("    ").append("    sb2.deleteCharAt(sb2.length() - 1);").append("\n");

		sb1.append("    ").append("String sql = \"insert into " + tableName
				+ "(\" + sb1.toString() + \") values(\" + sb2.toString() + \")\";").append("\n");
		sb1.append("    ").append("log.info(\"sql=\" + sql);").append("\n");
		sb1.append("    ").append("log.info(\"condition=\" + Arrays.deepToString(condition.toArray()));").append("\n");
		sb1.append("    ").append("return jdbcTemplate.update( sql, condition.toArray());").append("\n");

		sb1.append("\n").append("}");

		return sb1;
	}

	private static StringBuilder updateInterface2(String schemaName, String tableName, String modelName)
			throws Exception {
		List<String[]> list = getColumn(schemaName, tableName);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");
		if (modelName == null) {
			sb1.append("public int update( ");
		} else {
			sb1.append("public int update" + modelName + "( ");
		}
		for (String[] array : list) {
			sb1.append((array[0].equals("Date") ? "String" : array[0])).append(" ").append(array[1]).append(",");
		}
		sb1.delete(sb1.length() - 1, sb1.length());
		sb1.append(") throws Exception  ;");
		return sb1;
	}

	private static StringBuilder updateImplement2(String schemaName, String tableName, String modelName)
			throws Exception {
		List<String[]> list = getColumn(schemaName, tableName);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");
		//sb1.append("@Override").append("\n");
		if (modelName == null) {
			sb1.append("public int update( ");
		} else {
			sb1.append("public int update" + modelName + "( ");
		}
		for (String[] array : list) {
			sb1.append(array[0].equals("Date") ? "String" : array[0]).append(" ").append(array[1]).append(",");
		}
		sb1.delete(sb1.length() - 1, sb1.length());
		sb1.append(") throws Exception  {").append("\n");
		sb1.append("    ").append("StringBuilder sb = new StringBuilder();").append("\n");
		sb1.append("    ").append("List<Object> condition = new ArrayList<Object>();").append("\n");

		for (String[] array : list) {
			if (array[1].equals("id"))
				continue;
			sb1.append("    ").append("sb.append(\" " + array[1] + " = ? ,\");").append("\n");
			sb1.append("    ").append("condition.add(" + array[1] + ");").append("\n");
			sb1.append("    ").append("\n");
		}

		sb1.append("    ").append("if (sb.length() > 0)").append("\n");
		sb1.append("    ").append("    sb.deleteCharAt(sb.length() - 1);	").append("\n");

		sb1.append("    ")
				.append("String sql = \"update " + tableName + " set \" + sb.toString() + \" where    id=?\";")
				.append("\n");
		sb1.append("    ").append("condition.add(id);").append("\n");
		sb1.append("    ").append("log.info(\"sql=\" + sql);").append("\n");
		sb1.append("    ").append("log.info(\"condition=\" + Arrays.deepToString(condition.toArray()));").append("\n");
		sb1.append("    ").append("return jdbcTemplate.update(  sql, condition.toArray());").append("\n");
		sb1.append("}");
		return sb1;
	}

	private static StringBuilder updateInterface(String schemaName, String tableName, String modelName)
			throws Exception {
		List<String[]> list = getColumn(schemaName, tableName);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");
		if (modelName == null) {
			sb1.append("public int update( ");
		} else {
			sb1.append("public int update" + modelName + "( ");
		}
		sb1.append("Map<String,Object> map) throws Exception  ;");
		return sb1;
	}

	private static StringBuilder updateImplement(String schemaName, String tableName, String modelName)
			throws Exception {
		List<String[]> list = getColumn(schemaName, tableName);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");
		//sb1.append("@Override").append("\n");
		if (modelName == null) {
			sb1.append("public int update( ");
		} else {
			sb1.append("public int update" + modelName + "( ");
		}

		sb1.append("Map<String,Object> map) throws Exception  {").append("\n");
		sb1.append("    ").append("StringBuilder sb = new StringBuilder();").append("\n");
		sb1.append("    ").append("List<Object> condition = new ArrayList<Object>();").append("\n");

		for (String[] array : list) {
			if (array[1].equals("id"))
				continue;
			sb1.append("    ").append("sb.append(\" " + array[1] + " = ? ,\");").append("\n");
			sb1.append("    ").append("condition.add(map.get(\"" + array[1] + "\"));").append("\n");
			sb1.append("    ").append("\n");
		}

		sb1.append("    ").append("if (sb.length() > 0)").append("\n");
		sb1.append("    ").append("    sb.deleteCharAt(sb.length() - 1);	").append("\n");

		sb1.append("    ")
				.append("String sql = \"update " + tableName + " set \" + sb.toString() + \" where    id=?\";")
				.append("\n");
		sb1.append("    ").append("condition.add(map.get(\"id\"));").append("\n");
		sb1.append("    ").append("log.info(\"sql=\" + sql);").append("\n");
		sb1.append("    ").append("log.info(\"condition=\" + Arrays.deepToString(condition.toArray()));").append("\n");
		sb1.append("    ").append("return jdbcTemplate.update(  sql, condition.toArray());").append("\n");
		sb1.append("}");
		return sb1;
	}

	private static List<String[]> getColumn(String schemaName, String tableName) throws Exception {
		List<String[]> list = new ArrayList<>();
		Connection connection = null;
		try {
			Class.forName(driverClassName);
			connection = DriverManager.getConnection(url, username, password);
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet rssultSet = databaseMetaData.getColumns(null, schemaName, tableName, "%");
			while (rssultSet.next()) {
				String[] array = new String[2];
				String columnTypeName = rssultSet.getString("TYPE_NAME");// java.sql.Types类型 名称
				String javaTypeName = null;
				array[1] = rssultSet.getString("COLUMN_NAME");// 列名
				javaTypeName = convert(columnTypeName);
				array[0] = javaTypeName;
				list.add(array);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (Exception e2) {
					log.error("", e2);
				}
		}
		return list;
	}

	private static List<String[]> getColumn2(String tableName) throws Exception {
		String sql = "select * from " + tableName + " limit 0,1";
		List<String[]> list = new ArrayList<>();
		Connection connection = null;
		try {
			Class.forName(driverClassName);
			connection = DriverManager.getConnection(url, username, password);
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			int columns = resultSetMetaData.getColumnCount();
			while (resultSet.next()) {
				for (int i = 1; i <= columns; i++) {
					String[] array = new String[2];
					String columnTypeName = resultSetMetaData.getColumnTypeName(i);
					String javaTypeName = null;
					array[1] = resultSetMetaData.getColumnName(i);
					javaTypeName = convert(columnTypeName);
					array[0] = javaTypeName;
					list.add(array);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (Exception e2) {
					log.error("", e2);
				}
		}
		return list;
	}

	private static String convert(String columnTypeName) {
		String javaTypeName;
		if (columnTypeName.equalsIgnoreCase("date")) {
			javaTypeName = "Date";
		} else if (columnTypeName.equalsIgnoreCase("datetime")) {// 建议数据库定义datetime
			javaTypeName = "Date";
		} else if (columnTypeName.equalsIgnoreCase("timestamp")) {
			javaTypeName = "Date";
		} else if (columnTypeName.equalsIgnoreCase("mediumtext")) {
			javaTypeName = "String";
		} else if (columnTypeName.equalsIgnoreCase("text")) {
			javaTypeName = "String";
		} else if (columnTypeName.equalsIgnoreCase("char")) {
			javaTypeName = "String";
		} else if (columnTypeName.equalsIgnoreCase("varchar")) {
			javaTypeName = "String";
		} else if (columnTypeName.equalsIgnoreCase("longtext")) {
			javaTypeName = "String";
		} else if (columnTypeName.equalsIgnoreCase("enum")) {
			javaTypeName = "String";
		} else if (columnTypeName.equalsIgnoreCase("tinyint")) {
			javaTypeName = "String";
		} else if (columnTypeName.equalsIgnoreCase("int")) {
			javaTypeName = "Integer";
		} else if (columnTypeName.equalsIgnoreCase("smallint")) {
			javaTypeName = "Integer";
		} else if (columnTypeName.equalsIgnoreCase("bigint")) {
			javaTypeName = "Integer";
		} else if (columnTypeName.equalsIgnoreCase("decimal")) {
			javaTypeName = "Double";
		} else if (columnTypeName.equalsIgnoreCase("double")) {
			javaTypeName = "Double";
		} else if (columnTypeName.equalsIgnoreCase("varbinary")) {
			javaTypeName = "byte[]";
		} else {
			javaTypeName = "String";
		}
		return javaTypeName;
	}

}
