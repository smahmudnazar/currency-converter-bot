package Service;

import Api.Api;
import Database.Database;
import Model.Constants.Constant;
import Model.Currency;
import Model.Enums.State;
import Model.User;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import Database.DatabseAdmin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TelegramServiceImpl implements TelegramService {

    @Override
    public SendMessage start(Update update, String chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setRequestContact(true);
        button.setText("Raqamni yuborish\uD83D\uDCF2");
        row.add(button);
        rowList.add(row);
        registerUser(update, chatId);
        replyKeyboardMarkup.setKeyboard(rowList);
        SendMessage sendMessage=new SendMessage();
        sendMessage.setText("Assalomu Aleykum <Currency\uD83C\uDFE6>ga xush kelibsiz! \n" +
                "Harakatni davom ettirish uchun <Raqamni yuborish\uD83D\uDCF2>ni bosing!");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setChatId(chatId);


        return sendMessage;
    }

    @Override
    public SendMessage shareContact(Update update, String chatId) {
        Contact contact = update.getMessage().getContact();


        User user = getUser(chatId);
        user.setPhone(contact.getPhoneNumber());
        user.setLastOperation(State.SHARE_CONTACT.name());
        Database.writeJson(Constant.USER);
        return SendMessage.builder()
                .text("Komandalar:\n" +
                        "/reference - Valyutalar ma'lumoti. \n" +
                        "/set_currency - Konvertatsiya amalga oshirish. \n")
                .chatId(chatId)
                .build();
    }

    @Override
    public void registerUser(Update update, String chatId) {
        boolean hasUser = false;
        if (!Database.users.isEmpty()) {
            for (User user : Database.users) {
                if (user.getChatId().equals(chatId)) {
                    hasUser = true;
                }
            }
            if (!hasUser) {
                User user = new User();
                user.setChatId(chatId);
                user.setFirstName(!update.getMessage().getFrom().getFirstName().isEmpty()
                        ? update.getMessage().getFrom().getFirstName() :
                        Constant.NO_INFO);
                user.setLastOperation(State.START.name());
                user.setUsername(update.getMessage().getFrom().getUserName());
                Database.users.add(user);
                Database.map.putIfAbsent(Constant.USER, Collections.singletonList(Database.users));
                Database.readJson();
                Database.writeJson(Constant.USER);
            }
        } else {
            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(!update.getMessage().getFrom().getFirstName().isEmpty()
                    ? update.getMessage().getFrom().getFirstName() :
                    Constant.NO_INFO);
            user.setLastOperation(State.START.name());

//            user.setUsername(!update.getMessage().getFrom().getUserName().isEmpty()
//                    ? update.getMessage().getFrom().getUserName() : Model.Constants.Constant.NO_INFO);
            user.setUsername(update.getMessage().getFrom().getUserName());
            Database.users.add(user);
            Database.map.putIfAbsent(Constant.USER, Collections.singletonList(Database.users));
            Database.writeJson(Constant.USER);

        }
    }


    @Override
    public SendMessage someText(Update update, String chatId) {
        User user = getUser(chatId);
        user.setLastOperation(State.SOME_TEXT.name());
        return SendMessage.builder()
                .text("Bunday komanda yo'q! ❌")
                .chatId(chatId)
                .build();
    }

    public User getUser(String chatId) {
        for (User user : Database.users) {
            if (user.getChatId().equals(chatId)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public SendMessage forAdmin(Update update, String chatId) {
        registerAdmin(update,chatId);
        return SendMessage.builder()
                .text("Assalamu Aleykum Admin paneliga xush kelibsiz !\n" +
                        "-------------------------------------------------------------------------------------\n" +
                        "⚙️Komandalar:\n" +
                        "\n" +
                        "\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC66#users  -  foydalanivchilar ro'yxati(xlsx).\n" +
                        "\uD83D\uDCCA#story  -  convertatsiyalar tarixi(xlsx).\n" +
                        "⚖️#currency  -  barcha kurslarni O'zbek so'miga nisbatan kurslari(pdf).\n" +
                        "\uD83D\uDCE4#reklama  -  barcha foydalanivchilarga reklama jo'natish.\n" +
                        "\uD83D\uDDDE#news  -  barcha foydalanivchilarga yangiliklar jo'natish. \n"+
                        "-------------------------------------------------------------------------------------\n")
                .chatId(chatId)
                .build();
    }

    @Override
    public SendMessage reference(Update update, String chatId){
        StringBuilder builder=new StringBuilder();

        List<Currency> currencyAll = Api.getCurrencyAll();
        for (int i = 0; i < 25; i++) {
            builder.append("\uD83C\uDFE6 CCY: "+(currencyAll.get(i).getCcy())+"\n");
            builder.append("\uD83C\uDDFA\uD83C\uDDFF CCY_UZ: "+(currencyAll.get(i).getCcyNm_UZ())+"\n");
            builder.append("\uD83C\uDDFA\uD83C\uDDF8 CCY_EN: "+(currencyAll.get(i).getCcyNm_EN())+"\n");
            builder.append("\uD83C\uDDF7\uD83C\uDDFA CCY_RU: "+(currencyAll.get(i).getCcyNm_RU())+"\n");
            builder.append("♻️ RATE: "+(currencyAll.get(i).getRate())+"\n");
            builder.append("-------------------------------------------------");
            builder.append("\n");
        }


        return SendMessage.builder()
                .chatId(chatId)
                .text(builder.toString())
                .build();
    }
    @Override
    public SendMessage forSuperAdmin(Update update, String chatId) {
        return SendMessage.builder()
                .text("Assalamu Aleykum SuperAdmin paneliga xush kelibsiz !\n" +
                        "-------------------------------------------------------------------------------------\n" +
                        "⚙Qo'shimcha komandalar:\n" +
                        "\n" +
                        "\uD83D\uDC68\u200D✈️ *admins - barcha adminlar ro'yxati(xlsx).\n" +
                        "\uD83D\uDD10 *setpassword - adminlar parolini o'zgartirish.\n" +
                        "Hozirgi adminlar paroli: < " + SuperAdminService.password.getPassword()+" >\n" +
                        "-------------------------------------------------------------------------------------\n")
                .chatId(chatId)
                .build();
    }

    @SneakyThrows
    public void registerAdmin(Update update, String chatId) {
        File file1 = new File("src/main/resources/users.json");

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file1));
        Gson gson = new Gson();

        List<List<User>> list = gson.fromJson(bufferedReader, new TypeToken<List<List<User>>>() {
        }.getType());
        List<User> userList = list.get(0);


        boolean hasUser = false;
        if (!DatabseAdmin.users.isEmpty()) {
            for (User user : DatabseAdmin.users) {
                if (user.getChatId().equals(chatId)) {
                    hasUser = true;
                }
            }
            if (!hasUser) {
                User user = new User();
                user.setChatId(chatId);
                user.setFirstName(!update.getMessage().getFrom().getFirstName().isEmpty()
                        ? update.getMessage().getFrom().getFirstName() :
                        Constant.NO_INFO);
                user.setLastOperation(State.START.name());
                user.setUsername(update.getMessage().getFrom().getUserName());
                for (User user1 : userList) {
                    if (chatId.equals(user1.getChatId())){
                        user.setPhone(user1.getPhone());
                    }
                }
                DatabseAdmin.users.add(user);
                DatabseAdmin.map.putIfAbsent(Constant.USER, Collections.singletonList(DatabseAdmin.users));
                DatabseAdmin.readJson();
                DatabseAdmin.writeJson(Constant.USER);
            }
        } else {
            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(!update.getMessage().getFrom().getFirstName().isEmpty()
                    ? update.getMessage().getFrom().getFirstName() :
                    Constant.NO_INFO);
            user.setLastOperation(State.START.name());
            for (User user1 : userList) {
                if (chatId.equals(user1.getChatId())){
                    user.setPhone(user1.getPhone());
                }
            }
            user.setUsername(update.getMessage().getFrom().getUserName());
            DatabseAdmin.users.add(user);
            DatabseAdmin.map.putIfAbsent(Constant.USER, Collections.singletonList(DatabseAdmin.users));
            DatabseAdmin.writeJson(Constant.USER);

        }
    }
}
