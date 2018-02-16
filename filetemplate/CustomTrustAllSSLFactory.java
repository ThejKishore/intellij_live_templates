package ${PACKAGE_NAME};

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;

@Slf4j
public class ${NAME} {


    private static Properties properties = new Properties();

    static{
        try{
            log.info("before the ssl properties loading");
            properties.load(${NAME}.class.getClassLoader().getResourceAsStream("ssl_configuration.properties"));
            log.info("loaded the properties");
        }catch (IOException e)
        {
            log.error("IOException ...... ",e);
        }
    }

    public static SSLSocketFactory createTrustALLSSLSocket() throws Exception{
        TrustManager[] trustAllManager = new TrustManager[] {new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};
        SSLContext sslContext = SSLContext.getInstance(properties.getProperty("server.ssl.protocol","TLSv1.1"));
        sslContext.init(null,trustAllManager,new SecureRandom());
        return  sslContext.getSocketFactory();
    }

    public static HostnameVerifier allowAllHost(){
        return new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
    }

}
