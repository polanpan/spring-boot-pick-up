package com.unioncloud.redission.service;

import com.unioncloud.redission.dao.base.ListRedisBaseDao;
import com.unioncloud.redission.constant.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p> 与节点网关数据同步- 号码池子     </p>
 * @author panliyong  2019-05-22 16:27
 */
@Component
@Slf4j
public class CodeNumberPoolRedisService {

    @Autowired
    private ListRedisBaseDao baseDao;

    //region 号码池子

    /**
     * <p>添加号码池子</p>
     * @param poolId      号码池Id
     * @param codeNumbers 码号列表
     * @author panliyong  2019/1/28 11:47
     */
    public void addCodeNumberPool(Integer poolId, List<String> codeNumbers) {
        baseDao.addItems(String.format(RedisConstants.NUMBER_POOL, poolId), codeNumbers);
    }

    /**
     * <p>删除号码池子里某个码号</p>
     * @param poolId     号码池Id
     * @param codeNumber 码号
     * @author panliyong  2019/1/28 11:47
     */
    public void delCodeNumberFromPool(Integer poolId, String codeNumber) {
        baseDao.delItem(String.format(RedisConstants.NUMBER_POOL, poolId), codeNumber);
    }


    /**
     * <p>删除号码池子里一批码号</p>
     * @param poolId      号码池Id
     * @param codeNumbers 码号
     * @author panliyong  2019/1/28 11:47
     */
    public void delCodeNumberFromPool(Integer poolId, List<String> codeNumbers) {
        baseDao.delItems(String.format(RedisConstants.NUMBER_POOL, poolId), codeNumbers);
    }

    /**
     * <p> 删除某个租户号码池子 list</p>
     * @param poolId 号码池Id
     * @author panliyong  2019/1/28 11:47
     */
    public void delCodeNumberPool(Integer poolId) {
        baseDao.delList(String.format(RedisConstants.NUMBER_POOL, poolId));
    }
    //endregion
}
