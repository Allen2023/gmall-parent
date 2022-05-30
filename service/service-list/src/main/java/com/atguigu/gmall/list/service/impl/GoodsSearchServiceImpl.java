package com.atguigu.gmall.list.service.impl;

import com.atguigu.gmall.list.dao.GoodsDao;
import com.atguigu.gmall.list.service.GoodsSearchService;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.vo.GoodsSearchResultVo;
import com.atguigu.gmall.model.vo.SearchAttrListVo;
import com.atguigu.gmall.model.vo.SearchOrderMapVo;
import com.atguigu.gmall.model.vo.SearchTrademarkVo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/29 23:44
 */
@Slf4j
@Service
public class GoodsSearchServiceImpl implements GoodsSearchService {
    @Autowired
    GoodsDao goodsDao;

    @Autowired
    ElasticsearchRestTemplate restTemplate;

    /**
     * 保存商品到ES
     *
     * @param goods
     */
    @Override
    public void saveGoods(Goods goods) {
        goodsDao.save(goods);
    }

    /**
     * 下架商品从ES中删除
     *
     * @param skuId
     */
    @Override
    public void deleteGoods(Long skuId) {
        goodsDao.deleteById(skuId);
    }

    /**
     * 搜索商品数据
     *
     * @param params
     * @return
     */
    @Override
    public GoodsSearchResultVo searchGoods(SearchParam params) {
        //根据前端传过来的参数,构造复杂的检索条件
        Query query = buildQueryBySearchParam(params);
        SearchHits<Goods> searchHits = restTemplate.search(query, Goods.class, IndexCoordinates.of("goods"));
        GoodsSearchResultVo resultVo = buildResponses(searchHits, params);
        return resultVo;
    }

    /**
     * 增加商品热度
     *
     * @param skuId
     * @param score
     */
    @Override
    public void updateHotScore(Long skuId, Long score) {
        //查询skuId的ES数据
        Optional<Goods> goodsDaoById = goodsDao.findById(skuId);
        Goods goods = goodsDaoById.get();
        //修改商品热度
        goods.setHotScore(score);
        //保存商品
        goodsDao.save(goods);
        log.info("商品热度已更新为:"+skuId+":"+score);
    }


    /**
     * 创造商品检索条件
     *
     * @param params
     * @return
     */
    private Query buildQueryBySearchParam(SearchParam params) {
        NativeSearchQuery query = null;
        //构造一个bool query 条件查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        //========1.查询三级分类Id==============
        // 2.bool-must-category3Id(term)
        if (params.getCategory3Id() != null) {
            boolQuery.must(QueryBuilders.termQuery("category3Id", params.getCategory3Id()));
        }
        if (params.getCategory2Id() != null) {
            boolQuery.must(QueryBuilders.termQuery("category2Id", params.getCategory2Id()));
        }
        if (params.getCategory1Id() != null) {
            boolQuery.must(QueryBuilders.termQuery("category1Id", params.getCategory1Id()));
        }
        //========2.查询品牌列表==============
        if (!StringUtils.isEmpty(params.getTrademark())) {
            String[] split = params.getTrademark().split(":");
            boolQuery.must(QueryBuilders.termQuery("tmId", split[0]));
        }
        //========3.查询关键字==============
        if (!StringUtils.isEmpty(params.getKeyword())) {
            boolQuery.must(QueryBuilders.termQuery("title", params.getKeyword()));
        }
        //========4.平台属性值Id==============
        // 平台属性值名称 平台属性名  props = 23 : 4G : 运行内存
        if (params.getProps() != null && params.getProps().length > 0) {
            for (String prop : params.getProps()) {
                String[] split = prop.split(":");
                BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", split[0]));
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrValue", split[1]));
                //String path, QueryBuilder query, ScoreMode scoreMode
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None);
                boolQuery.must(nestedQuery);
            }
        }
        //代表完整的检索条件
        query = new NativeSearchQuery(boolQuery);
        //========5.排序规则==============
        // order = 1:asc order = 2:desc
        if (!StringUtils.isEmpty(params.getOrder())) {
            String[] split = params.getOrder().split(":");
            String sortField = "";
            switch (split[0]) {
                case "1":
                    sortField = "hotScore";
                    break;
                case "2":
                    sortField = "price";
                    break;
                default:
                    sortField = "hotScore";
            }
            Sort sort = Sort.by(Sort.Direction.fromString(split[1]), sortField);
            query.addSort(sort);
        }
        //========6.分页信息==============
        Pageable pageRequest = PageRequest.of(params.getPageNo() - 1, params.getPageSize());
        query.setPageable(pageRequest);

