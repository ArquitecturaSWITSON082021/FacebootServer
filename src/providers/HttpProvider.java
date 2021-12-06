/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package providers;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ivy
 */
public class HttpProvider {

    protected String EndpointUrl;

    public HttpProvider(String url) {
        this.EndpointUrl = url;
    }

    private String DoRequest(String method, String urlQuery, HashMap<String, String> headers, byte[] data) throws Exception {
        StringBuilder resultado = new StringBuilder();
        // Crear un objeto de tipo URL
        URL url = new URL(EndpointUrl + urlQuery);

        // Abrir la conexión e indicar que será de tipo GET
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod(method);
        if (headers != null) {
            headers.entrySet().forEach((header) -> {
                conexion.setRequestProperty(header.getKey(), header.getValue());
            });
        }

        if (data != null) {
            try (DataOutputStream wr = new DataOutputStream(conexion.getOutputStream())) {
                wr.write(data);
            }
        }
        // Búferes para leer
        BufferedReader rd = null;
        try {
            rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        } catch (Exception e) {
            rd = new BufferedReader(new InputStreamReader(conexion.getErrorStream()));
            // throw e;
        }
        String linea;
        // Mientras el BufferedReader se pueda leer, agregar contenido a resultado
        while ((linea = rd.readLine()) != null) {
            resultado.append(linea);
        }
        // Cerrar el BufferedReader
        rd.close();
        // Regresar resultado, pero como cadena, no como StringBuilder
        return resultado.toString();
    }

    public String Get(String urlQuery) throws Exception {
        return DoRequest("GET", urlQuery, null, null);
    }

    public String Get(String urlQuery, HashMap<String, String> headers) throws Exception {
        return DoRequest("GET", urlQuery, headers, null);
    }

    public String Post(String urlQuery, byte[] data) throws Exception {
        return DoRequest("POST", urlQuery, null, data);
    }

    public String Post(String urlQuery, HashMap<String, String> headers) throws Exception {
        return DoRequest("POST", urlQuery, headers, null);
    }

    public byte[] GetByteArray(String urlQuery) throws Exception {
        URL url = new URL(urlQuery);

        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod("GET");

        InputStream in = null;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            in = conexion.getInputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1) {
                bout.write(buffer, 0, length);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }
        return bout.toByteArray();
    }

}
