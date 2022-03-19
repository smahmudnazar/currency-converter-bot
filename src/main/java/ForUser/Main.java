package ForUser;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

    public static void main(String[] args) throws TelegramApiException {

//        ForUser.Database.readJson();
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(new TelegramBot());
    }
}
