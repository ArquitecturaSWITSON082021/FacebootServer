/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Ivy
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.util.ArrayList;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class HttpsOauthServer extends Thread {

    private int port = 9999;
    private boolean isServerDone = false;
    public HttpsOauthCodeCallback OnAuthCodeCallback = null;

    public HttpsOauthServer(int port) {
        this.port = port;
    }

    // Create the and initialize the SSLContext
    private SSLContext createSSLContext() {
        try {
            String keyStorePath = System.getProperty("user.dir") + File.separatorChar + "server.p12";
            String passphrase = "itson123";

            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(keyStorePath), passphrase.toCharArray());

            // Create key manager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, passphrase.toCharArray());
            KeyManager[] km = keyManagerFactory.getKeyManagers();

            // Create trust manager
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);
            TrustManager[] tm = trustManagerFactory.getTrustManagers();

            // Initialize SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            sslContext.init(km, tm, null);

            return sslContext;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Start to run the server
    @Override
    public void run() {
        try{
        SSLContext sslContext = this.createSSLContext();

        // Create server socket factory
        SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

        // Create server socket
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(this.port);

        System.out.println("[*] Running HTTPS oAuth server at 0.0.0.0:" + port);
        while (!isServerDone) {
            SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();

            // Start the server thread
            new ServerThread(sslSocket, this).start();
        }
        }catch(Exception e){
            System.out.println("[-] Failed to run HTTPS server: " + e.getMessage());
            e.printStackTrace();
        }
        
    }

    // Thread handling the socket from client
    static class ServerThread extends Thread {

        private SSLSocket sslSocket = null;
        private HttpsOauthServer server = null;

        ServerThread(SSLSocket sslSocket, HttpsOauthServer server) {
            this.sslSocket = sslSocket;
            this.server = server;
        }

        public void run() {
            sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());

            try {
                // Start handshake
                sslSocket.startHandshake();

                // Get session after the connection is established
                SSLSession sslSession = sslSocket.getSession();

                // Start handling application content
                InputStream inputStream = sslSocket.getInputStream();
                OutputStream outputStream = sslSocket.getOutputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
                ArrayList<String> lines = new ArrayList<>();

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    lines.add(line);
                    if (line.trim().isEmpty()) {
                        break;
                    }
                }

                // Get method, URI and version
                String[] headers = lines.get(0).split(" ");
                String method = headers[0];
                String uri = headers[1];
                String httpVersion = headers[2];

                if (uri.indexOf("/?") != 0) {
                    printWriter.print("HTTP/1.1 500\r\n");
                } else {
                    
                    HttpQuery query = new HttpQuery(uri.substring(2));
                    String remoteAddr = sslSocket.getInetAddress().getHostAddress();
                    if (this.server.OnAuthCodeCallback != null) {
                        this.server.OnAuthCodeCallback.Execute(method, remoteAddr, query);
                    }
                    printWriter.print("HTTP/1.1 200\r\n");
                }

                printWriter.flush();

                sslSocket.close();
            } catch (Exception ex) {
            }
        }
    }
}
