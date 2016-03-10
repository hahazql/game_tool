package com.hahazql.tools.http;

import com.hahazql.tools.helper.LogMgr;
import com.hahazql.tools.io.IOTinyUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;


public class HttpTinyClient {

    private static final String CHARSET = "charset=";
    /**
     * 连接超时,默认5秒
     */
    private static final int DEFAULT_CONNECT_TIMEOUT = 5000;
    /**
     * 读取超时,默认5秒
     */
    private static final int DEFAULT_READ_TIMEOUT = 5000;
    /**
     * 连接local的参数编码
     */
    private static final String DEFAULT_ENCODE_TYPE = "utf-8";

    /**
     * 按照utf-8的编码格式进行编码
     *
     * @param param
     * @return
     */
    public static String encode(String param) {
        try {
            return URLEncoder.encode(param, DEFAULT_ENCODE_TYPE);
        } catch (IOException e) {

            // 出异常了返回自身
            return param;
        }
    }

    /**
     * 获取指定地址的内容,如果能够从URLConnection中可以解析出编码则使用解析出的编码;否则就使用GBK编码
     *
     * @param requestUrl
     * @return
     * @throws IOException
     */
    public static String getUrl(String requestUrl) throws IOException {
        final long _begin = System.nanoTime();
        BufferedReader reader = null;
        HttpURLConnection urlConnection = null;
        try {
            InputStream urlStream;
            URL url = new URL(requestUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
            urlConnection.setReadTimeout(DEFAULT_READ_TIMEOUT);
            urlConnection.connect();
            urlStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(urlStream,
                    parseEncoding(urlConnection)));
            char[] _buff = new char[128];
            StringBuilder temp = new StringBuilder();
            int _len = -1;
            while ((_len = reader.read(_buff)) != -1) {
                temp.append(_buff, 0, _len);
            }
            return temp.toString();
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                urlConnection.disconnect();
            } catch (Exception e) {
                LogMgr.error(HttpTinyClient.class, e.getMessage());
            }
        }
    }

    /**
     * 对url的参数进行编码，并返回编码后的url
     *
     * @param requestParmUrl 带参数的url请求
     * @param params         ulr中的参数
     * @return
     */
    public static String encodeUrl(String requestParmUrl, Object... params) {
        // 对所有的字符类型参数进行编码
        for (int i = 0; i < params.length; i++) {
            Object _o = params[i];
            if (_o != null && _o instanceof String) {
                params[i] = encode((String) params[i]);
            }
        }
        return String.format(requestParmUrl, params);
    }

    /**
     * 带参数的url请求, 会先对URL中的参数进行编码
     *
     * @param requestParmUrl 请求的url
     * @param params         请求的参数
     * @return 返回的结果
     * @throws IOException
     */
    public static String getUrl(String requestParmUrl, Object... params)
            throws IOException {
        String _url = encodeUrl(requestParmUrl, params);
        return getUrl(_url);
    }

    /**
     * 尝试解析Http请求的编码格式,如果没有解析到则使用GBK编码(主要考虑到Local平台的返回编码是gb2312的)
     *
     * @param urlConnection
     * @return
     */
    static String parseEncoding(HttpURLConnection urlConnection) {
        String _encoding = urlConnection.getContentEncoding();
        if (_encoding != null) {
            return _encoding;
        }
        String _contentType = urlConnection.getContentType();
        if (_contentType != null) {
            int _index = _contentType.toLowerCase().indexOf(CHARSET);
            if (_index > 0) {
                _encoding = _contentType.substring(_index + CHARSET.length());
            }
        }
        if (_encoding != null) {
            return _encoding;
        } else {
            return DEFAULT_ENCODE_TYPE;
        }
    }


    /**
     * 发送GET请求。
     */
    static public HttpResult httpGet(String url, List<String> headers, List<String> paramValues,
                                     String encoding, long readTimeoutMs) throws IOException {
        String encodedContent = encodingParams(paramValues, encoding);
        url += (null == encodedContent) ? "" : ("?" + encodedContent);

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout((int) readTimeoutMs);
            conn.setReadTimeout((int) readTimeoutMs);
            setHeaders(conn, headers, encoding);

            conn.connect();
            int respCode = conn.getResponseCode(); // 这里内部发送请求
            String resp = null;

            if (HttpURLConnection.HTTP_OK == respCode) {
                resp = IOTinyUtils.toString(conn.getInputStream(), encoding);
            } else {
                resp = IOTinyUtils.toString(conn.getErrorStream(), encoding);
            }
            return new HttpResult(respCode, resp);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    /**
     * 发送POST请求。
     *
     * @param url
     * @param headers       请求Header，可以为null
     * @param paramValues   参数，可以为null
     * @param encoding      URL编码使用的字符集
     * @param readTimeoutMs 响应超时
     * @return
     * @throws java.io.IOException
     */
    static public HttpResult httpPost(String url, List<String> headers, List<String> paramValues,
                                      String encoding, long readTimeoutMs) throws IOException {
        String encodedContent = encodingParams(paramValues, encoding);

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout((int) readTimeoutMs);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            setHeaders(conn, headers, encoding);

            conn.getOutputStream().write(encodedContent.getBytes());

            int respCode = conn.getResponseCode(); // 这里内部发送请求
            String resp = null;

            if (HttpURLConnection.HTTP_OK == respCode) {
                resp = IOTinyUtils.toString(conn.getInputStream(), encoding);
            } else {
                resp = IOTinyUtils.toString(conn.getErrorStream(), encoding);
            }
            return new HttpResult(respCode, resp);
        } finally {
            if (null != conn) {
                conn.disconnect();
            }
        }
    }


    static private void setHeaders(HttpURLConnection conn, List<String> headers, String encoding) {
        if (null != headers) {
            for (Iterator<String> iter = headers.iterator(); iter.hasNext(); ) {
                conn.addRequestProperty(iter.next(), iter.next());
            }
        }
        conn.addRequestProperty("Client-Version", "");
        conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + encoding);

        // 其它
        String ts = String.valueOf(System.currentTimeMillis());
        conn.addRequestProperty("Metaq-Client-RequestTS", ts);
    }


    static private String encodingParams(List<String> paramValues, String encoding)
            throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        if (null == paramValues) {
            return null;
        }

        for (Iterator<String> iter = paramValues.iterator(); iter.hasNext(); ) {
            sb.append(iter.next()).append("=");
            sb.append(URLEncoder.encode(iter.next(), encoding));
            if (iter.hasNext()) {
                sb.append("&");
            }
        }
        return sb.toString();
    }

    static public class HttpResult {
        final public int code;
        final public String content;


        public HttpResult(int code, String content) {
            this.code = code;
            this.content = content;
        }
    }
}
