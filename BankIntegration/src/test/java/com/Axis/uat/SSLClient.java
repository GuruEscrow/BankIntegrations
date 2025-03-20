package com.Axis.uat;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.Security;
import javax.net.ssl.*;
public class SSLClient {
    public static void main(String[] args) throws Exception {
        // Step 1: Relax security restrictions to mimic --openssl-legacy-provider
        Security.setProperty("jdk.tls.disabledAlgorithms", ""); // Remove all disabled algorithms
        Security.setProperty("jdk.tls.legacyAlgorithms", "");   // Allow legacy algorithms
        String[] legacyProtocols = {"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"}; // Enable older protocols
        // Step 2: Load keystore with client certificate
        String pfxFilePath = "./src/main/resources/AxisUAT.p12"; // Ensure correct path
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(pfxFilePath)) {
            keyStore.load(fis, null); // No password
        }
        // Step 3: Initialize KeyManagerFactory
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, null); // No key password
        // Step 4: Initialize SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);
        // Step 5: Use the SSLContext for your connection
        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        SSLSocket socket = (SSLSocket) socketFactory.createSocket("sakshamuat.axisbank.co.in", 443);
        // Explicitly enable legacy protocols
        socket.setEnabledProtocols(legacyProtocols);
        // Optional: Enable all available ciphers (including weak ones)
        socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());
        // Step 6: Start handshake
        socket.startHandshake();
        System.out.println("Handshake successful!");
        socket.close();
    }
}