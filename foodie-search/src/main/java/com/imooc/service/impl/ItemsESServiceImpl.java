package com.imooc.service.impl;

import com.imooc.es.pojo.Items;
import com.imooc.service.ItemsESService;
import com.imooc.utils.PagedGridResult;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author mcg
 * @Date 2020/1/16 20:57
 **/
@Service
public class ItemsESServiceImpl implements ItemsESService {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public PagedGridResult searchItems(String keywords, String sort, int page, int pageSize) {


        {

//            String preTag = "<font color='red'>";
//            String postTag = "</font>";
            Pageable pageable = PageRequest.of(page, pageSize);
            String itemNameField = "itemName";
            // 单个字段排序，多个字段的话可将将 SortBuilder 再创建一个
//            SortBuilder sortBuilder = new FieldSortBuilder("money")
//                    .order(SortOrder.DESC);
//
//            SortBuilder sortAge = new FieldSortBuilder("age")
//                    .order(SortOrder.ASC);
            SortBuilder sortBuilder = null;
            if (sort.equals("c")) {
                sortBuilder = new FieldSortBuilder("sellCounts")
                        .order(SortOrder.DESC);
            } else if (sort.equals("p")) {
                sortBuilder = new FieldSortBuilder("price")
                        .order(SortOrder.ASC);
            } else {
                sortBuilder = new FieldSortBuilder("itemName.keyword")
                        .order(SortOrder.ASC);
            }

            SearchQuery query = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.matchQuery(itemNameField, keywords))
                    .withHighlightFields(new HighlightBuilder.Field(itemNameField)
//                            .preTags(preTag)
//                            .postTags(postTag)
                    )
                    .withSort(sortBuilder)
                    .withPageable(pageable).build();

            AggregatedPage<Items> pageItems = esTemplate.queryForPage(query, Items.class, new SearchResultMapper() {
                @Override
                public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {

                    List<Items> itemHighLightList = new ArrayList<>();

                    SearchHits hits = response.getHits();
                    for (SearchHit hit : hits) {
                        HighlightField highlightField = hit.getHighlightFields().get(itemNameField);
                        String itemName = highlightField.getFragments()[0].toString();

                        String itemId = (String) hit.getSourceAsMap().get("itemId");
                        String imgUrl = (String) hit.getSourceAsMap().get("imgUrl");
                        Integer price = (Integer) hit.getSourceAsMap().get("price");
                        Integer sellCounts = (Integer) hit.getSourceAsMap().get("sellCounts");

                        Items items = new Items();
                        items.setItemId(itemId);
                        items.setItemName(itemName);
                        items.setImgUrl(imgUrl);
                        items.setPrice(price);
                        items.setSellCounts(sellCounts);

                        itemHighLightList.add(items);
                    }

                    return new AggregatedPageImpl<>((List<T>) itemHighLightList, pageable, response.getHits().totalHits);

                }
            });

            PagedGridResult gridResult = new PagedGridResult();
            gridResult.setRows(pageItems.getContent()); // 每一页展示的数据
            gridResult.setPage(page + 1); // controller 里做了 -- 这里加回去，es 分页搜索是 从 0 开始的，所以减去1 特殊处理
            gridResult.setTotal(pageItems.getTotalPages()); // 总页数
            gridResult.setRecords(pageItems.getTotalElements());// 总的记录条数

            return gridResult;

        }

    }
}