        //========7.构造高亮==============
        if (!StringUtils.isEmpty(params.getKeyword())) {
            //模糊检索的条件下给匹配上的值加上高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title").preTags("<span style='color:red'>").postTags("</span>");
            HighlightQuery highlightQuery = new HighlightQuery(highlightBuilder);
            query.setHighlightQuery(highlightQuery);
        }

        //==========8.构造聚合分析--分析品牌==============
        //创建聚合条件 group by  tmId,tmName,tmLogoUrl
        TermsAggregationBuilder tmIdAgg = AggregationBuilders.terms("tmIdAgg").field("tmId").size(100);
        //子聚合 -聚合品牌名
        tmIdAgg.subAggregation(AggregationBuilders.terms("tmNameAgg").field("tmName").size(1));
        //子聚合 - 聚合品牌Logo
        tmIdAgg.subAggregation(AggregationBuilders.terms("tmLogoUrlAgg").field("tmLogoUrl").size(1));
        query.addAggregation(tmIdAgg);

        //===========9.分析平台属性====================
        //创建嵌入式查询
        NestedAggregationBuilder attrsAgg = AggregationBuilders.nested("attrsAgg", "attrs");
        // "attrId": 1,
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId").size(100);
        //"attrName": "价格"
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName").size(1));
        //"attrValue": "2800-4499",
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue").size(100));
        // 将 attrIdAgg 放入整个子聚合中
        attrsAgg.subAggregation(attrIdAgg);
        query.addAggregation(attrsAgg);

