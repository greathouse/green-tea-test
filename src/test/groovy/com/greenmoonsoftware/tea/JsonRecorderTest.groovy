package com.greenmoonsoftware.tea

import groovy.json.JsonSlurper
import junit.framework.TestCase
import org.junit.Test

class JsonRecorderTest extends TestCase {

    void test_get() {
        def host = 'http://www.google.com'
        def uri = '/some/path'
        def method = 'GET'
        def actual = record(new HttpMetaData(host: host, uri:uri, method: method))

        assert actual.host == host
        assert actual.uri == uri
        assert actual.method == method
    }

    void test_requestHeaders() {
        def headers = [some:'value', another:'different']
        def actual = record(new HttpMetaData(requestHeaders: headers))

        assert actual.request.headers.some == 'value'
        assert actual.request.headers.another == 'different'
    }

    void test_body_contentTypeJson() {
        def actual = record(new HttpMetaData(
                requestBody: [some:'value', another:'different']
                , requestHeaders:  ['Content-Type': 'application/json']
        ))

        assert actual.request.body == /{"some":"value","another":"different"}/
    }

    void test_body_contentTypeFormUrlEncoded() {
        def actual = record(new HttpMetaData (
                requestBody: [some:'va lue', another:'dif ferent']
                , requestHeaders:  ['Content-Type': 'application/x-www-form-urlencoded']
        ))
        assert actual.request.body == 'some=va+lue&another=dif+ferent'
    }

    def record(HttpMetaData p) {
        def record = JsonRecorder.record(p)
        println record
        new JsonSlurper().parseText(record)
    }
}
