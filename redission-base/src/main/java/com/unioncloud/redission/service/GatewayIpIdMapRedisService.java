package com.unioncloud.redission.service;

import com.unioncloud.redission.dao.base.IntegerRedisBaseDao;
import com.unioncloud.redission.constant.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p> 与节点网关数据同步-网关ip和id的对应关系 数据访问层 </p>
 * @author panliyong  2019-05-22 16:27
 */
@Component
@Slf4j
public class GatewayIpIdMapRedisService {

    @Autowired
    private IntegerRedisBaseDao baseDao;

    //region IP和网关ID对应关系

    /**
     * <p>添加IP和网关ID对应关系</p>
     * @param gatewayIp 网关对接IP
     * @param gatewayId 网关 Id
     * @author panliyong  2019/1/28 11:47
     */
    public void addGatewayIpIdMap(Integer gatewayId, String gatewayIp) {

        baseDao.addItem(String.format(RedisConstants.GATEWAY_IP_ID_MAP, gatewayIp), gatewayId);
    }


    /**
     * <p>删除IP和网关ID对应关系</p>
     * @param gatewayIp 网关对接IP
     * @author panliyong  2019/1/28 11:47
     */
    public void delGatewayIpIdMap(String gatewayIp) {
        baseDao.delItem(String.format(RedisConstants.GATEWAY_IP_ID_MAP, gatewayIp));
    }
 
    //endregion
}
