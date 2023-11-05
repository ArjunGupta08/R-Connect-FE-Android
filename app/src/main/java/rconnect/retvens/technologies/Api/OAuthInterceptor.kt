package rconnect.retvens.technologies.Api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class OAuthInterceptor(private val accessToken: String, private val headers:Map<String,String>):
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()

        val requestBuilder: Request.Builder = original.newBuilder()
            .header("authcode", "$accessToken")

        headers.forEach {
            requestBuilder.header(it.key, it.value)
        }

        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}