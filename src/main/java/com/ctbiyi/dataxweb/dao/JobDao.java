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
public class JobDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

public int countJob(String name) throws Exception { 
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT count(1) count from job where 1=1 "); 
    sb .append(" and name = ? ");
    condition.add(name);
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));//如果存在blog等字节数组类型的，请注释此行打印
    return jdbcTemplate.queryForObject( sb.toString(), condition.toArray(),Integer.class);
}

public boolean checkJob(String id,String name) throws Exception { 
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT count(1) count  from job where 1=1 "); 
    if (!StringUtils.isEmpty(id)) {
    	sb .append(" and id != ? ");
    	condition.add(id);
    }
    sb .append(" and name= ? ");
    condition.add(name);
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));//如果存在blog等字节数组类型的，请注释此行打印
    int count = jdbcTemplate.queryForObject( sb.toString(), condition.toArray(),Integer.class);
    if(count>0) return false;
    else return true;
}

public int deleteJob(String id) throws Exception { 
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" delete  from job where 1=1 "); 
    sb .append(" and id = ? ");
    condition.add(id);
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));//如果存在blog等字节数组类型的，请注释此行打印
    return jdbcTemplate.update( sb.toString(), condition.toArray());
}

public Map<String,Object> getJob(Object id) throws Exception { 
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT id,name,inputId,inputName,inputType,outputId,outputName,outputType,transformerId,transformerName,transformerType,dataxJson,status,lastBuildId,lastBuildNumber,lastSuccessfulBuildNumber,lastUnsuccessfulBuildNumber,nextBuildNumber,date_format(lastBuildCreateTime,	'%Y-%m-%d %H:%i:%s') lastBuildCreateTime,lastBuildEstimatedDuration,lastBuildProgress,createUserId,createUserName,date_format(createTime,	'%Y-%m-%d %H:%i:%s') createTime from job where 1=1 "); 
    if (!StringUtils.isEmpty(id)) {
    	sb .append(" and id = ? ");
    	condition.add(id);
    }
    sb.append(" limit ? ,?  ");
    condition.add(0);
    condition.add(1);
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));//如果存在blog等字节数组类型的，请注释此行打印
    Map<String,Object> map = new HashMap<>();
    try{
    	map =jdbcTemplate.queryForMap( sb.toString(), condition.toArray());
    }catch (Exception e) {
    }
    return map;
}

public Map<String,Object> getJobByKeyValue(String key,Object value) throws Exception { 
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT id,name,inputId,inputName,inputType,outputId,outputName,outputType,transformerId,transformerName,transformerType,dataxJson,status,lastBuildId,lastBuildNumber,lastSuccessfulBuildNumber,lastUnsuccessfulBuildNumber,nextBuildNumber,date_format(lastBuildCreateTime,	'%Y-%m-%d %H:%i:%s') lastBuildCreateTime,lastBuildEstimatedDuration,lastBuildProgress,createUserId,createUserName,date_format(createTime,	'%Y-%m-%d %H:%i:%s') createTime from job where 1=1 "); 
   	sb .append(" and "+key+" = ? ");
   	condition.add(value);
    sb.append(" limit ? ,?  ");
    condition.add(0);
    condition.add(1);
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));//如果存在blog等字节数组类型的，请注释此行打印
    Map<String,Object> map = new HashMap<>();
    try{
    	map =jdbcTemplate.queryForMap( sb.toString(), condition.toArray());
    }catch (Exception e) {
    }
    return map;
}

