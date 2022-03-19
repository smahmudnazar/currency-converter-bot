package ForUser;

import Model.Password;
import Model.User;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.List;

public class SuperAdminService {
  public static Password password=new Password("#smu#");

    @SneakyThrows
    public static void ExcelWriter(Update update, String chatId) {
        File file1 = new File("src/main/resources/admins.json");

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file1));
        Gson gson = new Gson();

        List<List<User>> list = gson.fromJson(bufferedReader, new TypeToken<List<List<User>>>() {
        }.getType());
        List<User> userList = list.get(0);

        File file = new File("src/main/resources/adminlist.xlsx");
        FileOutputStream outputStream = new FileOutputStream(file);
        //  List<User>users = Database.users.stream().toList();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Admins");

        XSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("CHAT ID");
        row.createCell(1).setCellValue("USERNAME");
        row.createCell(2).setCellValue("PHONE");
        row.createCell(3).setCellValue("LAST NAME");
        row.createCell(4).setCellValue("FIRST NAME");

        int number = 0;
        for (User user : userList) {
            XSSFRow row1 = sheet.createRow(number + 1);
            row1.createCell(0).setCellValue(user.getChatId());
            row1.createCell(1).setCellValue(user.getUsername());
            row1.createCell(2).setCellValue(user.getPhone());
            row1.createCell(3).setCellValue(user.getLastName());
            row1.createCell(4).setCellValue(user.getFirstName());
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            number++;
        }
        workbook.write(outputStream);
        outputStream.close();
    }

    @SneakyThrows
    public static SendDocument SendAdminList(Update update, String chatId) {
        File file = new File("src/main/resources/adminlist.xlsx");
        InputFile inputFile = new InputFile(file);
        return SendDocument.builder()
                .chatId(chatId)
                .document(inputFile)
                .build();
    }

    @SneakyThrows
    public static SendMessage PasswordEditor(String newPassword,String chatId){
            String string;
            password.setPassword(newPassword.substring(2));
            string="Muvafaqiyatli o'zgartirildi !✅";
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(string)
                    .build();

    }

    @SneakyThrows
    public static SendMessage passwordSpros(Update update, String chatId) {
       return SendMessage.builder()
                .text("Yangi parolni kiriting: \n" +
                        "P=... manashu formatda kiriting. \n" +
                        "AGAR P= BO'LMASA PAROL O'ZGARMAYDI ‼️")
               .chatId(chatId)
                .build();
    }
}
