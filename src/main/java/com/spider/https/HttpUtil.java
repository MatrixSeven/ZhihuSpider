package com.spider.https;


//=======================================================
//		          .----.
//		       _.'__    `.
//		   .--(^)(^^)---/!\
//		 .' @          /!!!\
//		 :         ,    !!!!
//		  `-..__.-' _.-\!!!/
//		        `;_:    `"'
//		      .'"""""`.
//		     /,  ya ,\\
//		    //狗神保佑\\
//		    `-._______.-'
//		    ___`. | .'___
//		   (______|______)
//=======================================================

import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;

import java.io.*;

/**
 * [Zhihu]https://www.zhihu.com/people/Sweets07
 * [Github]https://github.com/MatrixSeven
 * [Blog]https://matrixseven.github.io/
 * [Email]hacker.kill07@gmail.com
 * Created by seven on 2016/11/25.
 */
public class HttpUtil {

    private static Object deserializeMyContext(String path) throws Exception {
        File file = new File(path);
        InputStream fis = new FileInputStream(file);
        ObjectInputStream ois = null;
        Object object = null;
        try {
            ois = new ObjectInputStream(fis);
            object = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (ois != null) {
                ois.close();
            }
            fis.close();

        }
        return object;
    }

    public static boolean deserializeCookie(String path, HttpClientContext httpClientContext) {
        try {
            CookieStore cookieStore = (CookieStore) deserializeMyContext(path);
            httpClientContext.setCookieStore(cookieStore);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void serializeCookie(Object object, String filePath) {
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();
            fos.close();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = null;
        RequestConfig globalConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
                .build();
        httpClient = HttpClients.custom()
                .setDefaultRequestConfig(globalConfig)
                .build();
        return httpClient;
    }

    public static HttpClientContext getHttpClientContext() {
        HttpClientContext context = null;
        context = HttpClientContext.create();
        Registry<CookieSpecProvider> registry = RegistryBuilder
                .<CookieSpecProvider>create()
                .register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
                .register(CookieSpecs.BROWSER_COMPATIBILITY,
                        new BrowserCompatSpecFactory()).build();
        context.setCookieSpecRegistry(registry);
        return context;
    }

    public static void downloadFile(CloseableHttpClient httpClient
            , HttpClientContext context
            , String fileURL
            , String path
            , String saveFileName
            , Boolean isReplaceFile) {
        try {
            HttpGet request = new HttpGet(fileURL);
            CloseableHttpResponse response = httpClient.execute(request, context);
            File file = new File(path);
            if (!file.exists() && !file.isDirectory()) {
                file.mkdirs();
            } else {
            }
            file = new File(path + saveFileName);
            if (!file.exists() || isReplaceFile) {
                try {
                    OutputStream os = new FileOutputStream(file);
                    InputStream is = response.getEntity().getContent();
                    byte[] buff = new byte[(int) response.getEntity().getContentLength()];
                    while (true) {
                        int read = is.read(buff);
                        if (read == -1) {
                            break;
                        }
                        byte[] temp = new byte[read];
                        System.arraycopy(buff, 0, temp, 0, read);
                        os.write(temp);
                    }
                    is.close();
                    os.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
            }
            request.releaseConnection();
        } catch (IllegalArgumentException e) {

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    public static String unicode2String(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);
            string.append((char) data);
        }
        return string.toString();
    }
}
