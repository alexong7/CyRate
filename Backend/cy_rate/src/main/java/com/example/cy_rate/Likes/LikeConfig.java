package com.example.cy_rate.Likes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class LikeConfig {
    @Bean
	public ServerEndpointExporter serverEndpointExporterLikes() {
		return new ServerEndpointExporter();
	}

    
}
