package com.ContestSite.CodeCompiler.Service;

import com.ContestSite.CodeCompiler.Controllers.HomeController;
import com.ContestSite.CodeCompiler.Models.CustomRunRequest;
import com.ContestSite.CodeCompiler.Models.CustomRunResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {

    private CPPCodeCompilerService service;

    public TelegramBot() {
        this.service = new CPPCodeCompilerService();
    }
    public TelegramBot(CPPCodeCompilerService service) {
        this.service = new CPPCodeCompilerService();
    }
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());

            String code = update.getMessage().getText();
            CustomRunRequest request = new CustomRunRequest();
            request.setCode(code);
            request.setTestCase("");
            request.setCodeLanguage("C++");
            request.setUniqueSubmissionId("SUB");
            CustomRunResponse res = service.runCPPFile(request);

            System.out.println(res.getOutput());
            System.out.println(res.getErrors());
            System.out.println(update.getMessage().getText());
            if(res.getErrors() != null && !res.getErrors().equals(""))
                message.setText(res.getErrors());
            else
                message.setText(res.getOutput());


            try {
                execute(message);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "CompileItBot";
    }

    public String getBotToken() {
        return "6214844448:AAHaEiTQQOcOZjDtmbTSNunARwI0MY6OLLQ";
    }
}
