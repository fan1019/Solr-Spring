package com.fmh.action;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.SolrParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/facet")
public class SolrFacet {
	private static SolrClient client = new HttpSolrClient.Builder("http://localhost:8080/solr/test/").build();

	@RequestMapping(value = "/get",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map<String,List<Map<String,Long>>> get(HttpServletRequest request, HttpServletResponse response){
		String field = request.getParameter("field");
		String[] fields = field.split(",");
		String q = request.getParameter("q");
		SolrQuery query = new SolrQuery();
		query.setFacet(true);
		query.setFacetLimit(10);
		query.setQuery("*:*");
		query.addFacetQuery(q);
		query.addFacetField(fields);
		SolrParams params = SolrParams.toSolrParams(query.toNamedList());
		try {
			QueryResponse res = client.query(params);
			List<FacetField> facets = res.getFacetFields();
			Map<String,List<Map<String,Long>>> results = new HashMap<>();
			for (FacetField facetField : facets){
				List<Map<String,Long>> listMap = new ArrayList<>();
				for (FacetField.Count count : facetField.getValues()){
					Map<String,Long> map = new HashMap<>();
					map.put(count.getName(),count.getCount());
					listMap.add(map);
				}
				results.put(facetField.getName(),listMap);
			}
			return results;
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
