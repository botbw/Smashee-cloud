package server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/config/")
public class ServerConfig {
    @Value("${server.address}")
    private String serverAddress;

    @RequestMapping("getServerInfo/")
    public String getServerInfo() {
        return serverAddress;
    }
}
