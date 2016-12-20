package com.fmh.action;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/suggest")
public class SuggestAction {

	@RequestMapping(value = "/get", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public SolrDocumentList get(HttpServletRequest request, HttpServletResponse response) {
		String q = request.getParameter("q");
		SolrClient client = new HttpSolrClient.Builder("http://localhost:8080/solr/solr/").build();
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("q", "id:"+q);
		System.out.println(q);
		NamedList namedList = new NamedList(queryMap);
		try {
			return client.query(SolrParams.toSolrParams(namedList)).getResults();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
