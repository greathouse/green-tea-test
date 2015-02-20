package com.greenmoonsoftware.tea

import groovy.json.JsonSlurper
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
        .log()
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
		.brew()
	}

    void test_Patch() {
        tea.patch("/patch", ['name':'Value'])
        .expectStatus(200)
        .verifyResponse { json ->
            assert json.json.name == 'Value'
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

        System.out
		tea.get('/get')
		.log()
		.expectStatus(200)
		.verifyResponse { json ->
			assert "http://httpbin.org/get" == json.url
		}
		.brew()
	}

	void test_CannotBrewMultipleTimes() {
		tea.get('/get')
		.expectStatus(200)
		.brew()

		try {
			tea.get('/get')
			.expectStatus(200)
			.brew()

			assert false, "This should throw an exception"
		}
		catch (all) {
			assert true
		}
	}

	void test_GetWithFullUrlGiven_ShouldOverwriteBaseUrl() {
		new Tea("http://bogus.com")
		.get('http://httpbin.org/get')
		.log()
		.expectStatus(200)
		.brew()
	}

	void test_PostWithFullUrlGiven_ShouldOverwriteBaseUrl() {
		new Tea("http://bogus.com").post('http://httpbin.org/post', ["name":"Value"])
		.expectStatus(200)
		.verifyResponse { json ->
			assert json.json.name == "Value"
		}
		.brew()
	}

	void test_PutWithFullUrlGiven_ShouldOverwriteBaseUrl() {
		new Tea("http://bogus.com").put("http://httpbin.org/put", ["name":"Value"])
		.expectStatus(200)
		.verifyResponse { json ->
			assert json.json.name == "Value"
		}
		.brew()
	}

	void test_DeleteWithFullUrlGiven_ShouldOverwriteBaseUrl() {
		def expectedKey = "X-Green-Tea-Test"
		def expectedValue = "Relax"

		new Tea("http://bogus.com").delete("http://httpbin.org/delete",  ["$expectedKey":expectedValue])
		.expectStatus(200)
		.verifyResponse { json ->
			assert expectedValue == json.args."$expectedKey"
		}
		.brew()
	}

    void test_customContentTypeParsing() {
        tea.get('/response-headers', ['content-type' : 'application/hal+json', 'accept' : 'application/hal+json'])
        .withParser('application/hal+json') {rest ->
            rest.parser.'application/json'
        }
        .expectStatus(200)
        .verifyResponse { json ->
            json."content-type" == 'application/hal+json'
        }
        .brew()
    }

    void test_Post_xwww_form_urlencoded() {
      tea.post('/post', [
              'grant_type':'password'
              , 'username': 'blah'
              , 'password': 'otherblah'
              , 'client_id': 'my_client'
      ], 'application/x-www-form-urlencoded')
      .log()
      .expectStatus(200)
      .verifyResponse { json ->
              assert json.headers."Content-Type" == 'application/x-www-form-urlencoded'
              assert json.form.client_id == 'my_client'
              assert json.form.grant_type == 'password'
              assert json.form.password == 'otherblah'
              assert json.form.username == 'blah'
      }.brew()
    }

    void test_recorder() {
        def tea = new Tea('http://requestb.in')
        def calledRecorder = false
        tea.post('/19s6ddl1', [
                key: 'value'
                , otherKey: 'otherValue'
        ])
//        .log()
        .withRecorder { data ->
            println JsonRecorder.record(data)
            calledRecorder = true
            assert data.host == 'http://requestb.in'
            assert data.uri == '/19s6ddl1'
            assert data.method == 'POST'
            assert data.requestBody
            assert !data.queryParameters
            assert data.responseHeaders
            assert data.responseBody
        }.brew()

        assert calledRecorder
    }

    void test_recorderWithGetQueryParams() {
        boolean calledRecorder = false
        tea.get('/get', [param: 'value'])
        .expectStatus(200)
        .log()
        .withRecorder { data ->
            calledRecorder = true
            assert !data.requestBody
            assert data.queryParameters
        }.brew()

        assert calledRecorder
    }

    void test_xml() {
        tea.get('/xml')
        .expectStatus(200)
        .log()
        .brew()
    }

    void test_getWithQueryParamsInPath_ThrowsException() {
        try {
            tea.get('/get?foo=bar')
            .brew()
        } catch (IllegalArgumentException e) {
            assert e.message == 'URL cannot have query params. Please pass as a map as the second param to \'get\'.'
        }
    }
}
