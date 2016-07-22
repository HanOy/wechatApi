package club.hoy.base.repository;

import java.sql.SQLException;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.jdbc.SqlRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * 数据库操作的工具类
 * @author floating
 *
 */

@Repository
public class Repositories {

	private static final Logger log = LoggerFactory.getLogger(Repositories.class);
	
	private static final String EXISTS_QUERY_STRING = "SELECT COUNT(*) _count FROM %s _e WHERE _e.id = ? ";
	
	
	private static SqlSessionFactory sessionFactory;
	
	@Resource
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		sessionFactory = sqlSessionFactory;
	}
	
	/**
	 * 查询id是否存在
	 * @param table
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static boolean exist(String table, String id) {
			SqlSession session = SqlSessionUtils.getSqlSession(sessionFactory);
			try {
				SqlRunner runner = new SqlRunner(session.getConnection());
				Map<String,Object> count = runner.selectOne(String.format(EXISTS_QUERY_STRING, table), id);
				if(count.size() > 0 ) return true;
				return false;
			}catch(Exception e) {
				log.error("调用Repositories.exist方法时出现异常",e);
				throw new RuntimeException(e);
			}
			finally {
				SqlSessionUtils.closeSqlSession(session, sessionFactory);
			}
	}
}
