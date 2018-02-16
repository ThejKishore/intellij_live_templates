package ${PACKAGE_NAME};


import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.*;
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


    private static Boolean sslDebug = Boolean.FALSE;

    public static SSLSocketFactory getCustomSSLSocketFactory() throws RuntimeException{

        final String identityStoreFile =  properties.getProperty("server.ssl.identity.key.store.file");
        final String identityKeyPwd = properties.getProperty("server.ssl.identity.key.store.password");

        final String trustStoreFile = properties.getProperty("server.ssl.trust.key.store.file");
        final String trustStorePwd = properties.getProperty("server.ssl.trust.key.store.password");

        final String aliasName = properties.getProperty("alias.name");

        final Boolean runLocal = Boolean.parseBoolean(properties.getProperty("running.local"));

        final String sslProtocol = properties.getProperty("server.ssl.protocol");

        sslDebug = Boolean.parseBoolean(properties.getProperty("server.ssl.debug.enabled","false"));

        final char[] identityKeypwdArray = identityKeyPwd.toCharArray();
        final char[] trustStorePwdArray = trustStorePwd.toCharArray();

        if(sslDebug){
            System.setProperty("javax.net.debug","ALL");
            System.setProperty("ssl.debug","true");
        }


        SSLContext sslCtx = null;
        try{


            sslCtx = SSLContext.getInstance(sslProtocol);

            KeyStore trustStore = KeyStore.getInstance("JKS");
            if(runLocal){
                trustStore.load(new FileInputStream(new File(trustStoreFile)),null);
            } else{
                trustStore.load(new FileInputStream(new File(trustStoreFile)),trustStorePwdArray);
            }

            log.info("trustStore loaded successfully");

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(new File(identityStoreFile)),identityKeypwdArray);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

            kmf.init(keyStore,identityKeypwdArray);
            KeyManager[] keyManagers = kmf.getKeyManagers();


            log.info("Inside ${NAME} keyManagers length"+keyManagers.length);

            for (int i = 0; i < keyManagers.length; i++) {
                if(keyManagers[i] instanceof X509KeyManager){
                    keyManagers[i] = new KeyManagerBasedOnAlias((X509KeyManager) keyManagers[i],aliasName);
                }
            }

            sslCtx.init(keyManagers,tmf.getTrustManagers(),new SecureRandom());
            SSLContext.setDefault(sslCtx);


        } catch (IOException e) {
            log.error("IOExceptions ",e);
        } catch (CertificateException e) {
            log.error("CertificateException ",e);
        } catch (UnrecoverableKeyException e) {
            log.error("UnrecoverableKeyException ",e);
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException ",e);
        } catch (KeyStoreException e) {
            log.error("KeyStoreException ",e);
        } catch (KeyManagementException e) {
            log.error("KeyManagementException ",e);
        }

        return sslCtx.getSocketFactory();
    }

    private static class KeyManagerBasedOnAlias implements X509KeyManager{
        private X509KeyManager x509KeyManager;
        private String alias;

        public KeyManagerBasedOnAlias(X509KeyManager keyManager, String alias){
            this.x509KeyManager = keyManager;
            this.alias = alias;
            log.info("KeyManagerBasedOnAlias  {} ",alias);
        }

        public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket){
            boolean aliasFound = Boolean.FALSE;

            for (int i = 0; i < keyType.length && !aliasFound ; i++) {

                String[] validAliases = x509KeyManager.getClientAliases(keyType[i],issuers);
                if(validAliases != null){
                    for (int j = 0; j < validAliases.length ; j++) {
                        if(validAliases[j].equals(alias)){
                            aliasFound = Boolean.TRUE;
                        }
                    }
                }

            }
            log.info("Inside chooseClientAlias for alias : {} found identity : {} ",alias,aliasFound);
            if(aliasFound){
                return alias;
            }else{
                return  null;
            }
        }

        @Override
        public String[] getClientAliases(String s, Principal[] principals) {
            return x509KeyManager.getClientAliases(s,principals);
        }

        @Override
        public String[] getServerAliases(String s, Principal[] principals) {
            return x509KeyManager.getServerAliases(s,principals);
        }

        @Override
        public String chooseServerAlias(String s, Principal[] principals, Socket socket) {
            return x509KeyManager.chooseServerAlias(s,principals,socket);
        }

        @Override
        public X509Certificate[] getCertificateChain(String s) {
            return x509KeyManager.getCertificateChain(s);
        }

        @Override
        public PrivateKey getPrivateKey(String s) {
            return x509KeyManager.getPrivateKey(s);
        }


    }

}
