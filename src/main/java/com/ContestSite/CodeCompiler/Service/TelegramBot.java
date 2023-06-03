package com.ContestSite.CodeCompiler.Service;

import com.ContestSite.CodeCompiler.Configurations.TelegramConfig;
import com.ContestSite.CodeCompiler.Controllers.HomeController;
import com.ContestSite.CodeCompiler.Models.CustomRunRequest;
import com.ContestSite.CodeCompiler.Models.CustomRunResponse;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
public class TelegramBot extends TelegramLongPollingBot {

    private final CPPCodeCompilerService service;

    public TelegramBot() {
        this.service = new CPPCodeCompilerService();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());

            Message recMessage = update.getMessage();

            CustomRunResponse programResult = getProgramOutput(recMessage);
            System.out.println("Program Code is : " + update.getMessage().getText());
            System.out.println("Program output is : " + programResult.getOutput());
            System.out.println("Program errors are : " + programResult.getErrors());

            if(programResult.getErrors() != null && !programResult.getErrors().equals(""))
                message.setText(programResult.getErrors());
            else
                message.setText(programResult.getOutput());

            try {
                execute(message);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private CustomRunResponse getProgramOutput(Message message) {
        String code = message.getText();
        long time = System.currentTimeMillis();
        CustomRunRequest request = new CustomRunRequest();
        request.setCode(code);
        request.setTestCase("");
        request.setCodeLanguage("C++");
        // TODO : provide a better unique id for each request
        request.setUniqueSubmissionId(String.valueOf(time));
        return service.runCPPFile(request);
    }

    @Override
    public String getBotUsername() {
        return "Bot Username Here";
    }

    public String getBotToken() {
        return "Telegram Token Here";
    }
}
