package io.pivotal.poc.cs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ReaderApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ReaderApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {

		readerService.readMap();

//
//		List<DataMessage> received = new ArrayList<>();
//		while(received.isEmpty()) {
//			received = readerService.receive("recipient");
//		}
	}

	@Autowired
	private ReaderService readerService;
	private static final Logger LOG = LoggerFactory.getLogger(ReaderApplication.class);
}
