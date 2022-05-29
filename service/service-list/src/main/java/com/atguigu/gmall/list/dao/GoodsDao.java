package com.atguigu.gmall.list.dao;

import com.atguigu.gmall.model.list.Goods;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/29 20:48
 */
@Repository
public interface GoodsDao extends CrudRepository<Goods,Long> {

}
