package com.polan.redisop.service;

import com.polan.redisop.dao.UserRedisDao;
import com.polan.redisop.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p> user redis service</p>
 * @author youq  2019/4/9 15:42
 */
@Service
public class UserRedisService {

    @Autowired
    private UserRedisDao userRedisDao;

    /**
     * <p> 批量添加</p>
     * @param models 用户信息集合
     * @author youq  2019/4/9 15:51
     */
    public void saveBatch(List<UserModel> models) {
        userRedisDao.saveBatch(models);
    }

    /**
     * <p> 查询所有用户信息</p>
     * @return java.util.List<com.yq.redisop.model.UserModel>
     * @author youq  2019/4/9 15:52
     */
    public List<UserModel> findAll() {
        return userRedisDao.findAll();
    }

}
