package tylenol.common.util

import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * Created by baghyeongi on 2017. 5. 6..
 */
class SSLConnect {
    internal val DO_NOT_VERIFY = object : HostnameVerifier {
        override fun verify(p0: String?, p1: SSLSession?) = true
    }
    private fun trustAllHosts() {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }

            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>,
                                            authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>,
                                            authType: String) {
            }
        })

        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, java.security.SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun postHttps(url: String, connTimeout: Int, readTimeout: Int): HttpsURLConnection? {
        trustAllHosts()

        var https: HttpsURLConnection? = null
        try {
            https = URL(url).openConnection() as HttpsURLConnection
            https.setHostnameVerifier(DO_NOT_VERIFY)
            https.connectTimeout = connTimeout
            https.readTimeout = readTimeout
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return https
    }
}