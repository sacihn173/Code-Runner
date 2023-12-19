package com.ContestSite.CodeCompiler.TelegramUtil;

import com.ContestSite.CodeCompiler.Entities.CustomRunResponse;
import org.jvnet.hk2.annotations.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TelegramBot extends TelegramLongPollingBot {

//    private final RunProgramCpp service;

    public TelegramBot() {
//        this.service = new RunProgramCpp();
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
//        String code = message.getText();
//        long time = System.currentTimeMillis();
//        JobRequest request = new JobRequest();
//        request.setCode(code);
//        request.setTestCase("");
//        request.setCodeLanguage("C++");
//        // TODO : provide a better unique id for each request
//        request.setUniqueSubmissionId(String.valueOf(time));
//        return service.runCPPProgram(request);
        return null;
    }

    @Override
    public String getBotUsername() {
        return "bot username here";
    }

    public String getBotToken() {
        return "bot token here";
    }
}
