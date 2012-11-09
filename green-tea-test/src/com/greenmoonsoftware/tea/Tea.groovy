package com.greenmoonsoftware.tea

import groovyx.net.http.RESTClient
import groovyx.net.http.*

class Tea {
	def host
	private def action
	private List asserts = []
	private Closure verify
	
	def Tea(String host) {
		this.host = host
	}
	
	@Deprecated
	def Tea(def rest) {
		this.rest = rest
	}
	
	def brew() {
		def rest = new RESTClient(host)
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
		
		if (verify) {
			verify(response.data)
		}
		new Result(condition: (asserts.size() == 0)?Result.Condition.WARN : Result.Condition.SUCCESS)	
	}
	
	def Tea get(String url) {
		action = [method:"get", params:[path:url]]
		return this;
	}
	
	def Tea post(String url, Map json = null){
		action = [method:"post", params:[path:url, body:json, contentType : 'application/json']]
		return this
	}
	
	def expectStatus(int code) {
		asserts.add([eval: { response ->
			assert response.status == code  
		}])
		return this
	}
	
	def verifyResponse(Closure c) {
		verify = c
		return this
	}
}
