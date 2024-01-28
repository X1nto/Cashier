package dev.xinto.cashier.common.network

import java.net.InetAddress
import java.net.Socket
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

class TlsCompatSocketFactory : SSLSocketFactory() {

    private val delegate = SSLContext.getInstance("TLSv1")
        .apply {
            init(null, null, null)
        }.socketFactory

    override fun createSocket(s: Socket?, host: String?, port: Int, autoClose: Boolean): Socket {
        return interceptedSslSocket(delegate.createSocket(s, host, port, autoClose))
    }

    override fun createSocket(host: String?, port: Int): Socket {
        return interceptedSslSocket(delegate.createSocket(host, port))
    }

    override fun createSocket(
        host: String?,
        port: Int,
        localHost: InetAddress?,
        localPort: Int
    ): Socket {
        return interceptedSslSocket(delegate.createSocket(host, port, localHost, localPort))
    }

    override fun createSocket(host: InetAddress?, port: Int): Socket {
        return interceptedSslSocket(delegate.createSocket(host, port))
    }

    override fun createSocket(
        address: InetAddress?,
        port: Int,
        localAddress: InetAddress?,
        localPort: Int
    ): Socket {
        return interceptedSslSocket(delegate.createSocket(address, port, localAddress, localPort))
    }

    private fun interceptedSslSocket(socket: Socket): Socket {
        return socket.apply {
            if (this is SSLSocket) {
                enabledProtocols = arrayOf("TLSv1.1", "TLSv1.2")
            }
        }
    }

    override fun getDefaultCipherSuites(): Array<String> {
        return delegate.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return delegate.supportedCipherSuites
    }
}