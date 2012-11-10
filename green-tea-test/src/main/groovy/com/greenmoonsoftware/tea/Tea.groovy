package com.greenmoonsoftware.tea

import groovyx.net.http.RESTClient
import groovyx.net.http.*

class Tea {
	def host
	private def action
	private List asserts = []
	private Closure verifyResponseClosure
	private Closure verifyHeadersClosure
	private Map headers = [:]
	
	def Tea(String host) {
		this.host = host
	}
	
	@Deprecated
	def Tea(def rest) {
		this.rest = rest
	}
	
	def brew() {
		def rest = new RESTClient(host)
		
		if (headers) {
			headers.each { k,v ->
				rest.headers."${k}" = v
			}
		}
		
		def response
		try {
			response = rest."${action.method}"(action.params)
		}
		catch (HttpResponseException ex) {
			response = ex.response
		}
		
		asserts.each { a ->
			a.eval(response)
		}
		
		if (verifyHeadersClosure) {
			verifyHeadersClosure(response.headers)
		}
		
		if (verifyResponseClosure) {
			verifyResponseClosure(response.data)
		}
		new Result(condition: (asserts.size() == 0)?Result.Condition.WARN : Result.Condition.SUCCESS)	
	}
	
	def Tea get(String url, Map query = null) {
		action = [method:"get", params:[path:url, query:query]]
		return this;
	}
	
	def Tea post(String url, Map json = null){
		action = [method:"post", params:[path:url, body:json, contentType : 'application/json']]
		return this
	}
	
	def Tea put(String url, Map json = null){
		action = [method:"put", params:[path:url, body:json, contentType : 'application/json']]
		return this
	}
	
	def Tea delete(String url, Map query = null) {
		action = [method:"delete", params:[path:url, query:query]]
		return this
	}
	
	def expectStatus(int code) {
		asserts.add([eval: { response ->
			assert response.status == code  
		}])
		return this
	}
	
	def verifyResponse(Closure c) {
		verifyResponseClosure = c
		return this
	}
	
	def verifyHeaders(Closure c) {
		verifyHeadersClosure = c
		return this
	}
	
	def userAgent(String ua) {
		headers."User-Agent" = ua
		return this
	}
	
	def addHeader(String header, String value) {
		headers[header] = value
		return this
	}
	
	def basicAuth(String username, String password) {
		def auth = "Basic " + "${username}:${password}".getBytes().encodeBase64().toString()
		addHeader("Authorization", auth)
		return this
		
	}
}
