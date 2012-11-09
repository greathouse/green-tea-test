package com.greenmoonsoftware.tea.TeaTests

import org.mockito.ArgumentCaptor

import com.greenmoonsoftware.tea.Result
import com.greenmoonsoftware.tea.Tea

import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import static org.mockito.Mockito.*
import groovy.mock.interceptor.*

class PostTest extends GroovyTestCase {
	
	void ignoretest_ShouldCallPost() {
		def expectedUrl = "/test"
		def expectedData = [key:"value"] 
		
		def rest = mock(RESTClient.class)
		
		def tea = new Tea(rest) 
		tea.post(expectedUrl, expectedData).brew() 
		
		def actualArguments = ArgumentCaptor.forClass(Map.class)
		verify(rest).post(actualArguments.capture())
		assert expectedUrl == actualArguments.value["path"]
		 
		assert actualArguments.value["body"] != null
		assert expectedData.size() == actualArguments.value["body"].size()
		assert expectedData.key == actualArguments.value.body.key
	}
	
	void ignoretest_WithoutAnyExpectClauses_ShouldResultInWarning() {
		def expectedUrl = "/test"
		def expectedData = [key:"value"]
		
		def rest = mock(RESTClient.class)
		
		def tea = new Tea(rest)
		def actual = tea.post(expectedUrl, expectedData).brew()
		
		assert actual != null
		assert actual.condition == Result.Condition.WARN
	}
	
	void ignoretest_WithExpectStatus_ShouldPass() {
		def expectedUrl = "/test"
		def expectedData = [key:"value"]
		def expectedStatus = 200
		
		def stubResponse = [getStatus:{expectedStatus}] as HttpResponseDecorator
		
		def rest = mock(RESTClient.class)
		when(rest.post(anyMap())).thenReturn(stubResponse)
		
		def tea = new Tea(rest)
		def actual = tea.post(expectedUrl, expectedData).expectStatus(expectedStatus).brew()
		
		assert actual != null
		assert actual.condition == Result.Condition.SUCCESS
	}
}
  