package com.unioncloud.redission.constant;

/**
 * <p> redis 对应的key 字符串常量</p>
 * @author panliyong  2019-05-22 15:13
 */
public class RedisConstants {
    //region 黑名单
    /**
     * 全局黑名单 key
     * format: 000:006:black_list:【11位手机号码】
     * eg: 000:006:black_list:13566667777
     */
    public static final String GLOBAL_BLACK_LIST = "000:006:black_list:%s";

    /**
     * 租户黑名单 key
     * format: 000:006:black_list:【商户Id】:【11位手机号码】
     * eg: 000:006:black_list:23:13566667777
     */
    public static final String TENANT_BLACK_LIST = "000:006:black_list:%s:%s";
    //endregion

    //region 白名单
    /**
     * 全局白名单 key
     * format: 000:006:white_list:【11位手机号码】
     * eg: 000:006:white_list:13566667777
     */
    public static final String GLOBAL_WHITE_LIST = "000:006:white_list:%s";

    /**
     * 租户白名单 key
     * format: 000:006:white_list:【商户Id】:【11位手机号码】
     * eg: 000:006:black_list:213:13566667777
     */
    public static final String TENANT_WHITE_LIST = "000:006:white_list:%s:%s";
    //endregion

    //region H码
    /**
     * 全局号码前缀 【 H码 】key
     * format: 000:006:prefix_number:【7位手机号码】
     * eg: 000:006:prefix_number:1356666
     */
    public static final String GLOBAL_PREFIX_NUMBER = "000:006:prefix_number:%s";

    /**
     * 租户号码前缀 【 H码 】key
     * format: 000:006:prefix_number:【商户Id】:【7位手机号码】
     * eg: 000:006:prefix_number:213:1356666
     */
    public static final String TENANT_PREFIX_NUMBER = "000:006:prefix_number:%s:%s";
    //endregion

    //region  限频限次
    /**
     * 全局被叫限频限次 key
     * format: 000:007:called:limit_count
     * eg: 000:007:called:limit_count
     */
    public static final String GLOBAL_CALLED_LIMIT = "000:007:called:limit_count";

    /**
     * 租户被叫限频限次 key
     * format: 000:007:called:【商户ID】:limit_count
     * eg: 000:007:called:164:limit_count
     */
    public static final String TENANT_CALLED_LIMIT = "000:007:called:%s:limit_count";

    /**
     * 租户主叫限频限次 key
     * format: 000:007:calling:【商户ID】:limit_count
     * eg: 000:007:calling:164:limit_count
     */
    public static final String TENANT_CALLER_LIMIT = "000:007:calling:%s:limit_count";
    //endregion

    //region 租户相关对应关系
    /**
     * IP和租户ID对应关系 key
     * format: 000:004:customeripid: 【商户IP 地址】
     * eg: 000:004:customeripid:101.132.116.32
     */
    public static final String TENANT_IP_MAP = "000:004:customeripid:%s";

    /**
     * 租户 ID 和 网关 Ip,池子 ID 对应关系 key
     * format: 000:004:customer_gateway_numberpool:【商户ID】:【网关IP】
     * eg: 000:004:customer_gateway_numberpool:32
     */
    public static final String TENANT_GATEWAY_POOL_MAP = "000:004:customer_gateway_numberpool:%s:%s";
    //endregion

    //region 号码池
    /**
     * 池子 ID 和号码对应关系 key
     * format: number_pool: 【池子Id】
     * eg: number_pool:3:2
     */
    public static final String NUMBER_POOL = "number_pool:%s";
    //endregion

    //region 租户资费配置
    /**
     * 租户资费配置 key
     * format: 000:004:billing: 【节点ID】：【商户Id】
     * eg: 000:004:billing:3:2
     */
    public static final String TENANT_CHARGE = "000:004:billing:%s:%s";
    //endregion

    //region 租户节点使用开关
    /**
     * 租户资费配置 key
     * format: 000:004:caller_control:【商户Id】: 【节点ID】
     * eg: 000:004:caller_control: ON
     */
    public static final String TENANT_NODE_SERVICE_SWITCH = "000:004:caller_control:%s:%s";
    //endregion

    //region 租户网关IpId对应关系
    /**
     * 租户资费配置 key
     * format: 000:001:gwipid:【网关IP】
     * eg: 000:001:gwipid: 122
     */
    public static final String GATEWAY_IP_ID_MAP = "000:001:gwipid:%s";
    //endregion
}
