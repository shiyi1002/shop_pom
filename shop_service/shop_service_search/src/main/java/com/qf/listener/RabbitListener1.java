package com.qf.listener;

import com.qf.entity.Goods;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
@Component
public class RabbitListener1 {
    @Autowired
    private SolrClient solrClient;

    /**
     * 监听消息处理
     * @param goods
     */
    @RabbitListener(queues="good_queue1")
    public void listenMsg(Goods goods){
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
