package com.greenmoonsoftware.tea

import groovyx.net.http.RESTClient
import org.apache.http.client.HttpClient
import org.apache.http.params.HttpParams

class GreenTeaRestClient extends RESTClient {

    GreenTeaRestClient(String s) {
        super(s)
    }

    protected HttpClient createClient( HttpParams params ) {
       return new GreenTeaHttpClient(params)
    }

    def getRequest() {
        def client = getClient()
        println client.class
        client.request
    }
}
