package com.greenmoonsoftware.tea

import junit.framework.TestCase


class HttpBinTest extends TestCase {
	Tea tea
	
	void setUp() {
		tea = new Tea('http://httpbin.org')
	}
	
	void test_GetWithExpectStatus() {
		tea.get('/get')
		.expectStatus(200)
		.verifyResponse { json ->
			assert "http://httpbin.org/get" == json.url
		}
		.brew() 
	}
	
	void test_418Status() {
		tea.get('/status/418')
		.expectStatus(418)
		.brew()
	}
	
	void test_Post() {
		tea.post('/post', ["name":"Value"])
		.expectStatus(200)
		.verifyResponse { json ->
			assert json.json.name == "Value"
		}
		.brew()
	}
	
	void test_ResponseHeaders() {
		def expectedKey = "X-Green-Tea-Test"
		def expectedValue = "Relax"
		tea.get('/response-headers', ["$expectedKey":expectedValue])
		.expectStatus(200)
		.verifyHeaders { headers ->
			assert expectedValue == headers."$expectedKey"
		}
		.brew() 
	}
	
	void test_Put() {
		tea.put("/put", ["name":"Value"])
		.expectStatus(200)
		.verifyResponse { json ->
			assert json.json.name == "Value"
		}
	}
	
	void test_OverrideUserAgent() {
		def expectedUserAgent = "green-tea-test/1.0"
		tea.get("/user-agent")
		.userAgent(expectedUserAgent)
		.verifyResponse { json ->
			assert expectedUserAgent == json."user-agent"
		}
		.brew()
	}
	
	void test_Delete() {
		def expectedKey = "X-Green-Tea-Test"
		def expectedValue = "Relax"
		tea.delete("/delete",  ["$expectedKey":expectedValue])
		.expectStatus(200)
		.verifyResponse { json ->
			assert expectedValue == json.args."$expectedKey"
		}
		.brew()
	}
	
	void test_Delete_NoQueryParams() {
		def expectedKey = "X-Green-Tea-Test"
		def expectedValue = "Relax"
		tea.delete("/delete")
		.expectStatus(200)
		.brew()
	}
	
	void test_AddRequestHeader() {
		def authorization = "Basic aGJtLmFwaS51c2VyOmE="
		tea.get("/basic-auth/hbm.api.user/a")
		.addHeader('Authorization', authorization)
		.expectStatus(200)
		.verifyResponse { json ->
			assert json.authenticated == true
		}
		.brew()
	}
	
	void test_AddBasicAuth() {
		def username = "hbm.api.user"
		def password = "a"
		tea.get("/basic-auth/${username}/${password}")
		.basicAuth(username, password)
		.expectStatus(200)
		.verifyResponse { json ->
			assert json.authenticated == true
		}
		.brew()
	}
	
	void test_LogRequest() {
		tea.get('/get')
		.log()
		.expectStatus(200)
		.verifyResponse { json ->
			assert "http://httpbin.org/get" == json.url
		}
		.brew()
	}
}
