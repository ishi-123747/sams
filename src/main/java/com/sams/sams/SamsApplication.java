package com.sams.sams;

import com.sams.sams.model.User;
import com.sams.sams.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.List;

@SpringBootApplication
public class SamsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SamsApplication.class, args);
	}

	@Bean
	public CommandLineRunner createDefaultAdmin(UserRepository userRepository) {
		return args -> {
			// Delete all duplicate admin users first
			List<User> existing = userRepository.findAll()
					.stream()
					.filter(u -> "admin".equals(u.getUsername()))
					.collect(java.util.stream.Collectors.toList());

			if (existing.size() > 1) {
				// Keep first, delete the rest
				existing.subList(1, existing.size())
						.forEach(u -> userRepository.deleteById(u.getId()));
				System.out.println("✅ Removed " + (existing.size()-1) + " duplicate admin users");
			} else if (existing.isEmpty()) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword("admin123");
				admin.setRole("ADMIN");
				userRepository.save(admin);
				System.out.println("✅ Default admin created: admin / admin123");
			}
		};
	}
}