package Service;

import Model.Enums.Currencies;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.SimpleDateFormat;
import java.util.*;

public class CurrencyService {

    public static SendMessage setcommand(Update update, String chatId){
        InlineKeyboardMarkup inlineKeyboardMarkup=new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons=new ArrayList<>();
        Currencies originalCurrency = CurrencyModeService.getOriginalCurrency(chatId);
        Currencies targetCurrency = CurrencyModeService.getTargetCurrency(chatId);


        for (Currencies value : Currencies.values()) {
            buttons.add(Arrays.asList(
                    InlineKeyboardButton.builder().text(getCurrencyButton(originalCurrency,value)).callbackData("ORIGINAL:"+value).build(),
                    InlineKeyboardButton.builder().text(getCurrencyButton(targetCurrency,value)).callbackData("TARGET:"+value).build()));
        }

        inlineKeyboardMarkup.setKeyboard(buttons);
        SendMessage sendMessage=new SendMessage();
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setText("Iltimos, asl va maqsadli valyutani tanlang: \n" +
                "So'ng qiymatni kiriting.");
        sendMessage.setChatId(chatId);

        return sendMessage;
    }

    public static EditMessageReplyMarkup handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        String action = param[0];
        Currencies newCurrency = Currencies.valueOf(param[1]);
        switch (action){
            case "ORIGINAL":
                CurrencyModeService.setOriginalCurrency(message.getChatId().toString(),newCurrency);
        break;
            case "TARGET":
                CurrencyModeService.setTargetCurrency(message.getChatId().toString(),newCurrency);
        break;

        }
        List<List<InlineKeyboardButton>> buttons=new ArrayList<>();
        Currencies originalCurrency = CurrencyModeService.getOriginalCurrency(message.getChatId().toString());
        Currencies targetCurrency = CurrencyModeService.getTargetCurrency(message.getChatId().toString());


        for (Currencies value : Currencies.values()) {
            buttons.add(Arrays.asList(
                    InlineKeyboardButton.builder().text(getCurrencyButton(originalCurrency,value)).callbackData("ORIGINAL:"+value).build(),
                    InlineKeyboardButton.builder().text(getCurrencyButton(targetCurrency,value)).callbackData("TARGET:"+value).build()));
        }

      return   EditMessageReplyMarkup.builder()
        .chatId(message.getChatId().toString())
        .messageId(message.getMessageId())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
        .build();


    }

    public static String getCurrencyButton (Currencies saved,Currencies current){
        return saved==current ? current + "⭕️" : current.name();
    }

    public static Optional<Double> parseDouble(String messagetext) {
        try {
            return Optional.of(Double.parseDouble(messagetext));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public static SendMessage convertor(Update update,String chatId){
        String messagetext = update.getMessage().getText();
        Optional<Double> value = CurrencyService.parseDouble(messagetext);
        Currencies originalCurrency = CurrencyModeService.getOriginalCurrency(chatId);
        Currencies targetCurrency = CurrencyModeService.getTargetCurrency(chatId);
        double ratio = CurrencyConvertionService.getConversionRatio(originalCurrency, targetCurrency,value.get());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = df.format(new Date());
        StoryService.registerStory(update,originalCurrency.name(),targetCurrency.name(),format,ratio,value.get());

        if (targetCurrency.name().equals("UZS")) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(String.format("%4.2f %s ➡️ %4.2f %s", value.get(), originalCurrency, (ratio * value.get())/value.get(), targetCurrency))
                    .build();
        }

       else {
           return SendMessage.builder()
                .chatId(chatId)
                .text(String.format("%4.2f %s ➡️ %4.2f %s",value.get(),originalCurrency,(value.get()*ratio),targetCurrency))
                .build();
       }
    }

}
