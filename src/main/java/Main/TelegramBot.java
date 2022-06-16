package Main;

import Service.*;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class TelegramBot extends TelegramLongPollingBot {

    //bot username
    public static final String username = "";
    //bot token
    public static final String token = "";
    //password for superAdmin
    public static final String superAdminPassword="";
    TelegramService telegramService = new TelegramServiceImpl();
    AdminService adminService=new AdminService();

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            String chatId = String.valueOf(update.getMessage().getChatId());
            if (update.getMessage().hasText()) {
                String messagetext = update.getMessage().getText();
                Optional<Double> value = CurrencyService.parseDouble(messagetext);
                log(update.getMessage().getChatId().toString(), update.getMessage().getFrom().getUserName(), update.getMessage().getText());
                if (update.getMessage().getText().equals("/start")) {
                    execute(telegramService.start(update, chatId));
                    telegramService.registerUser(update,chatId);
                }
                else if (update.getMessage().getText().equals(SuperAdminService.password.getPassword())){
                    execute(telegramService.forAdmin(update,chatId));
                }
                else if (update.getMessage().getText().equals(superAdminPassword)){
                    execute(telegramService.forSuperAdmin(update,chatId));
                }
                else if (update.getMessage().getText().equals("/set_currency")){
                    execute(CurrencyService.setcommand(update,chatId));
                }
                else if (update.getMessage().getText().equals("/reference")){
                    execute(telegramService.reference(update,chatId));
                }
                else if(update.getMessage().getText().equals("*admins")){
                   SuperAdminService.ExcelWriter(update,chatId);
                    execute(SuperAdminService.SendAdminList(update,chatId));
                }
                else if(update.getMessage().getText().equals("*setpassword")){
                    execute(SuperAdminService.passwordSpros(update,chatId));
                }
                else if (update.getMessage().getText().startsWith("P=")){
                   execute(SuperAdminService.PasswordEditor(update.getMessage().getText(),chatId));
                }
               else if(update.getMessage().getText().equals("#users")){
                    AdminService.ExcelWriter(update,chatId);
                    execute(adminService.SendDocument(update,chatId));
                }
               else if(update.getMessage().getText().equals("#currency")){
                    AdminService.PdfWriterAllCurrency();
                    execute(adminService.SendDocumentPDFList(update,chatId));
                }
                else if(update.getMessage().getText().equals("#story")){
                    AdminService.ExcelStoryWriter(update,chatId);
                    execute(adminService.SendDocumentStory(update,chatId));
                }
                else if(update.getMessage().getText().equals("#reklama")){
                    new Thread(() -> {adminService.SendReklama();}).start();
                }
                else if(update.getMessage().getText().equals("#news")){
                    new Thread(() -> {adminService.newsSend();}).start();
                }
               else if (value.isPresent()){
                   execute(CurrencyService.convertor(update,chatId));
                }
                else {
                    execute(telegramService.someText(update, chatId));
                }
            }
            else if (update.getMessage().hasContact()) {
                execute(telegramService.shareContact(update, chatId));
            }
        } else if (update.hasCallbackQuery()) {
            execute(CurrencyService.handleCallback(update.getCallbackQuery()));
        }
    }
    public void log(String chatId, String userName, String text) {
        System.out.println(chatId + "--" + userName + " : " + text);
    }
}