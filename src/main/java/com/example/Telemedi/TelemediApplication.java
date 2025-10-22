package com.example.Telemedi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TelemediApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelemediApplication.class, args);
		System.out.println("✅ TeleMedi Backend Started Successfully!");
		System.out.println("🌐 Open index.html in your browser to use the application");
		System.out.println("📡 API running on: http://localhost:8080");
	}
}
