package com.ContestSite.CodeCompiler;

import com.ContestSite.CodeCompiler.APIs.AddJobAPI;
import com.ContestSite.CodeCompiler.Scheduler.JobQueuesHandler;
import com.ContestSite.CodeCompiler.Scheduler.JobScheduler;
import com.ContestSite.CodeCompiler.Scheduler.UserQueue;
import com.ContestSite.CodeCompiler.Scheduler.UserQueueHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class CodeCompilerApplication {

	public static void main(String[] args) throws TelegramApiException {
		SpringApplication.run(CodeCompilerApplication.class, args);

		// Start picking Jobs from Queues and executes them
		JobScheduler.initialize();

//		TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
//		api.registerBot(new TelegramBot());

	}

}
