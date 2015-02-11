package com.greenmoonsoftware.tea

class HttpMetaData {
    String host = ''
    String uri = ''
    String method = ''
    Map requestHeaders = [:]
    Map queryParameters = [:]
    def requestBody = [:]
    Map responseHeaders = [:]
    def responseBody
    int responseStatus
}
