package io.inbox;

import java.nio.file.Path;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import io.inbox.email.EmailRepository;
import io.inbox.email.EmailService;
import io.inbox.emailslist.EmailListItemRepository;
import io.inbox.folders.Folder;
import io.inbox.folders.FolderRepository;
import io.inbox.folders.UnreadEmailStatusRepository;

@SpringBootApplication
@RestController
public class SpringGitHubLoginApplication {
	@Autowired
	FolderRepository folderRepository;
	@Autowired
	EmailService emailService;

	public static void main(String[] args) {
		SpringApplication.run(SpringGitHubLoginApplication.class, args);
	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

	@PostConstruct
	public void init() {
		folderRepository.save(new Folder("Feras1995", "Inbox", "blue"));
		folderRepository.save(new Folder("Feras1995", "sent", "green"));
		folderRepository.save(new Folder("Feras1995", "important", "yellow"));
		
		for(int i=0;i<10;i++){
			emailService.SendEmail("Feras1995", Arrays.asList("Feras1995","Feras-Habib"), "subject"+i,"body"+i);
		}

	}

}