public int saveJob( Map<String,Object> map)  throws Exception  {
    StringBuilder sb1 = new StringBuilder();
    StringBuilder sb2 = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb1.append("id").append(",");
    sb2.append("?,");
    condition.add(map.get("id"));

    sb1.append("name").append(",");
    sb2.append("?,");
    condition.add(map.get("name"));

    sb1.append("inputId").append(",");
    sb2.append("?,");
    condition.add(map.get("inputId"));

    sb1.append("inputName").append(",");
    sb2.append("?,");
    condition.add(map.get("inputName"));

    sb1.append("inputType").append(",");
    sb2.append("?,");
    condition.add(map.get("inputType"));

    sb1.append("outputId").append(",");
    sb2.append("?,");
    condition.add(map.get("outputId"));

    sb1.append("outputName").append(",");
    sb2.append("?,");
    condition.add(map.get("outputName"));

    sb1.append("outputType").append(",");
    sb2.append("?,");
    condition.add(map.get("outputType"));

    sb1.append("transformerId").append(",");
    sb2.append("?,");
    condition.add(map.get("transformerId"));

    sb1.append("transformerName").append(",");
    sb2.append("?,");
    condition.add(map.get("transformerName"));

    sb1.append("transformerType").append(",");
    sb2.append("?,");
    condition.add(map.get("transformerType"));

    sb1.append("dataxJson").append(",");
    sb2.append("?,");
    condition.add(map.get("dataxJson"));

    sb1.append("status").append(",");
    sb2.append("?,");
    condition.add(map.get("status"));

    sb1.append("lastBuildId").append(",");
    sb2.append("?,");
    condition.add(map.get("lastBuildId"));

    sb1.append("lastBuildNumber").append(",");
    sb2.append("?,");
    condition.add(map.get("lastBuildNumber"));

    sb1.append("lastSuccessfulBuildNumber").append(",");
    sb2.append("?,");
    condition.add(map.get("lastSuccessfulBuildNumber"));

    sb1.append("lastUnsuccessfulBuildNumber").append(",");
    sb2.append("?,");
    condition.add(map.get("lastUnsuccessfulBuildNumber"));

    sb1.append("nextBuildNumber").append(",");
    sb2.append("?,");
    condition.add(map.get("nextBuildNumber"));

    sb1.append("lastBuildCreateTime").append(",");
    sb2.append("?,");
    condition.add(map.get("lastBuildCreateTime"));

    sb1.append("lastBuildEstimatedDuration").append(",");
    sb2.append("?,");
    condition.add(map.get("lastBuildEstimatedDuration"));

    sb1.append("lastBuildProgress").append(",");
    sb2.append("?,");
    condition.add(map.get("lastBuildProgress"));

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
    String sql = "insert into job(" + sb1.toString() + ") values(" + sb2.toString() + ")";
    log.info("sql=" + sql);
    log.info("condition=" + Arrays.deepToString(condition.toArray()));//如果存在blog等字节数组类型的，请注释此行打印
    return jdbcTemplate.update( sql, condition.toArray());

}

public int updateJob( Map<String,Object> map) throws Exception  {
    StringBuilder sb = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" name = ? ,");
    condition.add(map.get("name"));
    
    sb.append(" inputId = ? ,");
    condition.add(map.get("inputId"));
    
    sb.append(" inputName = ? ,");
    condition.add(map.get("inputName"));
    
    sb.append(" inputType = ? ,");
    condition.add(map.get("inputType"));
    
    sb.append(" outputId = ? ,");
    condition.add(map.get("outputId"));
    
    sb.append(" outputName = ? ,");
    condition.add(map.get("outputName"));
    
    sb.append(" outputType = ? ,");
    condition.add(map.get("outputType"));
    
    sb.append(" transformerId = ? ,");
    condition.add(map.get("transformerId"));
    
    sb.append(" transformerName = ? ,");
    condition.add(map.get("transformerName"));
    
    sb.append(" transformerType = ? ,");
    condition.add(map.get("transformerType"));
    
    sb.append(" dataxJson = ? ,");
    condition.add(map.get("dataxJson"));
    
    sb.append(" status = ? ,");
    condition.add(map.get("status"));
    
    sb.append(" lastBuildId = ? ,");
    condition.add(map.get("lastBuildId"));
    
    sb.append(" lastBuildNumber = ? ,");
    condition.add(map.get("lastBuildNumber"));
    
    sb.append(" lastSuccessfulBuildNumber = ? ,");
    condition.add(map.get("lastSuccessfulBuildNumber"));
    
    sb.append(" lastUnsuccessfulBuildNumber = ? ,");
    condition.add(map.get("lastUnsuccessfulBuildNumber"));
    
    sb.append(" nextBuildNumber = ? ,");
    condition.add(map.get("nextBuildNumber"));
    
    sb.append(" lastBuildCreateTime = ? ,");
    condition.add(map.get("lastBuildCreateTime"));
    
    sb.append(" lastBuildEstimatedDuration = ? ,");
    condition.add(map.get("lastBuildEstimatedDuration"));
    
    sb.append(" lastBuildProgress = ? ,");
    condition.add(map.get("lastBuildProgress"));
    
    sb.append(" createUserId = ? ,");
    condition.add(map.get("createUserId"));
    
    sb.append(" createUserName = ? ,");
    condition.add(map.get("createUserName"));
    
    sb.append(" createTime = ? ,");
    condition.add(map.get("createTime"));
    
    if (sb.length() > 0)
        sb.deleteCharAt(sb.length() - 1);	
    String sql = "update job set " + sb.toString() + " where    id=?";
    condition.add(map.get("id"));
    log.info("sql=" + sql);
    log.info("condition=" + Arrays.deepToString(condition.toArray()));//如果存在blog等字节数组类型的，请注释此行打印
    return jdbcTemplate.update(  sql, condition.toArray());
}

