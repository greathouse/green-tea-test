package com.greenmoonsoftware.tea

import groovy.json.JsonBuilder
import groovy.xml.XmlUtil

class JsonRecorder {
    final static queryEncoders = [
            'application/x-www-form-urlencoded': JsonRecorder.&urlEncode
            , 'application/xml': JsonRecorder.&xmlEncode
            , 'application/json' : JsonRecorder.&jsonEncode
            , 'application/hal+json' : JsonRecorder.&jsonEncode
    ].withDefault {
        { query ->
            query.toString()
        }
    }

    static record(HttpMetaData data ) {
        def json = [
            'host': data.host
            , 'uri': data.uri
            , 'method': data.method
            , request: [
                headers: data.requestHeaders
                , 'query': urlEncode(data.queryParameters)
                , 'body': encodeBody(extractContentType(data.requestHeaders), data.requestBody)
            ]
            , response: [
                headers: data.responseHeaders
                , status: data.responseStatus
                , 'body': data.responseBody.toString()
            ]
        ]
        new groovy.json.JsonBuilder(json).toPrettyString()
    }

    static String extractContentType(requestHeaders) {
        requestHeaders.find { k, v -> k.toLowerCase() == 'content-type'}?.value
    }

    static encodeBody(contentType, body) {
        def encoder = queryEncoders[contentType]
        encoder(body)
    }

    private static urlEncode(params){
        def encode = { URLEncoder.encode( it, 'UTF-8') }
        params.collect { "${encode(it.key)}=${encode(it.value)}" }.join('&')
    }

    private static xmlEncode(node) {
        XmlUtil.serialize(node).toString()
    }

    private static jsonEncode(json) {
        new JsonBuilder(json).toString()
    }
}
