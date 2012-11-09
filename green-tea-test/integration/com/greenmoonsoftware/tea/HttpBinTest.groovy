package com.greenmoonsoftware.tea

class HttpBinTest extends GroovyTestCase {
	Tea tea
	
	void setUp() {
		tea = new Tea('http://httpbin.org')
	}
	
	void test_GetWithExpectStatus() {
		tea.get('/get')
		.expectStatus(200)
		.brew() 
	}
	
	void test_500Status() {
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
}
