package com.unioncloud.redission.service;

import com.google.common.collect.Maps;
import com.unioncloud.redission.dao.base.IntegerRedisBaseDao;
import com.unioncloud.redission.constant.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <p> 与节点网关数据同步-黑名单   </p>
 * @author panliyong  2019-05-22 16:27
 */
@Component
@Slf4j
public class BlackListRedisService {

    @Autowired
    private IntegerRedisBaseDao baseDao;

    //region 全局黑名单
  
    /**
     * <p>添加全局黑名单</p>
     * @param phone 手机号码
     * @author panliyong  2019/1/28 11:47
     */
    public void addGlobalBlack(String phone) {
        baseDao.addItem(String.format(RedisConstants.GLOBAL_BLACK_LIST, phone), baseDao.getMillisecond());
    }

    /**
     * <p>批量添加全局黑名单</p>
     * @param phoneList 手机号码
     * @author panliyong  2019/1/28 11:47
     */
    public void addGlobalBlackList(List<String> phoneList) {
        Map<String,Integer> map = Maps.newHashMap();
        for (String phone : phoneList) {
            map.put(String.format(RedisConstants.GLOBAL_BLACK_LIST, phone), baseDao.getMillisecond());
        }
        baseDao.addItems(map);
    }

    /**
     * <p>删除全局黑名单</p>
     * @param phone 手机号码
     * @author panliyong  2019/1/28 11:47
     */
    public void delGlobalBlack(String phone) {
        baseDao.delItem(String.format(RedisConstants.GLOBAL_BLACK_LIST, phone));
    }

    /**
     * <p>批量删除全局黑名单</p>
     * @param phoneList 手机号码
     * @author panliyong  2019/1/28 11:47
     */
    public void delGlobalBlackList(List<String> phoneList) {
        for (String phone : phoneList) {
            this.delGlobalBlack(phone);
        }
    }
    //endregion

    //region 租户黑名单

    /**
     * <p>添加租户黑名单</p>
     * @param phone    手机号码
     * @param tenantId 租户 Id
     * @author panliyong  2019/1/28 11:47
     */
    public void addTenantBlack(Integer tenantId, String phone) {
        baseDao.addItem(String.format(RedisConstants.TENANT_BLACK_LIST, tenantId, phone), baseDao.getMillisecond());
    }

    /**
     * <p>批量添加租户黑名单</p>
     * @param phoneList 手机号码
     * @param tenantId  租户 Id
     * @author panliyong  2019/1/28 11:47
     */
    public void addTenantBlackList(Integer tenantId, List<String> phoneList) {
        Map<String,Integer> map = Maps.newHashMap();
        for (String phone : phoneList) {
            map.put(String.format(RedisConstants.TENANT_BLACK_LIST, tenantId, phone), baseDao.getMillisecond());
        }
        baseDao.addItems(map);
    }

    /**
     * <p>删除租户黑名单</p>
     * @param phone    手机号码
     * @param tenantId 租户 Id
     * @author panliyong  2019/1/28 11:47
     */
    public void delTenantBlack(Integer tenantId, String phone) {
        baseDao.delItem(String.format(RedisConstants.TENANT_BLACK_LIST, tenantId, phone));
    }

    /**
     * <p>批量删除租户黑名单</p>
     * @param phoneList 手机号码
     * @param tenantId  租户 Id
     * @author panliyong  2019/1/28 11:47
     */
    public void delTenantBlackList(Integer tenantId, List<String> phoneList) {
        for (String phone : phoneList) {
            this.delTenantBlack(tenantId, phone);
        }
    }
    //endregion
}
