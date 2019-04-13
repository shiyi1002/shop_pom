package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements ISearchService{
    @Autowired
    private SolrClient solrClient;
    @Override
    public List<Goods> searchGoods(String keyword) {
        SolrQuery solrQuery=new SolrQuery();
        //判断关键字是否有
       if(keyword==""){//为空就查询全部
           keyword="*:*";
       }
       solrQuery.setHighlightSimplePre("<font color='red'>");//设置高亮的前缀
       solrQuery.setHighlightSimplePost("</font>");//设置高亮的后缀
       solrQuery.addHighlightField("gname");//设置高亮的字段
       //设置查询条件
        solrQuery.setQuery("gname:" + keyword + " || ginfo:" + keyword);

        try {
            QueryResponse  response = solrClient.query(solrQuery);
            //获得高亮的结果集
            //第一个参数是高亮的id,后面Map<String, List<String>>是高亮的内容
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

            //查询到的结果集
            SolrDocumentList results = response.getResults();
            //遍历结果集
            List<Goods> goodsList=new ArrayList<>();

            for (SolrDocument result : results) {
                //把它装到List<Goods>中
                Goods good=new Goods();
                good.setId(Integer.parseInt(result.get("id")+""));
                good.setGname(result.get("gname")+"");
                good.setGimage(result.get("gimage")+"");
                good.setGinfo(result.get("ginfo")+"");
                good.setGsave(Integer.parseInt(result.get("gsave")+""));
                good.setGprice(BigDecimal.valueOf(Double.parseDouble(result.get("gprice")+"")));

                if(highlighting.containsKey(good.getId()+"")){//判断是否有高亮
                    //获得这个id对应的高亮的内容
                    Map<String, List<String>> maplist=highlighting.get(good.getId()+"");
                    String s = maplist.get("gname").get(0);
                    good.setGname(s);//讲高亮的内容替换到对象中
                }
                goodsList.add(good);
            }

            return goodsList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public int insertSearch(Goods goods) {

        try {
            SolrInputDocument document=new SolrInputDocument();
            document.setField("gname",goods.getGname());
            document.setField("gimage",goods.getGimage());
            document.setField("ginfo",goods.getGinfo());
            document.setField("gsave",goods.getGsave());
            document.setField("gprice",goods.getGprice().doubleValue());
            document.setField("id",goods.getId());

            solrClient.add(document);
            solrClient.commit();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
