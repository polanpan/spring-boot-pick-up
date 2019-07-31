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
 * <p> 与节点网关数据同步-H码  数据访问层 </p>
 * @author panliyong  2019-05-22 16:27
 */
@Component
@Slf4j
public class PrefixCodeRedisService {

    @Autowired
    private IntegerRedisBaseDao baseDao;

    //region 全局H码 

    /**
     * <p>添加全局H码 </p>
     * @param phone 手机号码前缀
     * @author panliyong  2019/1/28 11:47
     */
    public void addGlobalPrefixCode(String phone) {
        baseDao.addItem(String.format(RedisConstants.GLOBAL_PREFIX_NUMBER, phone), baseDao.getMillisecond());
    }

    /**
     * <p>批量添加全局H码 </p>
     * @param phoneList 手机号码前缀
     * @author panliyong  2019/1/28 11:47
     */
    public void addGlobalPrefixCodeList(List<String> phoneList) {
        Map<String,Integer> map = Maps.newHashMap();
        for (String phone : phoneList) {
            map.put(String.format(RedisConstants.GLOBAL_PREFIX_NUMBER, phone), baseDao.getMillisecond());
        }
        baseDao.addItems(map);
    }

    /**
     * <p>删除全局H码 </p>
     * @param phone 手机号码前缀
     * @author panliyong  2019/1/28 11:47
     */
    public void delGlobalPrefixCode(String phone) {
        baseDao.delItem(String.format(RedisConstants.GLOBAL_PREFIX_NUMBER, phone));
    }

    /**
     * <p>批量删除全局H码 </p>
     * @param phoneList 手机号码前缀
     * @author panliyong  2019/1/28 11:47
     */
    public void delGlobalPrefixCodeList(List<String> phoneList) {
        for (String phone : phoneList) {
            this.delGlobalPrefixCode(phone);
        }
    }
    //endregion

    //region 租户H码 

    /**
     * <p>添加租户H码 </p>
     * @param phone    手机号码前缀
     * @param tenantId 租户 Id
     * @author panliyong  2019/1/28 11:47
     */
    public void addTenantPrefixCode(Integer tenantId, String phone) {
        baseDao.addItem(String.format(RedisConstants.TENANT_PREFIX_NUMBER, tenantId, phone), baseDao.getMillisecond());
    }

    /**
     * <p>批量添加租户H码 </p>
     * @param phoneList 手机号码前缀
     * @param tenantId  租户 Id
     * @author panliyong  2019/1/28 11:47
     */
    public void addTenantPrefixCodeList(Integer tenantId, List<String> phoneList) {
        Map<String,Integer> map = Maps.newHashMap();
        for (String phone : phoneList) {
            map.put(String.format(RedisConstants.TENANT_PREFIX_NUMBER, tenantId, phone), baseDao.getMillisecond());
        }
        baseDao.addItems(map);
    }

    /**
     * <p>删除租户H码 </p>
     * @param phone    手机号码前缀
     * @param tenantId 租户 Id
     * @author panliyong  2019/1/28 11:47
     */
    public void delTenantPrefixCode(Integer tenantId, String phone) {
        baseDao.delItem(String.format(RedisConstants.TENANT_PREFIX_NUMBER, tenantId, phone));
    }

    /**
     * <p>批量删除租户H码 </p>
     * @param phoneList 手机号码前缀
     * @param tenantId  租户 Id
     * @author panliyong  2019/1/28 11:47
     */
    public void delTenantPrefixCodeList(Integer tenantId, List<String> phoneList) {
        for (String phone : phoneList) {
            this.delTenantPrefixCode(tenantId, phone);
        }
    }
    //endregion
}