        //返回查询条件
        return query;
    }

    /**
     * 根据检索的结果构造返回ES数据
     *
     * @param searchHits
     * @return
     */
    private GoodsSearchResultVo buildResponses(SearchHits<Goods> searchHits, SearchParam params) {
        GoodsSearchResultVo resultVo = new GoodsSearchResultVo();
        //以前的检索条件参数
        resultVo.setSearchParam(params);

        //品牌面包屑
        String trademark = params.getTrademark();
        if (!StringUtils.isEmpty(trademark)) {
            String[] split = trademark.split(":");
            resultVo.setTrademarkParam("品牌:" + split[1]);
        }

        //URL参数 ？ list.html?（urlParam）
        String urlParam = buildParam(params);
        resultVo.setUrlParam(urlParam);
        //平台属性面包屑 props=24:128G:机身内存
        List<SearchAttr> searchAttrs = new ArrayList<>();
        if (params.getProps() != null && params.getProps().length > 0) {
            for (String prop : params.getProps()) {
                SearchAttr searchAttr = new SearchAttr();
                String[] split = prop.split(":");
                searchAttr.setAttrId(Long.parseLong(split[0]));
                searchAttr.setAttrValue(split[1]);
                searchAttr.setAttrName(split[2]);
                searchAttrs.add(searchAttr);
            }
        }
        resultVo.setPropsParamList(searchAttrs);
        //品牌列表信息
        List<SearchTrademarkVo> trademarkList = analyseTrademarkList(searchHits);
        resultVo.setTrademarkList(trademarkList);


        //平台属性列表信息
        List<SearchAttrListVo> attrListVos = analyseAttrList(searchHits);
        resultVo.setAttrsList(attrListVos);

        //排序条件
        //order=1:desc 2:asc
        String order = params.getOrder();
        if (!StringUtils.isEmpty(order)) {
            String[] split = order.split(":");
            SearchOrderMapVo orderMapVo = new SearchOrderMapVo();
            orderMapVo.setType(split[0]);
            orderMapVo.setSort(split[1]);
            resultVo.setOrderMap(orderMapVo);
        }


        //检索到的所有的商品的集合
        List<Goods> goods = Lists.newArrayList();
        for (SearchHit<Goods> hit : searchHits) {
            Goods content = hit.getContent();
            goods.add(content);
        }
        resultVo.setGoodsList(goods);


        //当前页
        resultVo.setPageNo(params.getPageNo());
        //总页码
        Long pages = searchHits.getTotalHits() % params.getPageSize() == 0 ? searchHits.getTotalHits() / params.getPageSize() : (searchHits.getTotalHits() / params.getPageSize() + 1);
        resultVo.setTotalPages(pages.intValue());

        return resultVo;
    }


    /**
     * URL参数
     *
     * @param params
     * @return
     */
    private String buildParam(SearchParam params) {
        StringBuilder URLParam = new StringBuilder("list.html?");
        if (params.getCategory1Id() != null) {
            URLParam.append("category1Id=" + params.getCategory1Id() + "&");
        }
        if (params.getCategory2Id() != null) {
            URLParam.append("category2Id=" + params.getCategory2Id() + "&");
        }
        if (params.getCategory3Id() != null) {
            URLParam.append("category3Id=" + params.getCategory3Id() + "&");
        }
        //trademark=1:小米
        if (!StringUtils.isEmpty(params.getTrademark())) {
            URLParam.append("trademark=" + params.getTrademark() + "&");
        }
        //keyword=小米
        if (!StringUtils.isEmpty(params.getKeyword())) {
            URLParam.append("keyword=" + params.getKeyword() + "&");
        }
        //props=23:8G:运行内存
        if (params.getProps() != null && params.getProps().length > 0) {
            for (String prop : params.getProps()) {
                URLParam.append("props=" + prop + "&");
            }
        }
        return URLParam.toString();
    }

    /**
     * 检索区:品牌列表信息
     *
     * @param searchHits
     * @return
     */
    private List<SearchTrademarkVo> analyseTrademarkList(SearchHits<Goods> searchHits) {
        List<SearchTrademarkVo> searchTrademarkVoList = new ArrayList<>();

        Aggregations aggregations = searchHits.getAggregations();
        ParsedLongTerms tmIdAgg = aggregations.get("tmIdAgg");
        for (Terms.Bucket bucket : tmIdAgg.getBuckets()) {
            SearchTrademarkVo trademarkVo = new SearchTrademarkVo();
            //桶中的key为tmId
            Number tmId = bucket.getKeyAsNumber();
            trademarkVo.setTmId(tmId.longValue());
            //设置tmName
            ParsedStringTerms tmNameAgg = bucket.getAggregations().get("tmNameAgg");
            String tmName = tmNameAgg.getBuckets().get(0).getKeyAsString();
            trademarkVo.setTmName(tmName);
            //设置tmLogoUrl
            ParsedStringTerms tmLogoUrlAgg = bucket.getAggregations().get("tmLogoUrlAgg");
            String tmLogoUrl = tmLogoUrlAgg.getBuckets().get(0).getKeyAsString();
            trademarkVo.setTmLogoUrl(tmLogoUrl);

            searchTrademarkVoList.add(trademarkVo);
        }
        return searchTrademarkVoList;

    }

    /**
     * 检索区:平台属性信息
     *
     * @param hits
     * @return
     */
    private List<SearchAttrListVo> analyseAttrList(SearchHits<Goods> hits) {
        List<SearchAttrListVo> result = new ArrayList<>();

        Aggregations aggregations = hits.getAggregations();

        //1、拿到 attrsAgg 聚合结果
        ParsedNested attrsAgg = aggregations.get("attrsAgg");


        //2、 拿到 attrsAgg 里面的 attrIdAgg  聚合结果
        ParsedLongTerms attrIdAgg = attrsAgg.getAggregations().get("attrIdAgg");
        for (Terms.Bucket bucketItem : attrIdAgg.getBuckets()) {
            SearchAttrListVo attr = new SearchAttrListVo();
            //1、属性Id
            Number attrid = bucketItem.getKeyAsNumber();
            attr.setAttrId(attrid.longValue());

            //2、属性名
            ParsedStringTerms attrNameAgg = bucketItem.getAggregations().get("attrNameAgg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            attr.setAttrName(attrName);

            //3、属性值
            ParsedStringTerms attrValueAgg = bucketItem.getAggregations().get("attrValueAgg");
            List<String> attrValueList = new ArrayList<>();
            for (Terms.Bucket bucket : attrValueAgg.getBuckets()) {
                String attrValue = bucket.getKeyAsString();
                attrValueList.add(attrValue);
            }
            attr.setAttrValueList(attrValueList);


            //返回当前属性
            result.add(attr);
        }

        //返回属性集合
        return result;
    }


}
