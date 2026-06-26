package com.restaurant.ordering.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

@RestController
public class QRController {

    @Value("${server.port:8080}")
    private int serverPort;

    @Value("${app.public-url:}")
    private String publicUrl;

    @GetMapping(value = "/api/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateQR() throws WriterException, IOException {
        String menuUrl = resolveMenuUrl();

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(menuUrl, BarcodeFormat.QR_CODE, 300, 300);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return out.toByteArray();
    }

    @GetMapping("/api/server-url")
    public String getServerUrl() {
        return resolveMenuUrl();
    }

    private String resolveMenuUrl() {
        // 1. Explicit override in config
        if (publicUrl != null && !publicUrl.isBlank()) {
            return publicUrl.endsWith("/") ? publicUrl : publicUrl + "/";
        }
        // 2. Render provides its full public URL automatically
        String renderUrl = System.getenv("RENDER_EXTERNAL_URL");
        if (renderUrl != null && !renderUrl.isBlank()) {
            return renderUrl.endsWith("/") ? renderUrl : renderUrl + "/";
        }
        // 3. Fallback: local network IP (same-WiFi use)
        return "http://" + getLocalNetworkIp() + ":" + serverPort + "/";
    }

    private String getLocalNetworkIp() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp()) continue;
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    // Prefer IPv4 addresses that start with 192.168 or 10. (common LAN)
                    String ip = addr.getHostAddress();
                    if (!addr.isLoopbackAddress() && ip.indexOf(':') == -1) {
                        return ip;
                    }
                }
            }
        } catch (Exception ignored) {}
        return "localhost";
    }
}
