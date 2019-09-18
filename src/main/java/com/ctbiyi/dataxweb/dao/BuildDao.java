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
public class BuildDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int countBuild(String name) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(" SELECT count(1) count from build where 1=1 ");
		sb.append(" and name = ? ");
		condition.add(name);
		log.info("sql=" + sb.toString());
		log.info("condition=" + Arrays.deepToString(condition.toArray()));// 如果存在blog等字节数组类型的，请注释此行打印
		return jdbcTemplate.queryForObject(sb.toString(), condition.toArray(), Integer.class);
	}

	public boolean checkBuild(String id, String name) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(" SELECT count(1) count  from build where 1=1 ");
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

	public int deleteBuild(String id) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(" delete  from build where 1=1 ");
		sb.append(" and id = ? ");
		condition.add(id);
		log.info("sql=" + sb.toString());
		log.info("condition=" + Arrays.deepToString(condition.toArray()));// 如果存在blog等字节数组类型的，请注释此行打印
		return jdbcTemplate.update(sb.toString(), condition.toArray());
	}
	public int deleteBuildByJobId(String jobId) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(" delete  from build where 1=1 ");
		sb.append(" and jobId = ? ");
		condition.add(jobId);
		log.info("sql=" + sb.toString());
		log.info("condition=" + Arrays.deepToString(condition.toArray()));// 如果存在blog等字节数组类型的，请注释此行打印
		return jdbcTemplate.update(sb.toString(), condition.toArray());
	}

	public Map<String, Object> getBuild(Object id) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(
				" SELECT id,name,jobId,jobName,number,status,estimatedDuration,progress,duration,result,consoleText,createUserId,createUserName,date_format(createTime,	'%Y-%m-%d %H:%i:%s') createTime from build where 1=1 ");
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
			log.error("",e);
		}
		return map;
	}

	public Map<String, Object> getBuildByKeyValue(String key, Object value) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(
				" SELECT id,name,jobId,jobName,number,status,estimatedDuration,progress,duration,result,consoleText,createUserId,createUserName,date_format(createTime,	'%Y-%m-%d %H:%i:%s') createTime from build where 1=1 ");
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
			log.error("",e);
		}
		return map;
	}

	public Map<String, Object> getBuildByJobNameAndNumber(String name, Object number) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(
				" SELECT id,name,jobId,jobName,number,status,estimatedDuration,duration,result,consoleText,createUserId,createUserName,date_format(createTime,	'%Y-%m-%d %H:%i:%s') createTime from build where 1=1 ");
		sb.append(" and jobName = ? ");
		condition.add(name);
		sb.append(" and number = ? ");
		condition.add(number);
		sb.append(" limit ? ,?  ");
		condition.add(0);
		condition.add(1);
		log.info("sql=" + sb.toString());
		log.info("condition=" + Arrays.deepToString(condition.toArray()));// 如果存在blog等字节数组类型的，请注释此行打印
		Map<String, Object> map = new HashMap<>();
		try {
			map = jdbcTemplate.queryForMap(sb.toString(), condition.toArray());
		} catch (Exception e) {
			log.error("",e);
		}
		return map;
	}
	
	public int saveBuild(Map<String, Object> map) throws Exception {
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb1.append("id").append(",");
		sb2.append("?,");
		condition.add(map.get("id"));

		sb1.append("name").append(",");
		sb2.append("?,");
		condition.add(map.get("name"));

		sb1.append("jobId").append(",");
		sb2.append("?,");
		condition.add(map.get("jobId"));

		sb1.append("jobName").append(",");
		sb2.append("?,");
		condition.add(map.get("jobName"));

		sb1.append("number").append(",");
		sb2.append("?,");
		condition.add(map.get("number"));

		sb1.append("status").append(",");
		sb2.append("?,");
		condition.add(map.get("status"));

		sb1.append("estimatedDuration").append(",");
		sb2.append("?,");
		condition.add(map.get("estimatedDuration"));

		sb1.append("progress").append(",");
		sb2.append("?,");
		condition.add(map.get("progress"));

		sb1.append("duration").append(",");
		sb2.append("?,");
		condition.add(map.get("duration"));

		sb1.append("result").append(",");
		sb2.append("?,");
		condition.add(map.get("result"));

		sb1.append("consoleText").append(",");
		sb2.append("?,");
		condition.add(map.get("consoleText"));

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
		String sql = "insert into build(" + sb1.toString() + ") values(" + sb2.toString() + ")";
		log.info("sql=" + sql);
		log.info("condition=" + Arrays.deepToString(condition.toArray()));// 如果存在blog等字节数组类型的，请注释此行打印
		return jdbcTemplate.update(sql, condition.toArray());

	}

	public int updateBuild(Map<String, Object> map) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(" name = ? ,");
		condition.add(map.get("name"));

		sb.append(" jobId = ? ,");
		condition.add(map.get("jobId"));

		sb.append(" jobName = ? ,");
		condition.add(map.get("jobName"));

		sb.append(" number = ? ,");
		condition.add(map.get("number"));

		sb.append(" status = ? ,");
		condition.add(map.get("status"));

		sb.append(" estimatedDuration = ? ,");
		condition.add(map.get("estimatedDuration"));

		sb.append(" progress = ? ,");
		condition.add(map.get("progress"));

		sb.append(" duration = ? ,");
		condition.add(map.get("duration"));

		sb.append(" result = ? ,");
		condition.add(map.get("result"));

		sb.append(" consoleText = ? ,");
		condition.add(map.get("consoleText"));

		sb.append(" createUserId = ? ,");
		condition.add(map.get("createUserId"));

		sb.append(" createUserName = ? ,");
		condition.add(map.get("createUserName"));

		sb.append(" createTime = ? ,");
		condition.add(map.get("createTime"));

		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		String sql = "update build set " + sb.toString() + " where    id=?";
		condition.add(map.get("id"));
		log.info("sql=" + sql);
		log.info("condition=" + Arrays.deepToString(condition.toArray()));// 如果存在blog等字节数组类型的，请注释此行打印
		return jdbcTemplate.update(sql, condition.toArray());
	}

	public List<Map<String, Object>> listBuild(String id, String name, String jobId, String jobName, String number,
			Integer status, Integer estimatedDuration, Double progress, Integer duration, Integer result,
			String consoleText, String createUserId, String createUserName, String startCreateTime,
			String endCreateTime) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(
				" SELECT id,name,jobId,jobName,number,status,estimatedDuration,progress,duration,result,consoleText,createUserId,createUserName,date_format(createTime,	'%Y-%m-%d %H:%i:%s') createTime from build where 1=1 ");
		if (!StringUtils.isEmpty(id)) {
			sb.append(" and id like ? ");
			condition.add("%" + id + "%");
		}
		if (!StringUtils.isEmpty(name)) {
			sb.append(" and name like ? ");
			condition.add("%" + name + "%");
		}
		if (!StringUtils.isEmpty(jobId)) {
			sb.append(" and jobId like ? ");
			condition.add("%" + jobId + "%");
		}
		if (!StringUtils.isEmpty(jobName)) {
			sb.append(" and jobName like ? ");
			condition.add("%" + jobName + "%");
		}
		if (!StringUtils.isEmpty(number)) {
			sb.append(" and number like ? ");
			condition.add("%" + number + "%");
		}
		if (status != null) {
			sb.append(" and status = ? ");
			condition.add(status);
		}
		if (estimatedDuration != null) {
			sb.append(" and estimatedDuration = ? ");
			condition.add(estimatedDuration);
		}
		if (progress != null) {
			sb.append(" and progress = ? ");
			condition.add(progress);
		}
		if (duration != null) {
			sb.append(" and duration = ? ");
			condition.add(duration);
		}
		if (result != null) {
			sb.append(" and result = ? ");
			condition.add(result);
		}
		if (!StringUtils.isEmpty(consoleText)) {
			sb.append(" and consoleText like ? ");
			condition.add("%" + consoleText + "%");
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

	public Map<String, Object> pageBuild(
			String id, String name, String jobId, String jobName, String number,
			Integer status, Integer estimatedDuration, Double progress, Integer duration, Integer result,
			String consoleText, String createUserId, String createUserName, String startCreateTime,
			String endCreateTime, Integer pageNum, Integer pageSize) throws Exception {
		if (pageNum == null)
			pageNum = 1;// 取名pageNum为了兼容mybatis-pageHelper中的page对象的pageNum,注意spring的PageRequest使用page表示页号,综合比较，感觉pageNum更加直观,不需要看上下文能猜出字段是页号
		if (pageSize == null)
			pageSize = 10;// 取名pageSize为了兼容mybatis-pageHelper中的page对象的pageSize,注意spring的PageRequest使用size表示页数量，综合比较，感觉pageSize会更加直观,不需要看上下文能猜出字段是分页时当前页的数量
		int from = (pageNum - 1) * pageSize;
		int size = pageSize;
		StringBuilder sb = new StringBuilder();
		List<Object> condition = new ArrayList<Object>();
		sb.append(
				" SELECT id,name,jobId,jobName,number,status,estimatedDuration,progress,duration,result,createUserId,createUserName,date_format(createTime,	'%Y-%m-%d %H:%i:%s') createTime from build where 1=1 ");
		if (!StringUtils.isEmpty(id)) {
			sb.append(" and id like ? ");
			condition.add("%" + id + "%");
		}
		if (!StringUtils.isEmpty(name)) {
			sb.append(" and name like ? ");
			condition.add("%" + name + "%");
		}
		if (!StringUtils.isEmpty(jobId)) {
			sb.append(" and jobId like ? ");
			condition.add("%" + jobId + "%");
		}
		if (!StringUtils.isEmpty(jobName)) {
			sb.append(" and jobName like ? ");
			condition.add("%" + jobName + "%");
		}
		if (!StringUtils.isEmpty(number)) {
			sb.append(" and number like ? ");
			condition.add("%" + number + "%");
		}
		if (status != null) {
			sb.append(" and status = ? ");
			condition.add(status);
		}
		if (estimatedDuration != null) {
			sb.append(" and estimatedDuration = ? ");
			condition.add(estimatedDuration);
		}
		if (progress != null) {
			sb.append(" and progress = ? ");
			condition.add(progress);
		}
		if (duration != null) {
			sb.append(" and duration = ? ");
			condition.add(duration);
		}
		if (result != null) {
			sb.append(" and result = ? ");
			condition.add(result);
		}
		if (!StringUtils.isEmpty(consoleText)) {
			sb.append(" and consoleText like ? ");
			condition.add("%" + consoleText + "%");
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
