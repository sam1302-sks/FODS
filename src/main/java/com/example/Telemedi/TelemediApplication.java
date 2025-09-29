package com.example.Telemedi;

import com.example.Telemedi.ui.ChatWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TelemediApplication {
	private static ConfigurableApplicationContext springContext;

	public static void main(String[] args) {
		// Simple look and feel setup (no errors)
		setupLookAndFeel();

		// Start Spring Boot context
		springContext = SpringApplication.run(TelemediApplication.class, args);

		// Launch Swing UI
		new ChatWindow().setVisible(true);
	}

	private static void setupLookAndFeel() {
		try {
			// Try to set Nimbus look and feel
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					return;
				}
			}
		} catch (Exception e) {
			// If anything fails, just use default - no error thrown
		}
	}

	public static ConfigurableApplicationContext getSpringContext() {
		return springContext;
	}
}