public List<Map<String,Object>> listJob( String id,String name,String inputId,String inputName,String inputType,String outputId,String outputName,String outputType,String transformerId,String transformerName,String transformerType,String dataxJson,Integer status,String lastBuildId,String lastBuildNumber,String lastSuccessfulBuildNumber,String lastUnsuccessfulBuildNumber,String nextBuildNumber,String startLastBuildCreateTime,String endLastBuildCreateTime,Integer lastBuildEstimatedDuration,Double lastBuildProgress,String createUserId,String createUserName,String startCreateTime,String endCreateTime) throws Exception  {
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT id,name,inputId,inputName,inputType,outputId,outputName,outputType,transformerId,transformerName,transformerType,dataxJson,status,lastBuildId,lastBuildNumber,lastSuccessfulBuildNumber,lastUnsuccessfulBuildNumber,nextBuildNumber,date_format(lastBuildCreateTime,	'%Y-%m-%d %H:%i:%s') lastBuildCreateTime,lastBuildEstimatedDuration,lastBuildProgress,createUserId,createUserName,date_format(createTime,	'%Y-%m-%d %H:%i:%s') createTime from job where 1=1 "); 
    if (!StringUtils.isEmpty(id)) {
    	sb .append(" and id like ? ");
    	condition.add("%"+id+"%");
    }
    if (!StringUtils.isEmpty(name)) {
    	sb .append(" and name like ? ");
    	condition.add("%"+name+"%");
    }
    if (!StringUtils.isEmpty(inputId)) {
    	sb .append(" and inputId like ? ");
    	condition.add("%"+inputId+"%");
    }
    if (!StringUtils.isEmpty(inputName)) {
    	sb .append(" and inputName like ? ");
    	condition.add("%"+inputName+"%");
    }
    if (!StringUtils.isEmpty(inputType)) {
    	sb .append(" and inputType like ? ");
    	condition.add("%"+inputType+"%");
    }
    if (!StringUtils.isEmpty(outputId)) {
    	sb .append(" and outputId like ? ");
    	condition.add("%"+outputId+"%");
    }
    if (!StringUtils.isEmpty(outputName)) {
    	sb .append(" and outputName like ? ");
    	condition.add("%"+outputName+"%");
    }
    if (!StringUtils.isEmpty(outputType)) {
    	sb .append(" and outputType like ? ");
    	condition.add("%"+outputType+"%");
    }
    if (!StringUtils.isEmpty(transformerId)) {
    	sb .append(" and transformerId like ? ");
    	condition.add("%"+transformerId+"%");
    }
    if (!StringUtils.isEmpty(transformerName)) {
    	sb .append(" and transformerName like ? ");
    	condition.add("%"+transformerName+"%");
    }
    if (!StringUtils.isEmpty(transformerType)) {
    	sb .append(" and transformerType like ? ");
    	condition.add("%"+transformerType+"%");
    }
    if (!StringUtils.isEmpty(dataxJson)) {
    	sb .append(" and dataxJson like ? ");
    	condition.add("%"+dataxJson+"%");
    }
    if (status!=null) {
    	sb .append(" and status = ? ");
    	condition.add(status);
    }
    if (!StringUtils.isEmpty(lastBuildId)) {
    	sb .append(" and lastBuildId like ? ");
    	condition.add("%"+lastBuildId+"%");
    }
    if (!StringUtils.isEmpty(lastBuildNumber)) {
    	sb .append(" and lastBuildNumber like ? ");
    	condition.add("%"+lastBuildNumber+"%");
    }
    if (!StringUtils.isEmpty(lastSuccessfulBuildNumber)) {
    	sb .append(" and lastSuccessfulBuildNumber like ? ");
    	condition.add("%"+lastSuccessfulBuildNumber+"%");
    }
    if (!StringUtils.isEmpty(lastUnsuccessfulBuildNumber)) {
    	sb .append(" and lastUnsuccessfulBuildNumber like ? ");
    	condition.add("%"+lastUnsuccessfulBuildNumber+"%");
    }
    if (!StringUtils.isEmpty(nextBuildNumber)) {
    	sb .append(" and nextBuildNumber like ? ");
    	condition.add("%"+nextBuildNumber+"%");
    }
    if (!StringUtils.isEmpty(startLastBuildCreateTime)) {
    	sb .append(" and lastBuildCreateTime >=  ?  ");
    	condition.add(startLastBuildCreateTime);
    }
    if (!StringUtils.isEmpty(endLastBuildCreateTime)) {
    	sb .append(" and lastBuildCreateTime <=  ?  ");
    	condition.add(endLastBuildCreateTime);
    }
    if (lastBuildEstimatedDuration!=null) {
    	sb .append(" and lastBuildEstimatedDuration = ? ");
    	condition.add(lastBuildEstimatedDuration);
    }
    if (lastBuildProgress!=null) {
    	sb .append(" and lastBuildProgress = ? ");
    	condition.add(lastBuildProgress);
    }
    if (!StringUtils.isEmpty(createUserId)) {
    	sb .append(" and createUserId like ? ");
    	condition.add("%"+createUserId+"%");
    }
    if (!StringUtils.isEmpty(createUserName)) {
    	sb .append(" and createUserName like ? ");
    	condition.add("%"+createUserName+"%");
    }
    if (!StringUtils.isEmpty(startCreateTime)) {
    	sb .append(" and createTime >=  ?  ");
    	condition.add(startCreateTime);
    }
    if (!StringUtils.isEmpty(endCreateTime)) {
    	sb .append(" and createTime <=  ?  ");
    	condition.add(endCreateTime);
    }
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));//如果存在blog等字节数组类型的，请注释此行打印
    return jdbcTemplate.queryForList( sb.toString(), condition.toArray());

}

