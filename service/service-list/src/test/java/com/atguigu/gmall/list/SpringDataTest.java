package com.atguigu.gmall.list;

import com.atguigu.gmall.list.bean.Person;
import com.atguigu.gmall.list.dao.PersonsEsDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/29 18:26
 */
@SpringBootTest
public class SpringDataTest {
    @Autowired
    PersonsEsDao personsEsDao;

    //批量保存
    @Test
    void testCRUD(){
        List<Person> asList = Arrays.asList(new Person(1L, "张三", "zhangsan@qq.com", "北京市昌平区东大街", new Date()),
                new Person(2L, "李四", "lisi@qq.com", "北京市昌平区西大街", new Date()),
                new Person(3L, "王五", "wangwu@qq.com", "北京市昌平区南大街", new Date()),
                new Person(4L, "赵六", "zhaoliu@qq.com", "北京市昌平区北大街", new Date()),
                new Person(5L, "田七", "tianqi@qq.com", "北京市昌平区中大街", new Date()));
        personsEsDao.saveAll(asList);

    }

    //根据id查询
   @Test
   void testQuery(){
       Optional<Person> byId = personsEsDao.findById(2L);
       Person person = byId.get();
       System.out.println("person = " + person);
   }

    //查询住在北大街的所有人
    @Test
    void testQuery2(){
        List<Person> like = personsEsDao.findAllByAddressLike("东大街");
        for (Person person : like) {
            System.out.println("person = " + person);
        }

        List<Person> personList = personsEsDao.findAllByIdGreaterThanEqualOrAddressLike(3L, "南大街");
        for (Person person : personList) {
            System.out.println("person = " + person);
        }

    }
}
