package Service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramService {
    SendMessage start(Update update, String chatId) ;

    SendMessage shareContact(Update update, String chatId);

    void registerUser(Update update, String chatId);

    SendMessage someText(Update update, String chatId);

    SendMessage forAdmin(Update update, String chatId);

    SendMessage reference(Update update, String chatId);

    SendMessage forSuperAdmin(Update update, String chatId);

    void registerAdmin(Update update, String chatId);
}
