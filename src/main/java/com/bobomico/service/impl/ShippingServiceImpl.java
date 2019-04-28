package com.bobomico.service.impl;

import com.bobomico.common.ServerResponse;
import com.bobomico.dao.MallShippingMapper;
import com.bobomico.dao.po.MallShipping;
import com.bobomico.service.IShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: com.bobomico.service.impl.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/4  6:23
 * @Description:
 * @version:
 */
@Service
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private MallShippingMapper shippingMapper;

    /**
     * 新增地址
     * @param userId
     * @param shipping
     * @return 地址id 需要使用mybatis的两个配置 useGeneratedKeys="true" keyProperty="id"
     */
    public ServerResponse add(Integer userId, MallShipping shipping){
        shipping.setSysUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if(rowCount > 0){
            Map result = Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功",result);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    /**
     * 删除地址
     * @param userId
     * @param shippingId
     * @return msg
     */
    public ServerResponse<String> del(Integer userId,Integer shippingId){
        int resultCount = shippingMapper.deleteByShippingIdUserId(userId,shippingId);
        if(resultCount > 0){
            return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    /**
     * 更新地址
     * @param userId
     * @param shipping
     * @return msg
     */
    public ServerResponse<String> update(Integer userId, MallShipping shipping){
        shipping.setSysUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    /**
     * 查询地址
     * @param userId 根据用户id和地址id
     * @param shippingId
     * @return 单个地址
     */
    public ServerResponse<MallShipping> select(Integer userId, Integer shippingId){
        MallShipping shipping = shippingMapper.selectByShippingIdUserId(userId,shippingId);
        if(shipping == null){
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServerResponse.createBySuccess("更新地址成功",shipping);
    }

    /**
     * 查询用户全部地址
     * @param userId 根据用户id
     * @param pageNum
     * @param pageSize
     * @return 地址集合
     */
    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<MallShipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}