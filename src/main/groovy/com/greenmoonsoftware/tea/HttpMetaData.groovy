package com.greenmoonsoftware.tea

class HttpMetaData {
    String host = ''
    String uri = ''
    String method = ''
    Map requestHeaders = [:]
    Map queryParameters = [:]
    def requestBody = [:]
    Map<String, List<String>> responseHeaders = [:]
    def responseBody
    int responseStatus
}
