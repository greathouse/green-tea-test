package com.greenmoonsoftware.tea

import org.apache.http.client.ClientProtocolException
import org.apache.http.client.ResponseHandler
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.params.HttpParams
import org.apache.http.protocol.HttpContext

import java.lang.invoke.MethodHandleImpl

class GreenTeaHttpClient extends DefaultHttpClient {
    HttpRequestBase request

    GreenTeaHttpClient(final HttpParams params) {
        super(params)
    }

    def execute(
            HttpUriRequest request,
            ResponseHandler<? extends MethodHandleImpl.BindCaller.T> responseHandler,
            HttpContext context)
            throws IOException, ClientProtocolException {
        this.request = request
        super.execute(request, responseHandler, context)
    }
}
