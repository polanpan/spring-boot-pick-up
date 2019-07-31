package com.unioncloud.redission.dao.base;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p> 与节点网关数据同步-List   redis 项 通用数据访问层 </p>
 * @author panliyong  2019-05-22 16:27
 */
@Component
@Slf4j
public class ListRedisBaseDao {

    @Autowired
    private RedissonClient client;

    /**
     * <p>添加redis 项 </p>
     * @param key   redis key
     * @param value redis value
     * @author panliyong  2019/1/28 11:47
     */
    public void addItem(String key, String value) {
        RList<String> list = client.getList(key);
        list.add(value);
    }

    /**
     * <p>批量添加redis 项 </p>
     * @param list redis list value
     * @author panliyong  2019/1/28 11:47
     */
    public void addItems(String key, List<String> list) {
        RList<String> lists = client.getList(key);
        lists.addAll(list);
    }

    /**
     * <p>删除redis 项 </p>
     * @param key redis key
     * @author panliyong  2019/1/28 11:47
     */
    public void delItem(String key, String value) {
        RList<String> list = client.getList(key);
        list.remove(value);
    }

    /**
     * <p>批量删除 redis 项 </p>
     * @param key  redis key
     * @param list redis list value
     * @author panliyong  2019/1/28 11:47
     */
    public void delItems(String key, List<String> list) {
        RList<String> lists = client.getList(key);
        lists.removeAll(list);
    }

    /**
     * <p>删除某一个list</p>
     * @param key list redis key
     * @author panliyong  2019-05-23 11:22
     */
    public void delList(String key) {
        RList<String> list  = client.getList(key);
        list.delete();
    }
}
