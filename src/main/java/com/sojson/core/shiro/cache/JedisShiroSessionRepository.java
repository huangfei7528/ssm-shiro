package com.sojson.core.shiro.cache;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.shiro.session.Session;

import com.sojson.common.utils.LoggerUtils;
import com.sojson.common.utils.SpringRedisUtils;
import com.sojson.core.shiro.session.CustomSessionManager;
import com.sojson.core.shiro.session.SessionStatus;
import com.sojson.core.shiro.session.ShiroSessionRepository;
/**
 * Session 管理
 * @author sojson.com
 *
 */
public class JedisShiroSessionRepository implements ShiroSessionRepository {
    public static final String REDIS_SHIRO_SESSION = "shiro-session-cache";
    //这里有个小BUG，因为Redis使用序列化后，Key反序列化回来发现前面有一段乱码，解决的办法是存储缓存不序列化
    public static final String REDIS_SHIRO_ALL = "*shiro-session-cache:*";
    private static final int SESSION_VAL_TIME_SPAN = 18000;
    @Override
    public void saveSession(Session session) {
        if (session == null || session.getId() == null)
            throw new NullPointerException("session is empty");
        try {
        	String key = buildRedisSessionKey(session.getId());
            //不存在才添加。
            if(null == session.getAttribute(CustomSessionManager.SESSION_STATUS)){
            	//Session 踢出自存存储。
            	SessionStatus sessionStatus = new SessionStatus();
            	session.setAttribute(CustomSessionManager.SESSION_STATUS, sessionStatus);
            }
            //设置session过期时间与缓存过期时间同步
            long sessionTimeOut = session.getTimeout() / 1000;
            Long expireTime = sessionTimeOut + SESSION_VAL_TIME_SPAN + (5 * 60);
            SpringRedisUtils.setHashKey(REDIS_SHIRO_SESSION, session.getId().toString(), session,expireTime);
        } catch (Exception e) {
        	LoggerUtils.fmtError(getClass(), e, "save session error，id:[%s]",session.getId());
        }
    }

    @Override
    public void deleteSession(Serializable id) {
        if (id == null) {
            throw new NullPointerException("session id is empty");
        }
        try {
            SpringRedisUtils.deleteHashKey(REDIS_SHIRO_SESSION, id.toString());
        } catch (Exception e) {
        	LoggerUtils.fmtError(getClass(), e, "删除session出现异常，id:[%s]",id);
        }
    }

   
	@Override
    public Session getSession(Serializable id) {
        if (id == null)
        	 throw new NullPointerException("session id is empty");
        Session session = null;
        try {
        	session = (Session) SpringRedisUtils.getHashKey(REDIS_SHIRO_SESSION, id.toString());
        } catch (Exception e) {
        	LoggerUtils.fmtError(getClass(), e, "获取session异常，id:[%s]",id);
        }
        return session;
    }

    @Override
    public Collection<Session> getAllSessions() {
    	Collection<Session> sessions = null;
		try {
			Map<String, Object> map = SpringRedisUtils.getKeyForMap(REDIS_SHIRO_SESSION);
			if(!map.isEmpty()){
				sessions = new HashSet<Session>();
				for(Entry<String, Object> entry : map.entrySet()){
					if(entry.getValue() instanceof Session){
						Session sess = (Session) entry.getValue();
						sessions.add(sess);
					}
				}
			}
		} catch (Exception e) {
			LoggerUtils.fmtError(getClass(), e, "获取全部session异常");
		}
       
        return sessions;
    }

    private String buildRedisSessionKey(Serializable sessionId) {
        return buildReaidSessionKey() + sessionId;
    }
    private String buildReaidSessionKey(){
    	 return REDIS_SHIRO_SESSION + ":";
    }
}
