package com.polan.feign.service.feign;

import com.polan.feign.service.feign.qo.SaveUserQO;
import com.polan.kernel.model.ResultData;
import org.springframework.stereotype.Component;

/**
 * <p> 失败回调</p>
 * @author youq  2019/4/12 10:20
 */
@Component
public class UserFeignServiceFallBack implements UserFeignService{

    @Override
    public ResultData<?> saveUser(SaveUserQO qo) {
        return ResultData.fail();
    }

}
