package com.unioncloud.redission.dao.base;

import com.unioncloud.kernel.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RBuckets;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

/**
 * <p> 与节点网关数据同步-Integer simple redis 项 通用数据访问层 </p>
 * @author panliyong  2019-05-22 16:27
 */
@Component
@Slf4j
public class IntegerRedisBaseDao {

    @Autowired
    private RedissonClient client;

    /**
     * <p>黑白名单存值工具方法</p>
     * @return 当前时间的毫秒值
     * @author panliyong  2019-05-23 10:40
     */
    public Integer getMillisecond() {
        return Integer.parseInt(DateUtils.localDateTimeFormat(LocalDateTime.now(), "yyMMddHHmm"));
    }

    /**
     * <p>添加redis 项 </p>
     * @param key   redis key
     * @param value redis value
     * @author panliyong  2019/1/28 11:47
     */
    public void addItem(String key, Integer value) {
        RBucket<Integer> bucket = client.getBucket(key);
        bucket.set(value);
    }

    /**
     * <p>批量添加redis 项 </p>
     * @param map redis key-value 对
     * @author panliyong  2019/1/28 11:47
     */
    public void addItems(Map<String, Integer> map) {
        RBuckets buckets = client.getBuckets();
        buckets.set(map);
    }

    /**
     * <p>删除redis 项 </p>
     * @param key redis key
     * @author panliyong  2019/1/28 11:47
     */
    public void delItem(String key) {
        RBucket<Integer> bucket = client.getBucket(key);
        bucket.delete();
    }


    /**
     * <p>根据模糊匹配删除key</p>
     * @param format 格式
     * @author panliyong  2019-07-02 14:38
     */
    public void delItemByPattern(String format) {
        RKeys keys = client.getKeys();
        Collection<String> keysByPattern = keys.findKeysByPattern(format);
        keys.delete(String.join(",", keysByPattern));
    }
}
