Green Tea Test
==============
A Groovy framework for functional testing rest services. Intended to help you relax while you REST.


Code Samples
============
Note: Please review the tests for a comprehensive look at the usage.

```groovy
new Tea('http://httpbin.org')
.get('/get')
.expectStatus(200)
.verifyResponse { json ->
	assert "http://httpbin.org/get" == json.url
}
.brew()

new Tea('http://httpbin.org')
.post('/post', ["name":"Value"])
.expectStatus(200)
.verifyResponse { json ->
	assert json.json.name == "Value"
}
.brew()

new Tea('http://httpbin.org')
.put("/put", ["name":"Value"])
.expectStatus(200)
.verifyResponse { json ->
	assert json.json.name == "Value"
}

new Tea('http://httpbin.org')
.delete("/delete",  ["$expectedKey":expectedValue])
.expectStatus(200)
.verifyResponse { json ->
	assert expectedValue == json.args."$expectedKey"
}
.brew()
```

Installation
============
Green-Tea-Test is deployed to Maven Central Repository
```gradle
testCompile group: 'com.greenmoonsoftware', name: 'green-tea-test', version: '1.0.2'
```
