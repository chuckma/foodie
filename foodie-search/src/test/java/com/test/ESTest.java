package com.test;

import com.imooc.Application;
import com.imooc.es.pojo.Stu;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author mcg
 * @Date 2020/1/14 20:52
 **/


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {

    @Autowired
    private ElasticsearchTemplate esTemplate;


    @Test
    public void createIndexStu() {
        Stu stu = new Stu();
        stu.setStuId(1005L);
        stu.setAge(24);
        stu.setName("卜真诚");
        stu.setMoney(191299.88f);
        stu.setSign("隔壁同学");
        stu.setDescription("嘉兴人，男，已婚");
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(stu).build();
        esTemplate.index(indexQuery);

    }


    /**
     * 删除索引，但是不推荐用 Java es 客户端进行索引的管理（创建，更新，删除）
     * 索引就像 数据的表，代码里一般不会对表进行创建，更新，和删除
     */
    @Test
    public void deleteIndexStu() {

        esTemplate.deleteIndex(Stu.class);

    }

    /**
     * 更新记录
     */
    @Test
    public void updateIndexStu() {
        Map<String, Object> map = new HashMap<>();
        map.put("age", 21);
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.source(map);
        UpdateQuery query = new UpdateQueryBuilder()
                .withClass(Stu.class)
                .withId("1004")
                .withIndexRequest(indexRequest)
                .build();

        esTemplate.update(query);
    }


    /**
     * 查询
     */
    @Test
    public void getStuDoc() {

        GetQuery query = new GetQuery();
        query.setId("1002");

        Stu stu = esTemplate.queryForObject(query, Stu.class);
        System.out.println(stu);
    }


    /**
     * 删除记录
     */
    @Test
    public void deleteStuDoc() {

        esTemplate.delete(Stu.class, "1002");
    }


    /**
     * 分页检索
     */
    @Test
    public void searchStuDoc() {

        Pageable pageable = PageRequest.of(0, 10);
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("description", "已婚"))
                .withPageable(pageable).build();

        AggregatedPage<Stu> stus = esTemplate.queryForPage(query, Stu.class);
        System.out.println("检索后的总分页数为：" + stus.getTotalPages());
        List<Stu> stusContent = stus.getContent();
        for (Stu stu : stusContent) {
            System.out.println(stu);
        }
    }


    /**
     * 高亮
     */
    @Test
    public void highLightSearchStuDoc() {

        String preTag = "<font color='red'>";
        String postTag = "</font>";
        Pageable pageable = PageRequest.of(0, 10);
        // 单个字段排序，多个字段的话可将将 SortBuilder 再创建一个
        SortBuilder sortBuilder = new FieldSortBuilder("money")
                .order(SortOrder.DESC);

        SortBuilder sortAge = new FieldSortBuilder("age")
                .order(SortOrder.ASC);

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("description", "男"))
                .withHighlightFields(new HighlightBuilder.Field("description").preTags(preTag).postTags(postTag))
                .withSort(sortBuilder)
                .withSort(sortAge)
                .withPageable(pageable).build();

        AggregatedPage<Stu> stus = esTemplate.queryForPage(query, Stu.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {

                List<Stu> highLight = new ArrayList<>();

                SearchHits hits = response.getHits();
                for (SearchHit hit : hits) {
                    HighlightField highlightField = hit.getHighlightFields().get("description");
                    String description = highlightField.getFragments()[0].toString();

                    Object stuId = (Object)hit.getSourceAsMap().get("stuId");
                    String  name = (String) hit.getSourceAsMap().get("name");
                    Integer age =(Integer) hit.getSourceAsMap().get("age");
                    String sign = (String) hit.getSourceAsMap().get("sign");
                    Object money = (Object) hit.getSourceAsMap().get("money");

                    Stu hlStu = new Stu();
                    hlStu.setDescription(description);
                    hlStu.setStuId(Long.valueOf(stuId.toString()));
                    hlStu.setName(name);
                    hlStu.setAge(age);
                    hlStu.setSign(sign);
                    hlStu.setMoney(Float.valueOf(money.toString()));

                    highLight.add(hlStu);
                }

                if (highLight.size() > 0) {
                    return new  AggregatedPageImpl<>((List<T>)highLight);
                }

                return null;

            }
        });
        System.out.println("检索后的总分页数为：" + stus.getTotalPages());
        List<Stu> stusContent = stus.getContent();
        for (Stu stu : stusContent) {
            System.out.println(stu);
        }
    }


}
