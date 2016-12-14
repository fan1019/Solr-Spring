package com.fmh.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

public class SolrPush {

	@Test
	public void test() throws IOException, SolrServerException {
		SolrClient client = new HttpSolrClient.Builder("http://localhost:8080/solr/solr/").build();
		SolrInputDocument doc = new SolrInputDocument();
		doc.setField("id",5);
		doc.setField("title"," solr的智能提示");
		doc.setField("count",30);
		client.add(doc);
		client.commit();
	}
}
