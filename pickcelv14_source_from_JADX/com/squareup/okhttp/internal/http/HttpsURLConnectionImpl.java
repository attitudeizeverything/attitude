package com.squareup.okhttp.internal.http;

import android.annotation.SuppressLint;
import com.squareup.okhttp.OkHttpClient;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SecureCacheResponse;
import java.net.URL;
import java.security.Permission;
import java.security.Principal;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public final class HttpsURLConnectionImpl extends HttpsURLConnection {
    private final HttpUrlConnectionDelegate delegate;

    private final class HttpUrlConnectionDelegate extends HttpURLConnectionImpl {
        private HttpUrlConnectionDelegate(URL url, OkHttpClient client) {
            super(url, client);
        }

        public HttpURLConnection getHttpConnectionToCache() {
            return HttpsURLConnectionImpl.this;
        }

        public SecureCacheResponse getSecureCacheResponse() {
            if (this.httpEngine instanceof HttpsEngine) {
                return (SecureCacheResponse) this.httpEngine.getCacheResponse();
            }
            return null;
        }
    }

    public HttpsURLConnectionImpl(URL url, OkHttpClient client) {
        super(url);
        this.delegate = new HttpUrlConnectionDelegate(url, client, null);
    }

    public String getCipherSuite() {
        SecureCacheResponse cacheResponse = this.delegate.getSecureCacheResponse();
        if (cacheResponse != null) {
            return cacheResponse.getCipherSuite();
        }
        SSLSocket sslSocket = getSslSocket();
        if (sslSocket != null) {
            return sslSocket.getSession().getCipherSuite();
        }
        return null;
    }

    public Certificate[] getLocalCertificates() {
        SecureCacheResponse cacheResponse = this.delegate.getSecureCacheResponse();
        if (cacheResponse != null) {
            List<Certificate> result = cacheResponse.getLocalCertificateChain();
            if (result != null) {
                return (Certificate[]) result.toArray(new Certificate[result.size()]);
            }
            return null;
        }
        SSLSocket sslSocket = getSslSocket();
        if (sslSocket != null) {
            return sslSocket.getSession().getLocalCertificates();
        }
        return null;
    }

    public Certificate[] getServerCertificates() throws SSLPeerUnverifiedException {
        SecureCacheResponse cacheResponse = this.delegate.getSecureCacheResponse();
        if (cacheResponse != null) {
            List<Certificate> result = cacheResponse.getServerCertificateChain();
            if (result != null) {
                return (Certificate[]) result.toArray(new Certificate[result.size()]);
            }
            return null;
        }
        SSLSocket sslSocket = getSslSocket();
        if (sslSocket != null) {
            return sslSocket.getSession().getPeerCertificates();
        }
        return null;
    }

    public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
        SecureCacheResponse cacheResponse = this.delegate.getSecureCacheResponse();
        if (cacheResponse != null) {
            return cacheResponse.getPeerPrincipal();
        }
        SSLSocket sslSocket = getSslSocket();
        if (sslSocket != null) {
            return sslSocket.getSession().getPeerPrincipal();
        }
        return null;
    }

    public Principal getLocalPrincipal() {
        SecureCacheResponse cacheResponse = this.delegate.getSecureCacheResponse();
        if (cacheResponse != null) {
            return cacheResponse.getLocalPrincipal();
        }
        SSLSocket sslSocket = getSslSocket();
        if (sslSocket != null) {
            return sslSocket.getSession().getLocalPrincipal();
        }
        return null;
    }

    public HttpEngine getHttpEngine() {
        return this.delegate.getHttpEngine();
    }

    private SSLSocket getSslSocket() {
        if (this.delegate.httpEngine == null || !this.delegate.httpEngine.connected) {
            throw new IllegalStateException("Connection has not yet been established");
        } else if (this.delegate.httpEngine instanceof HttpsEngine) {
            return ((HttpsEngine) this.delegate.httpEngine).getSslSocket();
        } else {
            return null;
        }
    }

    public void disconnect() {
        this.delegate.disconnect();
    }

    public InputStream getErrorStream() {
        return this.delegate.getErrorStream();
    }

    public String getRequestMethod() {
        return this.delegate.getRequestMethod();
    }

    public int getResponseCode() throws IOException {
        return this.delegate.getResponseCode();
    }

    public String getResponseMessage() throws IOException {
        return this.delegate.getResponseMessage();
    }

    public void setRequestMethod(String method) throws ProtocolException {
        this.delegate.setRequestMethod(method);
    }

    public boolean usingProxy() {
        return this.delegate.usingProxy();
    }

    public boolean getInstanceFollowRedirects() {
        return this.delegate.getInstanceFollowRedirects();
    }

    public void setInstanceFollowRedirects(boolean followRedirects) {
        this.delegate.setInstanceFollowRedirects(followRedirects);
    }

    public void connect() throws IOException {
        this.connected = true;
        this.delegate.connect();
    }

    public boolean getAllowUserInteraction() {
        return this.delegate.getAllowUserInteraction();
    }

    public Object getContent() throws IOException {
        return this.delegate.getContent();
    }

    public Object getContent(Class[] types) throws IOException {
        return this.delegate.getContent(types);
    }

    public String getContentEncoding() {
        return this.delegate.getContentEncoding();
    }

    public int getContentLength() {
        return this.delegate.getContentLength();
    }

    public String getContentType() {
        return this.delegate.getContentType();
    }

    public long getDate() {
        return this.delegate.getDate();
    }

    public boolean getDefaultUseCaches() {
        return this.delegate.getDefaultUseCaches();
    }

    public boolean getDoInput() {
        return this.delegate.getDoInput();
    }

    public boolean getDoOutput() {
        return this.delegate.getDoOutput();
    }

    public long getExpiration() {
        return this.delegate.getExpiration();
    }

    public String getHeaderField(int pos) {
        return this.delegate.getHeaderField(pos);
    }

    public Map<String, List<String>> getHeaderFields() {
        return this.delegate.getHeaderFields();
    }

    public Map<String, List<String>> getRequestProperties() {
        return this.delegate.getRequestProperties();
    }

    public void addRequestProperty(String field, String newValue) {
        this.delegate.addRequestProperty(field, newValue);
    }

    public String getHeaderField(String key) {
        return this.delegate.getHeaderField(key);
    }

    public long getHeaderFieldDate(String field, long defaultValue) {
        return this.delegate.getHeaderFieldDate(field, defaultValue);
    }

    public int getHeaderFieldInt(String field, int defaultValue) {
        return this.delegate.getHeaderFieldInt(field, defaultValue);
    }

    public String getHeaderFieldKey(int position) {
        return this.delegate.getHeaderFieldKey(position);
    }

    public long getIfModifiedSince() {
        return this.delegate.getIfModifiedSince();
    }

    public InputStream getInputStream() throws IOException {
        return this.delegate.getInputStream();
    }

    public long getLastModified() {
        return this.delegate.getLastModified();
    }

    public OutputStream getOutputStream() throws IOException {
        return this.delegate.getOutputStream();
    }

    public Permission getPermission() throws IOException {
        return this.delegate.getPermission();
    }

    public String getRequestProperty(String field) {
        return this.delegate.getRequestProperty(field);
    }

    public URL getURL() {
        return this.delegate.getURL();
    }

    public boolean getUseCaches() {
        return this.delegate.getUseCaches();
    }

    public void setAllowUserInteraction(boolean newValue) {
        this.delegate.setAllowUserInteraction(newValue);
    }

    public void setDefaultUseCaches(boolean newValue) {
        this.delegate.setDefaultUseCaches(newValue);
    }

    public void setDoInput(boolean newValue) {
        this.delegate.setDoInput(newValue);
    }

    public void setDoOutput(boolean newValue) {
        this.delegate.setDoOutput(newValue);
    }

    public void setIfModifiedSince(long newValue) {
        this.delegate.setIfModifiedSince(newValue);
    }

    public void setRequestProperty(String field, String newValue) {
        this.delegate.setRequestProperty(field, newValue);
    }

    public void setUseCaches(boolean newValue) {
        this.delegate.setUseCaches(newValue);
    }

    public void setConnectTimeout(int timeoutMillis) {
        this.delegate.setConnectTimeout(timeoutMillis);
    }

    public int getConnectTimeout() {
        return this.delegate.getConnectTimeout();
    }

    public void setReadTimeout(int timeoutMillis) {
        this.delegate.setReadTimeout(timeoutMillis);
    }

    public int getReadTimeout() {
        return this.delegate.getReadTimeout();
    }

    public String toString() {
        return this.delegate.toString();
    }

    public void setFixedLengthStreamingMode(int contentLength) {
        this.delegate.setFixedLengthStreamingMode(contentLength);
    }

    public void setChunkedStreamingMode(int chunkLength) {
        this.delegate.setChunkedStreamingMode(chunkLength);
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.delegate.client.setHostnameVerifier(hostnameVerifier);
    }

    public HostnameVerifier getHostnameVerifier() {
        return this.delegate.client.getHostnameVerifier();
    }

    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.delegate.client.setSslSocketFactory(sslSocketFactory);
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return this.delegate.client.getSslSocketFactory();
    }

    @SuppressLint({"NewApi"})
    public void setFixedLengthStreamingMode(long contentLength) {
        this.delegate.setFixedLengthStreamingMode(contentLength);
    }
}
