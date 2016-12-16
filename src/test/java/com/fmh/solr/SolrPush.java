package com.fmh.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class SolrPush {

	@Test
	public void test() throws IOException, SolrServerException {
		SolrClient client = new HttpSolrClient.Builder("http://localhost:8080/solr/solr/").build();
		SolrInputDocument doc = new SolrInputDocument();
		client.deleteByQuery("*:*");
		int i = 1;
		for (String str : Arrays.asList("北大青岛","北大核心","北大法宝","北大荒","北大招生网","北大光华管理学院")) {
			doc.setField("id", i++);
			doc.setField("title", str);
			doc.setField("count", 12);
			client.add(doc);
		}
		client.commit();
	}
}
