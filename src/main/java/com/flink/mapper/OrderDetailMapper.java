package com.flink.mapper;

import com.flink.entity.OrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author LT-0024
 * @Date 2020/9/17 16:56
 * @Version 1.0
 */
public interface OrderDetailMapper {
    /**
     * 批量保存全门店
     * @param list
     * @return
     */
    int saveListAll(@Param("orderLists")List<OrderDetail> list);

    /**
     * 批量保存三亚
     * @param list
     * @return
     */
    int saveListSanYa(@Param("orderLists")List<OrderDetail> list);
}
