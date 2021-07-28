package com.mygame.service;

import com.mygame.common.error.IServerError;
import com.mygame.common.utils.CommonField;
import com.mygame.dao.UserAccountDao;
import com.mygame.db.entity.UserAccount;
import com.mygame.http.request.LoginParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class UserLoginService {
    @Autowired
    private UserAccountDao userAccountDao;
    private Logger logger = LoggerFactory.getLogger(UserLoginService.class);

    public IServerError verifyLoginParam(LoginParam loginParam) {
        return null;
    }

    public  IServerError verifySdkToken(String openId, String token) {
        return null;
    }

    public UserAccount login(LoginParam loginParam) {
        String openId = loginParam.getOpenId();
        openId = openId.intern(); // 将 openId 放入常量池, 保证由参数反序列化得到的字符串对象是同一个, 这样才能确保加锁有效
        synchronized (openId) { // 对 openId 加锁，防止用户单击注册多次
            Optional<UserAccount> op = userAccountDao.findById(openId);
            UserAccount userAccount = null;
            if (!op.isPresent()) {
                // 用户不存在，自动注册
                userAccount = this.register(loginParam);
            } else {
                userAccount = op.get();
            }
            return userAccount;
        }
    }

    public UserAccount register(LoginParam loginParam) {
        // 用 Redis 自增保证 useId 全局唯一
        long userId = userAccountDao.getNextUserId();
        UserAccount userAccount = new UserAccount();
        userAccount.setOpenId(loginParam.getOpenId());
        userAccount.setCreateTime(System.currentTimeMillis());
        userAccount.setUserId(userId);
        this.updateUserAccount(userAccount);
        logger.debug("user {} 注册成功", userAccount);
        return userAccount;
    }

    public void updateUserAccount(UserAccount userAccount) {
        this.userAccountDao.saveOrUpdate(userAccount, userAccount.getOpenId());
    }

    public Optional<UserAccount> getUserAccountByOpenId(String openId) {
        return this.userAccountDao.findById(openId);
    }

    public long getUserIdFromHeader(HttpServletRequest request) {
        String value = request.getHeader(CommonField.USER_ID);
        long userId = 0;
        if (!StringUtils.isEmpty(value)) {
            userId = Long.parseLong(value);
        }
        return userId;
    }

    public String getOpenIdFromHeader(HttpServletRequest request) {
        return request.getHeader(CommonField.OPEN_ID);
    }


}
