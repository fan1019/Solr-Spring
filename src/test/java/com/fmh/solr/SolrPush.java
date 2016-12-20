package com.fmh.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SolrPush {

	@Test
	public void test() throws IOException, SolrServerException {
		SolrClient client = new HttpSolrClient.Builder("http://localhost:8080/solr/solr/").build();

		client.deleteByQuery("*:*");
		int i = 1;
		for (String str : Arrays.asList("北大青岛","北大核心","北大法宝","北大荒","北大招生网","北大光华管理学院","北","北京时间")) {
			SolrInputDocument doc = new SolrInputDocument();
			doc.setField("id", i++);
			doc.setField("title", str);
			doc.setField("count", 12);
			client.add(doc);
		}
		client.commit();
	}

	@Test
	public void test2(){
		SolrClient client = new HttpSolrClient.Builder("http://localhost:8080/solr/solr/").build();
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("q", "id:2");
		NamedList namedList = new NamedList(queryMap);
		try {
			System.out.println(client.query(SolrParams.toSolrParams(namedList)).getResults());
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}
}
