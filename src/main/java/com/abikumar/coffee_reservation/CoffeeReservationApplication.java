package com.abikumar.coffee_reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

@SpringBootApplication
@EnableAsync
public class CoffeeReservationApplication {

	public static void main(String[] args) {
		// Pin the JVM clock to India Standard Time so every LocalDateTime.now() call
		// (reservation created_date, OTP expiry, auth token expiry, etc.) lines up
		// exactly with the MySQL "serverTimezone=Asia/Kolkata" JDBC setting. Without
		// this, a server whose OS timezone isn't IST would silently store times that
		// are hours off from the real wall-clock time.
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
		SpringApplication.run(CoffeeReservationApplication.class, args);
	}

}
