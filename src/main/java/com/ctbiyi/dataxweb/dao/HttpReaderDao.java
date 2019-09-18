package com.ctbiyi.dataxweb.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@RestController
@Slf4j
public class HttpReaderDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int countHttpReader(String name) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(" SELECT count(1) count from httpReader where 1=1 ");
		sb.append(" and name = ? ");
		condition.add(name);
		log.info("sql=" + sb.toString());
		log.info("condition=" + Arrays.deepToString(condition.toArray()));// 如果存在blog等字节数组类型的，请注释此行打印
		return jdbcTemplate.queryForObject(sb.toString(), condition.toArray(), Integer.class);
	}

	public boolean checkHttpReader(String id, String name) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(" SELECT count(1) count  from httpReader where 1=1 ");
		if (!StringUtils.isEmpty(id)) {
			sb.append(" and id != ? ");
			condition.add(id);
		}
		sb.append(" and name= ? ");
		condition.add(name);
		log.info("sql=" + sb.toString());
		log.info("condition=" + Arrays.deepToString(condition.toArray()));// 如果存在blog等字节数组类型的，请注释此行打印
		int count = jdbcTemplate.queryForObject(sb.toString(), condition.toArray(), Integer.class);
		if (count > 0)
			return false;
		else
			return true;
	}

	public int deleteHttpReader(String id) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(" delete  from httpReader where 1=1 ");
		sb.append(" and id = ? ");
		condition.add(id);
		log.info("sql=" + sb.toString());
		log.info("condition=" + Arrays.deepToString(condition.toArray()));// 如果存在blog等字节数组类型的，请注释此行打印
		return jdbcTemplate.update(sb.toString(), condition.toArray());
	}

	public Map<String, Object> getHttpReader(Object id) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(
				" SELECT id,name,type,parameterPath,parameterFieldDelimiter,parameterColumn,createUserId,createUserName,date_format(createTime,	'%Y-%m-%d %H:%i:%s') createTime from httpReader where 1=1 ");
		if (!StringUtils.isEmpty(id)) {
			sb.append(" and id = ? ");
			condition.add(id);
		}
		sb.append(" limit ? ,?  ");
		condition.add(0);
		condition.add(1);
		log.info("sql=" + sb.toString());
		log.info("condition=" + Arrays.deepToString(condition.toArray()));// 如果存在blog等字节数组类型的，请注释此行打印
		Map<String, Object> map = new HashMap<>();
		try {
			map = jdbcTemplate.queryForMap(sb.toString(), condition.toArray());
		} catch (Exception e) {
		}
		return map;
	}

	public Map<String, Object> getHttpReaderByKeyValue(String key, Object value) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(
				" SELECT id,name,type,parameterPath,parameterFieldDelimiter,parameterColumn,createUserId,createUserName,date_format(createTime,	'%Y-%m-%d %H:%i:%s') createTime from httpReader where 1=1 ");
		sb.append(" and " + key + " = ? ");
		condition.add(value);
		sb.append(" limit ? ,?  ");
		condition.add(0);
		condition.add(1);
		log.info("sql=" + sb.toString());
		log.info("condition=" + Arrays.deepToString(condition.toArray()));// 如果存在blog等字节数组类型的，请注释此行打印
		Map<String, Object> map = new HashMap<>();
		try {
			map = jdbcTemplate.queryForMap(sb.toString(), condition.toArray());
		} catch (Exception e) {
		}
		return map;
	}

	public int saveHttpReader(Map<String, Object> map) throws Exception {
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb1.append("id").append(",");
		sb2.append("?,");
		condition.add(map.get("id"));

		sb1.append("name").append(",");
		sb2.append("?,");
		condition.add(map.get("name"));

		sb1.append("type").append(",");
		sb2.append("?,");
		condition.add(map.get("type"));

		sb1.append("parameterPath").append(",");
		sb2.append("?,");
		condition.add(map.get("parameterPath"));

		sb1.append("parameterFieldDelimiter").append(",");
		sb2.append("?,");
		condition.add(map.get("parameterFieldDelimiter"));

		sb1.append("parameterColumn").append(",");
		sb2.append("?,");
		condition.add(map.get("parameterColumn"));

		sb1.append("createUserId").append(",");
		sb2.append("?,");
		condition.add(map.get("createUserId"));

		sb1.append("createUserName").append(",");
		sb2.append("?,");
		condition.add(map.get("createUserName"));

		sb1.append("createTime").append(",");
		sb2.append("?,");
		condition.add(map.get("createTime"));

		if (sb1.length() > 0)
			sb1.deleteCharAt(sb1.length() - 1);
		if (sb2.length() > 0)
			sb2.deleteCharAt(sb2.length() - 1);
		String sql = "insert into httpReader(" + sb1.toString() + ") values(" + sb2.toString() + ")";
		log.info("sql=" + sql);
		log.info("condition=" + Arrays.deepToString(condition.toArray()));// 如果存在blog等字节数组类型的，请注释此行打印
		return jdbcTemplate.update(sql, condition.toArray());

	}

	public int updateHttpReader(Map<String, Object> map) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(" name = ? ,");
		condition.add(map.get("name"));

		sb.append(" type = ? ,");
		condition.add(map.get("type"));

		sb.append(" parameterPath = ? ,");
		condition.add(map.get("parameterPath"));

		sb.append(" parameterFieldDelimiter = ? ,");
		condition.add(map.get("parameterFieldDelimiter"));

		sb.append(" parameterColumn = ? ,");
		condition.add(map.get("parameterColumn"));

		sb.append(" createUserId = ? ,");
		condition.add(map.get("createUserId"));

		sb.append(" createUserName = ? ,");
		condition.add(map.get("createUserName"));

		sb.append(" createTime = ? ,");
		condition.add(map.get("createTime"));

		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		String sql = "update httpReader set " + sb.toString() + " where    id=?";
		condition.add(map.get("id"));
		log.info("sql=" + sql);
		log.info("condition=" + Arrays.deepToString(condition.toArray()));// 如果存在blog等字节数组类型的，请注释此行打印
		return jdbcTemplate.update(sql, condition.toArray());
	}

	public List<Map<String, Object>> listHttpReader(String id, String name, String type, String parameterPath,
			String parameterFieldDelimiter, String parameterColumn, String createUserId, String createUserName,
			String startCreateTime, String endCreateTime) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(
				" SELECT id,name,type,parameterPath,parameterFieldDelimiter,parameterColumn,createUserId,createUserName,date_format(createTime,	'%Y-%m-%d %H:%i:%s') createTime from httpReader where 1=1 ");
		if (!StringUtils.isEmpty(id)) {
			sb.append(" and id like ? ");
			condition.add("%" + id + "%");
		}
		if (!StringUtils.isEmpty(name)) {
			sb.append(" and name like ? ");
			condition.add("%" + name + "%");
		}
		if (!StringUtils.isEmpty(type)) {
			sb.append(" and type like ? ");
			condition.add("%" + type + "%");
		}
		if (!StringUtils.isEmpty(parameterPath)) {
			sb.append(" and parameterPath like ? ");
			condition.add("%" + parameterPath + "%");
		}
		if (!StringUtils.isEmpty(parameterFieldDelimiter)) {
			sb.append(" and parameterFieldDelimiter like ? ");
			condition.add("%" + parameterFieldDelimiter + "%");
		}
		if (!StringUtils.isEmpty(parameterColumn)) {
			sb.append(" and parameterColumn like ? ");
			condition.add("%" + parameterColumn + "%");
		}
		if (!StringUtils.isEmpty(createUserId)) {
			sb.append(" and createUserId like ? ");
			condition.add("%" + createUserId + "%");
		}
		if (!StringUtils.isEmpty(createUserName)) {
			sb.append(" and createUserName like ? ");
			condition.add("%" + createUserName + "%");
		}
		if (!StringUtils.isEmpty(startCreateTime)) {
			sb.append(" and createTime >=  ?  ");
			condition.add(startCreateTime);
		}
		if (!StringUtils.isEmpty(endCreateTime)) {
			sb.append(" and createTime <=  ?  ");
			condition.add(endCreateTime);
		}
		log.info("sql=" + sb.toString());
		log.info("condition=" + Arrays.deepToString(condition.toArray()));// 如果存在blog等字节数组类型的，请注释此行打印
		return jdbcTemplate.queryForList(sb.toString(), condition.toArray());

	}

	public Map<String, Object> pageHttpReader(String id, String name, String type, String parameterPath,
			String parameterFieldDelimiter, String parameterColumn, String createUserId, String createUserName,
			String startCreateTime, String endCreateTime, Integer pageNum, Integer pageSize) throws Exception {
		if (pageNum == null)
			pageNum = 1;// 取名pageNum为了兼容mybatis-pageHelper中的page对象的pageNum,注意spring的PageRequest使用page表示页号,综合比较，感觉pageNum更加直观,不需要看上下文能猜出字段是页号
		if (pageSize == null)
			pageSize = 10;// 取名pageSize为了兼容mybatis-pageHelper中的page对象的pageSize,注意spring的PageRequest使用size表示页数量，综合比较，感觉pageSize会更加直观,不需要看上下文能猜出字段是分页时当前页的数量
		int from = (pageNum - 1) * pageSize;
		int size = pageSize;
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(
				" SELECT id,name,type,parameterPath,parameterFieldDelimiter,parameterColumn,createUserId,createUserName,date_format(createTime,	'%Y-%m-%d %H:%i:%s') createTime from httpReader where 1=1 ");
		if (!StringUtils.isEmpty(id)) {
			sb.append(" and id like ? ");
			condition.add("%" + id + "%");
		}
		if (!StringUtils.isEmpty(name)) {
			sb.append(" and name like ? ");
			condition.add("%" + name + "%");
		}
		if (!StringUtils.isEmpty(type)) {
			sb.append(" and type like ? ");
			condition.add("%" + type + "%");
		}
		if (!StringUtils.isEmpty(parameterPath)) {
			sb.append(" and parameterPath like ? ");
			condition.add("%" + parameterPath + "%");
		}
		if (!StringUtils.isEmpty(parameterFieldDelimiter)) {
			sb.append(" and parameterFieldDelimiter like ? ");
			condition.add("%" + parameterFieldDelimiter + "%");
		}
		if (!StringUtils.isEmpty(parameterColumn)) {
			sb.append(" and parameterColumn like ? ");
			condition.add("%" + parameterColumn + "%");
		}
		if (!StringUtils.isEmpty(createUserId)) {
			sb.append(" and createUserId like ? ");
			condition.add("%" + createUserId + "%");
		}
		if (!StringUtils.isEmpty(createUserName)) {
			sb.append(" and createUserName like ? ");
			condition.add("%" + createUserName + "%");
		}
		if (!StringUtils.isEmpty(startCreateTime)) {
			sb.append(" and createTime >=  ?  ");
			condition.add(startCreateTime);
		}
		if (!StringUtils.isEmpty(endCreateTime)) {
			sb.append(" and createTime <=  ?  ");
			condition.add(endCreateTime);
		}
		String countSql = "select count(1) count from ( " + sb.toString() + ") t";
		int count = jdbcTemplate.queryForObject(countSql, condition.toArray(), Integer.class);
		sb.append(" order by createTime desc ");
		sb.append(" limit ? ,?  ");
		condition.add(from);
		condition.add(size);
		log.info("sql=" + sb.toString());
		log.info("condition=" + Arrays.deepToString(condition.toArray()));// 如果存在blog等字节数组类型的，请注释此行打印
		List<Map<String, Object>> pageList = jdbcTemplate.queryForList(sb.toString(), condition.toArray());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", count);// 取名total为了兼容mybatis-pageHelper中的page对象的total,spring框架的PageImpl也使用total
		map.put("pageList", pageList);// 不同的框架取名不一样，可以把pageList改成list,array,rows,data,content,result等,spring框架使用的是content,mybatis因为page对象是继承ArrayList，字段命名乱七八糟，有时pageList，有时result，综上感觉pageList会更加直观和简洁,不需要看上下文能猜出字段是列表
		return map;

	}
}
