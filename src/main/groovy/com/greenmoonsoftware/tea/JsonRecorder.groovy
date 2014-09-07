package com.greenmoonsoftware.tea

import groovy.json.JsonBuilder

class JsonRecorder {
    final static queryEncoders = [
            'application/x-www-form-urlencoded': JsonRecorder.&urlEncode
    ].withDefault {
        { query ->
            new JsonBuilder(query).toString()
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
                , 'body': data.responseBody.toString()
            ]
        ]
        new groovy.json.JsonBuilder(json).toPrettyString()
    }

    private static String extractContentType(requestHeaders) {
        requestHeaders.find { k, v -> k.toLowerCase() == 'content-type'}?.value
    }

    private static encodeBody(contentType, body) {
        def encoder = queryEncoders[contentType]
        encoder(body)
    }

    private static urlEncode(params){
        def encode = { URLEncoder.encode( it, 'UTF-8') }
        params.collect { "${encode(it.key)}=${encode(it.value)}" }.join('&')
    }

}
