package com.ctbiyi.dataxweb.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.github.qq275860560.common.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@RestController
@Slf4j
public class QaDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Value("${esUrl}")
	private String esUrl;
	
	@Value("${esUsername}")
	private String esUsername;
	
	@Value("${esPassword}")
	private String esPassword;


public int countQa(String q) throws Exception { 
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT count(1) count from qa where 1=1 "); 
    sb .append(" and q = ? ");
    condition.add(q);
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));//如果存在blog等字节数组类型的，请注释此行打印
    return jdbcTemplate.queryForObject( sb.toString(), condition.toArray(),Integer.class);
}

public boolean checkQa(String id,String q) throws Exception { 
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT count(1) count  from qa where 1=1 "); 
    if (!StringUtils.isEmpty(id)) {
    	sb .append(" and id != ? ");
    	condition.add(id);
    }
    sb .append(" and q= ? ");
    condition.add(q);
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));//如果存在blog等字节数组类型的，请注释此行打印
    int count = jdbcTemplate.queryForObject( sb.toString(), condition.toArray(),Integer.class);
    if(count>0) return false;
    else return true;
}

public int deleteQa(String id) throws Exception { 
	
 

 
	
	new RestTemplate().exchange(esUrl + "/qa/qa/" + id, HttpMethod.DELETE,

			new HttpEntity<>(null,

					new HttpHeaders() {
						{
							setContentType(MediaType.APPLICATION_JSON);
							add("Authorization", "Basic " + new String(Base64Utils.encode( (esUsername+":"+esPassword).getBytes()))); 

						}
					}),
			Map.class);
	
	
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" delete  from qa where 1=1 "); 
    sb .append(" and id = ? ");
    condition.add(id);
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));//如果存在blog等字节数组类型的，请注释此行打印
    return jdbcTemplate.update( sb.toString(), condition.toArray());
}

 
public Map<String,Object> getQa(Object id) throws Exception { 
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT id,q,a,createUserId,createUserName,date_format(createTime,	'%Y-%m-%d %H:%i:%s') createTime from qa where 1=1 "); 
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

public Map<String,Object> getQaByKeyValue(String key,Object value) throws Exception { 
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT id,q,a,createUserId,createUserName,date_format(createTime,	'%Y-%m-%d %H:%i:%s') createTime from qa where 1=1 "); 
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

public int saveQa( Map<String,Object> map)  throws Exception  {
	
	
		Map<String, Object> qaMap = new HashMap<String, Object>() {
			{
				put("id", map.get("id"));
				put("q", map.get("q"));
				put("a", map.get("a"));
			}
		}; 

		new RestTemplate().exchange(esUrl + "/qa/qa/" + map.get("id"), HttpMethod.PUT,

				new HttpEntity<>(qaMap,

						new HttpHeaders() {
							{
								setContentType(MediaType.APPLICATION_JSON);
								add("Authorization", "Basic " + new String(Base64Utils.encode( (esUsername+":"+esPassword).getBytes()))); 

							}
						}),
				Map.class);

    StringBuilder sb1 = new StringBuilder();
    StringBuilder sb2 = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb1.append("id").append(",");
    sb2.append("?,");
    condition.add(map.get("id"));

    sb1.append("q").append(",");
    sb2.append("?,");
    condition.add(map.get("q"));

    sb1.append("a").append(",");
    sb2.append("?,");
    condition.add(map.get("a"));

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
    String sql = "insert into qa(" + sb1.toString() + ") values(" + sb2.toString() + ")";
    log.info("sql=" + sql);
    log.info("condition=" + Arrays.deepToString(condition.toArray()));//如果存在blog等字节数组类型的，请注释此行打印
    return jdbcTemplate.update( sql, condition.toArray());

}

public int updateQa( Map<String,Object> map) throws Exception  {
	
	Map<String, Object> qaMap = new HashMap<String, Object>() {
		{
			put("id", map.get("id"));
			put("q", map.get("q"));
			put("a", map.get("a"));
		}
	}; 

	new RestTemplate().exchange(esUrl + "/qa/qa/" + map.get("id"), HttpMethod.PUT,

			new HttpEntity<>(qaMap,

					new HttpHeaders() {
						{
							setContentType(MediaType.APPLICATION_JSON);
							add("Authorization", "Basic " + new String(Base64Utils.encode( (esUsername+":"+esPassword).getBytes()))); 

						}
					}),
			Map.class);
	
    StringBuilder sb = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" q = ? ,");
    condition.add(map.get("q"));
    
    sb.append(" a = ? ,");
    condition.add(map.get("a"));
    
    sb.append(" createUserId = ? ,");
    condition.add(map.get("createUserId"));
    
    sb.append(" createUserName = ? ,");
    condition.add(map.get("createUserName"));
    
    sb.append(" createTime = ? ,");
    condition.add(map.get("createTime"));
    
    if (sb.length() > 0)
        sb.deleteCharAt(sb.length() - 1);	
    String sql = "update qa set " + sb.toString() + " where    id=?";
    condition.add(map.get("id"));
    log.info("sql=" + sql);
    log.info("condition=" + Arrays.deepToString(condition.toArray()));//如果存在blog等字节数组类型的，请注释此行打印
    return jdbcTemplate.update(  sql, condition.toArray());
}

public List<Map<String,Object>> listQa( String id,String q,String a,String createUserId,String createUserName,String startCreateTime,String endCreateTime) throws Exception  {
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT id,q,a,createUserId,createUserName,date_format(createTime,	'%Y-%m-%d %H:%i:%s') createTime from qa where 1=1 "); 
    if (!StringUtils.isEmpty(id)) {
    	sb .append(" and id like ? ");
    	condition.add("%"+id+"%");
    }
    if (!StringUtils.isEmpty(q)) {
    	sb .append(" and q like ? ");
    	condition.add("%"+q+"%");
    }
    if (!StringUtils.isEmpty(a)) {
    	sb .append(" and a like ? ");
    	condition.add("%"+a+"%");
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

public Map<String,Object> pageQa( String id,String q,String a,String createUserId,String createUserName,String startCreateTime,String endCreateTime,Integer pageNum,Integer pageSize) throws Exception  {
    if(pageNum==null) pageNum=1;//取名pageNum为了兼容mybatis-pageHelper中的page对象的pageNum,注意spring的PageRequest使用page表示页号,综合比较，感觉pageNum更加直观,不需要看上下文能猜出字段是页号
    if(pageSize==null)pageSize=10;//取名pageSize为了兼容mybatis-pageHelper中的page对象的pageSize,注意spring的PageRequest使用size表示页数量，综合比较，感觉pageSize会更加直观,不需要看上下文能猜出字段是分页时当前页的数量
    int from = (pageNum-1)*pageSize;
    int size = pageSize;
    StringBuilder sb  = new StringBuilder();
    List<Object> condition = new ArrayList<Object>();
    sb.append(" SELECT id,q,a,createUserId,createUserName,date_format(createTime,	'%Y-%m-%d %H:%i:%s') createTime from qa where 1=1 "); 
    if (!StringUtils.isEmpty(id)) {
    	sb .append(" and id like ? ");
    	condition.add("%"+id+"%");
    }
    if (!StringUtils.isEmpty(q)) {
    	sb .append(" and q like ? ");
    	condition.add("%"+q+"%");
    }
    if (!StringUtils.isEmpty(a)) {
    	sb .append(" and a like ? ");
    	condition.add("%"+a+"%");
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
    sb.append(" order by createTime desc ");
    sb.append(" limit ? ,?  ");
    condition.add(from);
    condition.add(size);
    log.info("sql=" + sb.toString());
    log.info("condition=" + Arrays.deepToString(condition.toArray()));//如果存在blog等字节数组类型的，请注释此行打印
    
    ////////////////如果搜索条件不为空，使用es搜索
    if (!StringUtils.isEmpty(q)) {
	    Map<String, Object> qaMap = new HashMap<String, Object>() {
			{
				put("from", from);
				put("size", size);
				put("query", new HashMap<String, Object>() {{
					put("bool", new HashMap<String, Object>() {{
						put("must", new HashMap<String, Object>() {{
							put("bool", new HashMap<String, Object>() {{
								put("should", 
										
							 
									
								new ArrayList<Map>() {{
									add( 
											new HashMap<String, Object>() {{ 
												put( "match", new HashMap<String, Object>() {{ put("q",q );}});
											}});
											add(
											new HashMap<String, Object>() {{ 
												put( "match", new HashMap<String, Object>() {{ put("a",q );}});
											}}
											);
									 
								}}
								
										
										);
							}});
							
						}});
						
					}});
					
				}} );
			}
		}; 
		log.info(JsonUtil.toJSONString(qaMap));
	    
		ResponseEntity<Map> response = new RestTemplate().exchange(esUrl + "/qa/_search?pretty" , HttpMethod.POST,
	
				new HttpEntity<>(JsonUtil.toJSONString(qaMap),
	
						new HttpHeaders() {
							{
								setContentType(MediaType.APPLICATION_JSON);
								add("Authorization", "Basic " + new String(Base64Utils.encode( (esUsername+":"+esPassword).getBytes()))); 
	
							}
						}),
				Map.class);
		Map<String,Object> body = response.getBody();
		
		Map<String, Object> hits = ((Map<String, Object>)body.get("hits"));
		int total = (int)((Map<String, Object>)hits.get("total")).get("value");
		List<Map<String, Object>> pageList  = new ArrayList<>();	
		if(total>0) {
			List<Map<String, Object>> hitList =(ArrayList) hits.get("hits");
			for(Map<String,Object> map:hitList) {
				pageList.add((Map<String, Object>)map.get("_source"));
			}
		}
		 return new HashMap<String, Object>(){{
			 put("total", total); 
			    put("pageList", pageList);
		 }};
		 
	 }
	////////////////////////////
    
    List<Map<String, Object>> pageList = jdbcTemplate.queryForList( sb.toString(), condition.toArray());
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("total", count);//取名total为了兼容mybatis-pageHelper中的page对象的total,spring框架的PageImpl也使用total
    map.put("pageList", pageList);//不同的框架取名不一样，可以把pageList改成list,array,rows,data,content,result等,spring框架使用的是content,mybatis因为page对象是继承ArrayList，字段命名乱七八糟，有时pageList，有时result，综上感觉pageList会更加直观和简洁,不需要看上下文能猜出字段是列表
    return map;

}
}
