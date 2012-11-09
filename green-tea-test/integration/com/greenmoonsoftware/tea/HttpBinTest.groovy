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
		tea.post('/post', ["name":"Value"] as Map)
		.expectStatus(200)
		.brew()
	}
}
