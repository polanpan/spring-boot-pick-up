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
 * <p> 与节点网关数据同步-白名单 数据访问层 </p>
 * @author panliyong  2019-05-22 16:27
 */
@Component
@Slf4j
public class WhiteListRedisService {

    @Autowired
    private IntegerRedisBaseDao baseDao;

    //region 全局白名单

    /**
     * <p>添加全局白名单</p>
     * @param phone 手机号码
     * @author panliyong  2019/1/28 11:47
     */
    public void addGlobalWhite(String phone) {
        baseDao.addItem(String.format(RedisConstants.GLOBAL_WHITE_LIST, phone),baseDao.getMillisecond()) ;
    }

    /**
     * <p>批量添加全局白名单</p>
     * @param phoneList 手机号码
     * @author panliyong  2019/1/28 11:47
     */
    public void addGlobalWhiteList(List<String> phoneList) {
        Map<String,Integer> map = Maps.newHashMap();
        for (String phone : phoneList) {
            map.put(String.format(RedisConstants.GLOBAL_WHITE_LIST, phone), baseDao.getMillisecond());
        }
        baseDao.addItems(map);
    }

    /**
     * <p>删除全局白名单</p>
     * @param phone 手机号码
     * @author panliyong  2019/1/28 11:47
     */
    public void delGlobalWhite(String phone) {
        baseDao.delItem(String.format(RedisConstants.GLOBAL_WHITE_LIST, phone) ) ;
    }

    /**
     * <p>批量删除全局白名单</p>
     * @param phoneList 手机号码
     * @author panliyong  2019/1/28 11:47
     */
    public void delGlobalWhiteList(List<String> phoneList) {
        for (String phone : phoneList) {
            this.delGlobalWhite(phone);
        }
    }
    //endregion

    //region 租户白名单

    /**
     * <p>添加租户白名单</p>
     * @param phone    手机号码
     * @param tenantId 租户 Id
     * @author panliyong  2019/1/28 11:47
     */
    public void addTenantWhite(Integer tenantId, String phone) {
        baseDao.addItem(String.format(RedisConstants.TENANT_WHITE_LIST, tenantId, phone),baseDao.getMillisecond()) ;
    }

    /**
     * <p>批量添加租户白名单</p>
     * @param phoneList 手机号码
     * @param tenantId  租户 Id
     * @author panliyong  2019/1/28 11:47
     */
    public void addTenantWhiteList(Integer tenantId, List<String> phoneList) {

        Map<String,Integer> map = Maps.newHashMap();
        for (String phone : phoneList) {
            map.put(String.format(RedisConstants.TENANT_WHITE_LIST, tenantId,phone), baseDao.getMillisecond());
        }
        baseDao.addItems(map);
    }

    /**
     * <p>删除租户白名单</p>
     * @param phone    手机号码
     * @param tenantId 租户 Id
     * @author panliyong  2019/1/28 11:47
     */
    public void delTenantWhite(Integer tenantId, String phone) {
        baseDao.delItem(String.format(RedisConstants.TENANT_WHITE_LIST, tenantId, phone)) ;
    }

    /**
     * <p>批量删除租户白名单</p>
     * @param phoneList 手机号码
     * @param tenantId  租户 Id
     * @author panliyong  2019/1/28 11:47
     */
    public void delTenantWhiteList(Integer tenantId, List<String> phoneList) {
        for (String phone : phoneList) {
            this.delTenantWhite(tenantId, phone);
        }
    }
    //endregion
}
