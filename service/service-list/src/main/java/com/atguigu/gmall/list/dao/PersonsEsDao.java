package com.atguigu.gmall.list.dao;

import com.atguigu.gmall.list.bean.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/29 18:16
 */
@Repository
public interface PersonsEsDao extends CrudRepository<Person,Long> {
        List<Person> findAllByAddressLike(String address);

       List<Person> findAllByIdGreaterThanEqualOrAddressLike(Long id, String address);
}
