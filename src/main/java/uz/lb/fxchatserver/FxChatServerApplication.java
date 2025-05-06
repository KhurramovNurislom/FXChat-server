package uz.lb.fxchatserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FxChatServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(FxChatServerApplication.class, args);
		System.out.println("Assalom aleykum!.. FXChat-server...");
	}
}
