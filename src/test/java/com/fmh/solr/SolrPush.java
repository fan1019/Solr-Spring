package com.fmh.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class SolrPush {

	@Test
	public void test() throws IOException, SolrServerException {
		SolrClient client = new HttpSolrClient.Builder("http://localhost:8080/solr/test/").build();

		client.deleteByQuery("*:*");
		int i = 1;
		for (String str : Arrays.asList("Apple","联想","惠普","三星","戴尔","神舟","宏碁","联想","三星","联想")) {
			Random random = new Random();
			SolrInputDocument doc = new SolrInputDocument();
			doc.setField("id", i++);
			doc.setField("title", str);
			doc.setField("status", random.nextInt(10));
			client.add(doc);
		}
		client.commit();
	}

	@Test
	public void test2(){
		SolrClient client = new HttpSolrClient.Builder("http://localhost:8080/solr/solr/").build();
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("qt","/suggest");
		queryMap.put("q", "北大");
		queryMap.put("spellcheck.build","true");
		NamedList namedList = new NamedList(queryMap);
		try {
			List<SpellCheckResponse.Suggestion>suggestions = client.query(SolrParams.toSolrParams(namedList)).getSpellCheckResponse().getSuggestions();
			for (SpellCheckResponse.Suggestion suggestion : suggestions){
				System.out.println(suggestion.getAlternatives());
			}
			System.out.println();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test3() throws IOException, SolrServerException {
		SolrClient client = new HttpSolrClient.Builder("http://localhost:8080/solr/test/").build();
		SolrQuery query = new SolrQuery();
		query.setFacet(true);
		query.setFacetLimit(10);
		query.setQuery("*:*");
		query.addFacetField("title","status");
		SolrParams params = SolrParams.toSolrParams(query.toNamedList());
		QueryResponse response = client.query(params);
		List<FacetField> facets = response.getFacetFields();
		for (FacetField facetField : facets){
			System.out.println(facetField.getName());
			for (FacetField.Count count : facetField.getValues()){
				System.out.println(count.getName() + ":" + count.getCount());
			}
		}
	}
}
