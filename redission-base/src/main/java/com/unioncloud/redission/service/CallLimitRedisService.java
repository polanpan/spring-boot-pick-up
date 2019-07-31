package com.unioncloud.redission.service;

import com.google.common.collect.Maps;
import com.unioncloud.redission.dao.base.StringRedisBaseDao;
import com.unioncloud.redission.constant.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.fn.tuple.Tuple3;

import java.util.List;
import java.util.Map;

/**
 * <p> 与节点网关数据同步-限频限次 </p>
 * @author panliyong  2019-05-22 16:27
 */
@Component
@Slf4j
public class CallLimitRedisService {

    @Autowired
    private StringRedisBaseDao baseDao;

    //region 全局限频限次

    /**
     * <p>添加全局限频限次</p>
     * @param frequencyLimit      限频
     * @param countLimit          限次
     * @param forbidDurationLimit 禁止时长限制
     * @author panliyong  2019/1/28 11:47
     */
    public void addGlobalCalledLimit(Integer frequencyLimit, Integer countLimit, Integer forbidDurationLimit) {
        baseDao.addItem(RedisConstants.GLOBAL_CALLED_LIMIT, frequencyLimit + ":" + countLimit + ":" + forbidDurationLimit);
    }


    /**
     * <p>删除全局限频限次</p>
     * @author panliyong  2019/1/28 11:47
     */
    public void delGlobalCalledLimit() {
        baseDao.delItem(RedisConstants.GLOBAL_CALLED_LIMIT);
    }

    //endregion

    //region 租户被叫限频限次

    /**
     * <p>添加租户被叫限频限次</p>
     * @param tenantId            租户 Id
     * @param frequencyLimit      限频
     * @param countLimit          限次
     * @param forbidDurationLimit 禁止时长限制
     * @author panliyong  2019-05-23 9:40
     */
    public void addTenantCalledLimit(Integer tenantId, Integer frequencyLimit, Integer countLimit, Integer forbidDurationLimit) {
        baseDao.addItem(String.format(RedisConstants.TENANT_CALLED_LIMIT, tenantId), frequencyLimit + ":" + countLimit + ":" + forbidDurationLimit);
    }

    /**
     * <p>批量添加租户被叫限频限次</p>
     * @param limits   限频限次
     *                 format： frequencyLimit , countLimit ,forbidDurationLimit
     * @param tenantId 租户 Id
     * @author panliyong  2019/1/28 11:47
     */
    public void addTenantCalledLimitList(Integer tenantId, List<Tuple3<Integer, Integer, Integer>> limits) {
        Map<String, String> map = Maps.newHashMap();
        for (Tuple3<Integer, Integer, Integer> limit : limits) {
            map.put(String.format(RedisConstants.TENANT_CALLED_LIMIT, tenantId), limit.getT1() + ":" + limit.getT2() + ":" + limit.getT3());
        }
        baseDao.addItems(map);
    }

    /**
     * <p>删除租户被叫限频限次</p>
     * @param tenantId 租户 Id
     * @author panliyong  2019/1/28 11:47
     */
    public void delTenantCalledLimit(Integer tenantId) {
        baseDao.delItem(String.format(RedisConstants.TENANT_CALLED_LIMIT, tenantId));
    }

    /**
     * <p>批量删除租户被叫限频限次</p>
     * @param tenantIds 租户 Id 集合
     * @author panliyong  2019/1/28 11:47
     */
    public void delTenantCalledLimitList(List<Integer> tenantIds) {
        for (Integer tenantId : tenantIds) {
            this.delTenantCalledLimit(tenantId);
        }
    }
    //endregion

    //region 租户主叫限频限次

    /**
     * <p>添加租户主叫限频限次</p>
     * @param tenantId            租户 Id
     * @param frequencyLimit      限频
     * @param countLimit          限次
     * @param forbidDurationLimit 禁止时长限制
     * @author panliyong  2019-05-23 9:40
     */
    public void addTenantCallerLimit(Integer tenantId, Integer frequencyLimit, Integer countLimit, Integer forbidDurationLimit) {
        baseDao.addItem(String.format(RedisConstants.TENANT_CALLER_LIMIT, tenantId), frequencyLimit + ":" + countLimit + ":" + forbidDurationLimit);
    }

    /**
     * <p>批量添加租户主叫限频限次</p>
     * @param limits   限频限次
     *                 format： frequencyLimit , countLimit ,forbidDurationLimit
     * @param tenantId 租户 Id
     * @author panliyong  2019/1/28 11:47
     */
    public void addTenantCallerLimitList(Integer tenantId, List<Tuple3<Integer, Integer, Integer>> limits) {
        Map<String, String> map = Maps.newHashMap();
        for (Tuple3<Integer, Integer, Integer> limit : limits) {
            map.put(String.format(RedisConstants.TENANT_CALLER_LIMIT, tenantId), limit.getT1() + ":" + limit.getT2() + ":" + limit.getT3());
        }
        baseDao.addItems(map);
    }

    /**
     * <p>删除租户主叫限频限次</p>
     * @param tenantId 租户 Id
     * @author panliyong  2019/1/28 11:47
     */
    public void delTenantCallerLimit(Integer tenantId) {
        baseDao.delItem(String.format(RedisConstants.TENANT_CALLER_LIMIT, tenantId));
    }

    /**
     * <p>批量删除租户主叫限频限次</p>
     * @param tenantIds 租户 Id 集合
     * @author panliyong  2019/1/28 11:47
     */
    public void delTenantCallerLimitList(List<Integer> tenantIds) {
        for (Integer tenantId : tenantIds) {
            this.delTenantCalledLimit(tenantId);
        }
    }
    //endregion
}
