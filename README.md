Green Tea Test
==============
A Groovy framework for functional testing rest services. Intended to help you relax while you test rest.


Code Samples
============
Note: Please review the tests for a comprehensive look at the usage.

```groovy
def tea = new Tea('http://httpbin.org')
tea.get('/get')
.expectStatus(200)
.verifyResponse { json ->
	assert "http://httpbin.org/get" == json.url
}
.brew() 


```