package com.fmh.action;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
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
@RequestMapping("/suggest")
public class SuggestAction {

	@RequestMapping(value = "/get", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public List<String> get(HttpServletRequest request, HttpServletResponse response) {
		String q = request.getParameter("q");
		SolrClient client = new HttpSolrClient.Builder("http://localhost:8080/solr/solr/").build();
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("qt","/suggest");
		queryMap.put("q", q);
		queryMap.put("spellcheck.build","true");
		System.out.println(q);
		NamedList<String> namedList = new NamedList<>(queryMap);
		try {
			List<SpellCheckResponse.Suggestion> suggestions = client.query(SolrParams.toSolrParams(namedList)).getSpellCheckResponse().getSuggestions();
			List<String> result = new ArrayList<>();
			for (SpellCheckResponse.Suggestion suggestion : suggestions){
				result.addAll(suggestion.getAlternatives());
			}
			return result;
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
