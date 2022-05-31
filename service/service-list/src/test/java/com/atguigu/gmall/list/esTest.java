package com.atguigu.gmall.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/29 17:24
 */
@SpringBootTest
public class esTest {
    @Autowired
    ElasticsearchRestTemplate restTemplate;

    //保存索引
    @Test
    public void esTest() {
        IndexQuery indexQuery = new IndexQuery();
        Hello hello = new Hello(2L, "hello", "小贾");
        indexQuery.setId(hello.getId().toString());//指定保存时的id
        indexQuery.setObject(hello);//指定要保存哪个对象
        String s = restTemplate.index(indexQuery, IndexCoordinates.of("小徐"));
        System.out.println("s = " + s);
    }

    //ES 中 index代表数据库名
    //type 代表表名
    //field 代表字段名


    //删除字段
    @Test
    void testDelete() {
        String delete = restTemplate.delete("2", IndexCoordinates.of("小徐"));
        System.out.println("delete = " + delete);
    }

    //增量修改字段
    @Test
    void testUpdate() {
       /* Hello hello = new Hello(1L, "world", "小马3");
        String toStr = JSONs.toStr(hello);
        Document document = Document.parse(toStr);*/
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("name", "小马4");
        Document document = Document.from(map);
        UpdateQuery build = UpdateQuery.builder("1")
                .withDocAsUpsert(false)
                .withDocument(document).build();

        UpdateResponse update = restTemplate.update(build, IndexCoordinates.of("小徐"));
        System.out.println("update = " + update);
        System.out.println("====================");
        UpdateResponse.Result result = update.getResult();
        String s = result.toString();
        System.out.println("s = " + s);

    }

    //查询字段
    @Test
    void testSelect() {
        //单个查询
        Hello hello = restTemplate.get("id", Hello.class, IndexCoordinates.of("小徐"));
        System.out.println("hello = " + hello);
        //复杂查询
//        Query var1, Class<T> var2, IndexCoordinates var3
        //查询索引中所有的数据
        SearchHits<Hello> searchHits = restTemplate.search(Query.findAll(), Hello.class, IndexCoordinates.of("小徐"));
        //获得一个List 里面是hits的总数据
        List<SearchHit<Hello>> hitList = searchHits.getSearchHits();
        for (SearchHit<Hello> helloSearchHit : hitList) {
            Hello content = helloSearchHit.getContent();
            System.out.println("content = " + content);
        }
        System.out.println("hitList = " + hitList);
    }

    @org.junit.Test
    public void IntegerTest() {
        Integer a = 127;
        Integer b = 127;
        Integer c = 128;
        Integer d = 128;
        System.out.println(a == b); //true
        System.out.println(c == d); //false

    }

}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Hello {
    private Long id;
    private String msg;
    private String name;
}
