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
		tea.get('/status/500')
		.expectStatus(500)
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
}
