package com.greenmoonsoftware.tea

class HttpMetaData {
    String host = ''
    String uri = ''
    String method = ''
    Map requestHeaders = [:]
    Map queryParameters = [:]
    Map requestBody = [:]
    Map responseHeaders = [:]
    def responseBody
    String responseStatus = ''
}
