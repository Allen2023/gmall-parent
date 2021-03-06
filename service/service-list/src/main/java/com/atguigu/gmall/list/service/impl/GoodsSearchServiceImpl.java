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
     * ???????????????ES
     *
     * @param goods
     */
    @Override
    public void saveGoods(Goods goods) {
        goodsDao.save(goods);
    }

    /**
     * ???????????????ES?????????
     *
     * @param skuId
     */
    @Override
    public void deleteGoods(Long skuId) {
        goodsDao.deleteById(skuId);
    }

    /**
     * ??????????????????
     *
     * @param params
     * @return
     */
    @Override
    public GoodsSearchResultVo searchGoods(SearchParam params) {
        //??????????????????????????????,???????????????????????????
        Query query = buildQueryBySearchParam(params);
        SearchHits<Goods> searchHits = restTemplate.search(query, Goods.class, IndexCoordinates.of("goods"));
        GoodsSearchResultVo resultVo = buildResponses(searchHits, params);
        return resultVo;
    }

    /**
     * ??????????????????
     *
     * @param skuId
     * @param score
     */
    @Override
    public void updateHotScore(Long skuId, Long score) {
        //??????skuId???ES??????
        Optional<Goods> goodsDaoById = goodsDao.findById(skuId);
        Goods goods = goodsDaoById.get();
        //??????????????????
        goods.setHotScore(score);
        //????????????
        goodsDao.save(goods);
        log.info("????????????????????????:" + skuId + ":" + score);
    }


    /**
     * ????????????????????????
     *
     * @param params
     * @return
     */
    private Query buildQueryBySearchParam(SearchParam params) {
        NativeSearchQuery query = null;
        //????????????bool query ????????????
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        //========1.??????????????????Id==============
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
        //========2.??????????????????==============
        if (!StringUtils.isEmpty(params.getTrademark())) {
            String[] split = params.getTrademark().split(":");
            boolQuery.must(QueryBuilders.termQuery("tmId", split[0]));
        }
        //========3.???????????????==============
        if (!StringUtils.isEmpty(params.getKeyword())) {
            boolQuery.must(QueryBuilders.termQuery("title", params.getKeyword()));
        }
        //========4.???????????????Id==============
        // ????????????????????? ???????????????  props = 23 : 4G : ????????????
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
        //???????????????????????????
        query = new NativeSearchQuery(boolQuery);
        //========5.????????????==============
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
        //========6.????????????==============
        Pageable pageRequest = PageRequest.of(params.getPageNo() - 1, params.getPageSize());
        query.setPageable(pageRequest);

        //========7.????????????==============
        if (!StringUtils.isEmpty(params.getKeyword())) {
            //??????????????????????????????????????????????????????
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title").preTags("<span style='color:red'>").postTags("</span>");
            HighlightQuery highlightQuery = new HighlightQuery(highlightBuilder);
            query.setHighlightQuery(highlightQuery);
        }

        //==========8.??????????????????--????????????==============
        //?????????????????? group by  tmId,tmName,tmLogoUrl
        TermsAggregationBuilder tmIdAgg = AggregationBuilders.terms("tmIdAgg").field("tmId").size(100);
        //????????? -???????????????
        tmIdAgg.subAggregation(AggregationBuilders.terms("tmNameAgg").field("tmName").size(1));
        //????????? - ????????????Logo
        tmIdAgg.subAggregation(AggregationBuilders.terms("tmLogoUrlAgg").field("tmLogoUrl").size(1));
        query.addAggregation(tmIdAgg);

        //===========9.??????????????????====================
        //?????????????????????
        NestedAggregationBuilder attrsAgg = AggregationBuilders.nested("attrsAgg", "attrs");
        // "attrId": 1,
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId").size(100);
        //"attrName": "??????"
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName").size(1));
        //"attrValue": "2800-4499",
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue").size(100));
        // ??? attrIdAgg ????????????????????????
        attrsAgg.subAggregation(attrIdAgg);
        query.addAggregation(attrsAgg);

        //??????????????????
        return query;
    }

    /**
     * ?????????????????????????????????ES??????
     *
     * @param searchHits
     * @return
     */
    private GoodsSearchResultVo buildResponses(SearchHits<Goods> searchHits, SearchParam params) {
        GoodsSearchResultVo resultVo = new GoodsSearchResultVo();
        //???????????????????????????
        resultVo.setSearchParam(params);

        //???????????????
        String trademark = params.getTrademark();
        if (!StringUtils.isEmpty(trademark)) {
            String[] split = trademark.split(":");
            resultVo.setTrademarkParam("??????:" + split[1]);
        }

        //URL?????? ??? list.html????urlParam???
        String urlParam = buildParam(params);
        resultVo.setUrlParam(urlParam);
        //????????????????????? props=24:128G:????????????
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
        //??????????????????
        List<SearchTrademarkVo> trademarkList = analyseTrademarkList(searchHits);
        resultVo.setTrademarkList(trademarkList);


        //????????????????????????
        List<SearchAttrListVo> attrListVos = analyseAttrList(searchHits);
        resultVo.setAttrsList(attrListVos);

        //????????????
        //order=1:desc 2:asc
        String order = params.getOrder();
        if (!StringUtils.isEmpty(order)) {
            String[] split = order.split(":");
            SearchOrderMapVo orderMapVo = new SearchOrderMapVo();
            orderMapVo.setType(split[0]);
            orderMapVo.setSort(split[1]);
            resultVo.setOrderMap(orderMapVo);
        }


        //????????????????????????????????????
        List<Goods> goods = Lists.newArrayList();
        for (SearchHit<Goods> hit : searchHits) {
            Goods content = hit.getContent();
            //????????????????????? ???????????????
            if (!StringUtils.isEmpty(params.getKeyword())) {
                String title = hit.getHighlightField("title").get(0);
                content.setTitle(title);
            }
            goods.add(content);
        }
        resultVo.setGoodsList(goods);


        //?????????
        resultVo.setPageNo(params.getPageNo());
        //?????????
        Long pages = searchHits.getTotalHits() % params.getPageSize() == 0 ? searchHits.getTotalHits() / params.getPageSize() : (searchHits.getTotalHits() / params.getPageSize() + 1);
        resultVo.setTotalPages(pages.intValue());

        return resultVo;
    }


    /**
     * URL??????
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
        //trademark=1:??????
        if (!StringUtils.isEmpty(params.getTrademark())) {
            URLParam.append("trademark=" + params.getTrademark() + "&");
        }
        //keyword=??????
        if (!StringUtils.isEmpty(params.getKeyword())) {
            URLParam.append("keyword=" + params.getKeyword() + "&");
        }
        //props=23:8G:????????????
        if (params.getProps() != null && params.getProps().length > 0) {
            for (String prop : params.getProps()) {
                URLParam.append("props=" + prop + "&");
            }
        }
        return URLParam.toString();
    }

    /**
     * ?????????:??????????????????
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
            //?????????key???tmId
            Number tmId = bucket.getKeyAsNumber();
            trademarkVo.setTmId(tmId.longValue());
            //??????tmName
            ParsedStringTerms tmNameAgg = bucket.getAggregations().get("tmNameAgg");
            String tmName = tmNameAgg.getBuckets().get(0).getKeyAsString();
            trademarkVo.setTmName(tmName);
            //??????tmLogoUrl
            ParsedStringTerms tmLogoUrlAgg = bucket.getAggregations().get("tmLogoUrlAgg");
            String tmLogoUrl = tmLogoUrlAgg.getBuckets().get(0).getKeyAsString();
            trademarkVo.setTmLogoUrl(tmLogoUrl);

            searchTrademarkVoList.add(trademarkVo);
        }
        return searchTrademarkVoList;

    }

    /**
     * ?????????:??????????????????
     *
     * @param hits
     * @return
     */
    private List<SearchAttrListVo> analyseAttrList(SearchHits<Goods> hits) {
        List<SearchAttrListVo> result = new ArrayList<>();

        Aggregations aggregations = hits.getAggregations();

        //1????????? attrsAgg ????????????
        ParsedNested attrsAgg = aggregations.get("attrsAgg");


        //2??? ?????? attrsAgg ????????? attrIdAgg  ????????????
        ParsedLongTerms attrIdAgg = attrsAgg.getAggregations().get("attrIdAgg");
        for (Terms.Bucket bucketItem : attrIdAgg.getBuckets()) {
            SearchAttrListVo attr = new SearchAttrListVo();
            //1?????????Id
            Number attrid = bucketItem.getKeyAsNumber();
            attr.setAttrId(attrid.longValue());

            //2????????????
            ParsedStringTerms attrNameAgg = bucketItem.getAggregations().get("attrNameAgg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            attr.setAttrName(attrName);

            //3????????????
            ParsedStringTerms attrValueAgg = bucketItem.getAggregations().get("attrValueAgg");
            List<String> attrValueList = new ArrayList<>();
            for (Terms.Bucket bucket : attrValueAgg.getBuckets()) {
                String attrValue = bucket.getKeyAsString();
                attrValueList.add(attrValue);
            }
            attr.setAttrValueList(attrValueList);


            //??????????????????
            result.add(attr);
        }

        //??????????????????
        return result;
    }


}