public Map<String,Object> pageJob( String id,String name,String inputId,String inputName,String inputType,String outputId,String outputName,String outputType,String transformerId,String transformerName,String transformerType,String dataxJson,Integer status,String lastBuildId,String lastBuildNumber,String lastSuccessfulBuildNumber,String lastUnsuccessfulBuildNumber,String nextBuildNumber,String startLastBuildCreateTime,String endLastBuildCreateTime,Integer lastBuildEstimatedDuration,Double lastBuildProgress,String createUserId,String createUserName,String startCreateTime,String endCreateTime,Integer pageNum,Integer pageSize) throws Exception  {
    if(pageNum==null) pageNum=1;//取名pageNum为了兼容mybatis-pageHelper中的page对象的pageNum,注意spring的PageRequest使用page表示页号,综合比较，感觉pageNum更加直观,不需要看上下文能猜出字段是页号
    if(pageSize==null)pageSize=10;//取名pageSize为了兼容mybatis-pageHelper中的page对象的pageSize,注意spring的PageRequest使用size表示页数量，综��比较，感觉pageSize会更加直观,不需要看上下文能猜出字段是分页时当前页的数量
    int from = (pageNum-1)*pageSize;
    int size = pageSize;
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT id,name,inputId,inputName,inputType,outputId,outputName,outputType,transformerId,transformerName,transformerType,dataxJson,status,lastBuildId,lastBuildNumber,lastSuccessfulBuildNumber,lastUnsuccessfulBuildNumber,nextBuildNumber,date_format(lastBuildCreateTime,	'%Y-%m-%d %H:%i:%s') lastBuildCreateTime,lastBuildEstimatedDuration,lastBuildProgress,createUserId,createUserName,date_format(createTime,	'%Y-%m-%d %H:%i:%s') createTime from job where 1=1 "); 
    if (!StringUtils.isEmpty(id)) {
    	sb .append(" and id like ? ");
    	condition.add("%"+id+"%");
    }
    if (!StringUtils.isEmpty(name)) {
    	sb .append(" and name like ? ");
    	condition.add("%"+name+"%");
    }
    if (!StringUtils.isEmpty(inputId)) {
    	sb .append(" and inputId like ? ");
    	condition.add("%"+inputId+"%");
    }
    if (!StringUtils.isEmpty(inputName)) {
    	sb .append(" and inputName like ? ");
    	condition.add("%"+inputName+"%");
    }
    if (!StringUtils.isEmpty(inputType)) {
    	sb .append(" and inputType like ? ");
    	condition.add("%"+inputType+"%");
    }
    if (!StringUtils.isEmpty(outputId)) {
    	sb .append(" and outputId like ? ");
    	condition.add("%"+outputId+"%");
    }
    if (!StringUtils.isEmpty(outputName)) {
    	sb .append(" and outputName like ? ");
    	condition.add("%"+outputName+"%");
    }
    if (!StringUtils.isEmpty(outputType)) {
    	sb .append(" and outputType like ? ");
    	condition.add("%"+outputType+"%");
    }
    if (!StringUtils.isEmpty(transformerId)) {
    	sb .append(" and transformerId like ? ");
    	condition.add("%"+transformerId+"%");
    }
    if (!StringUtils.isEmpty(transformerName)) {
    	sb .append(" and transformerName like ? ");
    	condition.add("%"+transformerName+"%");
    }
    if (!StringUtils.isEmpty(transformerType)) {
    	sb .append(" and transformerType like ? ");
    	condition.add("%"+transformerType+"%");
    }
    if (!StringUtils.isEmpty(dataxJson)) {
    	sb .append(" and dataxJson like ? ");
    	condition.add("%"+dataxJson+"%");
    }
    if (status!=null) {
    	sb .append(" and status = ? ");
    	condition.add(status);
    }
    if (!StringUtils.isEmpty(lastBuildId)) {
    	sb .append(" and lastBuildId like ? ");
    	condition.add("%"+lastBuildId+"%");
    }
    if (!StringUtils.isEmpty(lastBuildNumber)) {
    	sb .append(" and lastBuildNumber like ? ");
    	condition.add("%"+lastBuildNumber+"%");
    }
    if (!StringUtils.isEmpty(lastSuccessfulBuildNumber)) {
    	sb .append(" and lastSuccessfulBuildNumber like ? ");
    	condition.add("%"+lastSuccessfulBuildNumber+"%");
    }
    if (!StringUtils.isEmpty(lastUnsuccessfulBuildNumber)) {
    	sb .append(" and lastUnsuccessfulBuildNumber like ? ");
    	condition.add("%"+lastUnsuccessfulBuildNumber+"%");
    }
    if (!StringUtils.isEmpty(nextBuildNumber)) {
    	sb .append(" and nextBuildNumber like ? ");
    	condition.add("%"+nextBuildNumber+"%");
    }
    if (!StringUtils.isEmpty(startLastBuildCreateTime)) {
    	sb .append(" and lastBuildCreateTime >=  ?  ");
    	condition.add(startLastBuildCreateTime);
    }
    if (!StringUtils.isEmpty(endLastBuildCreateTime)) {
    	sb .append(" and lastBuildCreateTime <=  ?  ");
    	condition.add(endLastBuildCreateTime);
    }
    if (lastBuildEstimatedDuration!=null) {
    	sb .append(" and lastBuildEstimatedDuration = ? ");
    	condition.add(lastBuildEstimatedDuration);
    }
    if (lastBuildProgress!=null) {
    	sb .append(" and lastBuildProgress = ? ");
    	condition.add(lastBuildProgress);
    }
    if (!StringUtils.isEmpty(createUserId)) {
    	sb .append(" and createUserId like ? ");
    	condition.add("%"+createUserId+"%");
    }
    if (!StringUtils.isEmpty(createUserName)) {
    	sb .append(" and createUserName like ? ");
    	condition.add("%"+createUserName+"%");
    }
    if (!StringUtils.isEmpty(startCreateTime)) {
    	sb .append(" and createTime >=  ?  ");
    	condition.add(startCreateTime);
    }
    if (!StringUtils.isEmpty(endCreateTime)) {
    	sb .append(" and createTime <=  ?  ");
    	condition.add(endCreateTime);
    }
    String countSql = "select count(1) count from ( " + sb.toString()+") t";
    int count = jdbcTemplate.queryForObject(countSql, condition.toArray(),Integer.class);
    sb.append(" order by lastBuildCreateTime desc ");
    sb.append(" limit ? ,?  ");
    condition.add(from);
    condition.add(size);
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));//如果存在blog等字节数组类型的，请注释此行打印
    List<Map<String, Object>> pageList = jdbcTemplate.queryForList( sb.toString(), condition.toArray());
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("total", count);//取名total为了兼容mybatis-pageHelper中的page对象的total,spring框架的PageImpl也使用total
    map.put("pageList", pageList);//不同的框架取名不一样，可以把pageList改成list,array,rows,data,content,result等,spring框架使用的是content,mybatis因为page对象是继承ArrayList，字段命名乱七八糟，有时pageList，有时result，综上感觉pageList会更加直观和简洁,不需要看上下文能猜出字段是列表
    return map;

}

}
