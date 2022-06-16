package Service;

import Api.Api;
import Main.TelegramBot;
import Model.Currency;
import Model.Story;
import Model.User;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;
import java.util.List;

public class AdminService {
    @SneakyThrows
    public static void ExcelWriter(Update update, String chatId) {
        File file1 = new File("src/main/resources/users.json");

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file1));
        Gson gson = new Gson();

        List<List<User>> list = gson.fromJson(bufferedReader, new TypeToken<List<List<User>>>() {
        }.getType());
        List<User> userList = list.get(0);

        File file = new File("src/main/resources/UserList.xlsx");
        FileOutputStream outputStream = new FileOutputStream(file);
        //  List<User>users = Database.users.stream().toList();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Users");

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
    public static void ExcelStoryWriter(Update update, String chatId) {
        File file1 = new File("src/main/resources/story.json");

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file1));
        Gson gson = new Gson();

        List<List<Story>> list = gson.fromJson(bufferedReader, new TypeToken<List<List<Story>>>() {
        }.getType());
        List<Story> userList = list.get(0);

        File file = new File("src/main/resources/story.xlsx");
        FileOutputStream outputStream = new FileOutputStream(file);
        //  List<User>users = Database.users.stream().toList();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Story");

        XSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("NAME");
        row.createCell(1).setCellValue("DATE");
        row.createCell(2).setCellValue("ORIGINAL");
        row.createCell(3).setCellValue("TARGET");
        row.createCell(4).setCellValue("FROM");
        row.createCell(5).setCellValue("TO");

        int number = 0;
        for (Story user : userList) {
            XSSFRow row1 = sheet.createRow(number + 1);
            row1.createCell(0).setCellValue(user.getUserName());
            row1.createCell(1).setCellValue(user.getDate());
            row1.createCell(2).setCellValue(user.getOriginal());
            row1.createCell(3).setCellValue(user.getTarget());
            row1.createCell(4).setCellValue(user.getValuefrom());
            row1.createCell(5).setCellValue(user.getValueto());


            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);

            number++;
        }
        workbook.write(outputStream);
        outputStream.close();
    }

    @SneakyThrows
    public SendDocument SendDocumentStory(Update update, String chatId) {
        File file = new File("src/main/resources/story.xlsx");
        InputFile inputFile = new InputFile(file);
        return SendDocument.builder()
                .chatId(chatId)
                .document(inputFile)
                .build();
    }

    @SneakyThrows
    public SendDocument SendDocument(Update update, String chatId) {
        File file = new File("src/main/resources/UserList.xlsx");
        InputFile inputFile = new InputFile(file);
        return SendDocument.builder()
                .chatId(chatId)
                .document(inputFile)
                .build();
    }

    @SneakyThrows
    public static void PdfWriterAllCurrency() {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/currency.pdf"));
        document.open();
        Font font = new Font();
        font.setSize(30);
        font.setStyle(Font.BOLD);

        Paragraph paragraph = new Paragraph("Valyuta kurslari\n", font);
        paragraph.setAlignment(Element.ALIGN_CENTER);


        Font font5 = new Font();
        font5.setStyle(Font.BOLDITALIC);
        Paragraph paragraph2 = new Paragraph("Valyuta birligi ", font5);
        Paragraph paragraph3 = new Paragraph("Valyuta miqdori ", font5);

        PdfPTable table = new PdfPTable(2);
        float[] column = {1f, 1f};
        table.setWidths(column);
        PdfPCell cell = new PdfPCell(new Paragraph(paragraph2));
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell cell1 = new PdfPCell(new Paragraph(paragraph3));
        cell1.setVerticalAlignment(Element.ALIGN_CENTER);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(cell);
        table.addCell(cell1);

        document.add(paragraph);
        Paragraph paragraph1 = new Paragraph("                             " +
                "                                  ");
        paragraph1.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph1);
        document.add(table);


        for (Currency currency : Api.getCurrencyAll()) {
            PdfPTable table1 = new PdfPTable(2);
            float[] column1 = {1f, 1f};
            table1.setWidths(column1);
            font.setSize(18);

            PdfPCell cell2 = new PdfPCell(new Paragraph("-  " + currency.getCcyNm_UZ()));
            cell2.setVerticalAlignment(Element.ALIGN_CENTER);
            font.setSize(15);
            font.setStyle(Font.ITALIC);
            font.setColor(BaseColor.RED);
            PdfPCell cell3 = new PdfPCell(new Paragraph(currency.getRate() + " " + currency.getCcy(), font));
            cell3.setVerticalAlignment(Element.ALIGN_CENTER);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell2);
            table1.addCell(cell3);
            document.add(table1);
        }
        document.close();
        writer.close();
    }

    @SneakyThrows
    public SendDocument SendDocumentPDFList(Update update, String chatId) {
        File file = new File("src/main/resources/currency.pdf");
        InputFile inputFile = new InputFile(file);
        return SendDocument.builder()
                .chatId(chatId)
                .document(inputFile)
                .build();

    }

    @SneakyThrows
    public void SendReklama() {
        Gson gson = new Gson();
        File file = new File("src/main/resources/users.json");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        List<List<User>> users = gson.fromJson(reader, new com.google.gson.reflect.TypeToken<List<List<User>>>() {
        }.getType());
        List<User> userList = users.get(0);

        File file1 = new File("src/main/resources/PDP.jpg");
        InputFile inputFile = new InputFile(file1);

        for (User user : userList) {
            new TelegramBot()
                    .execute(SendPhoto.builder()
                            .chatId(user.getChatId())
                            .photo(inputFile)
                            .caption("\uD83D\uDFE2 Onlayn chegirma foizlari kamayishiga 3 kun qoldi!\n" +
                                    "\n" +
                                    "Onlaynda samarali oʻqish uchun sizga yozib olingan videodarslar va PDP assistenti yordam beradi, chegirmalar esa oʻqishni yanada yoqimli qiladi:\n" +
                                    "\n" +
                                    "• 22% — 15-dekabrgacha\n" +
                                    "• 15% — 16-dekabrdan 31-dekabrgacha \n" +
                                    "\n" +
                                    "Barcha kurslar \uD83D\uDD17 pdp.uz saytida!\n" +
                                    "\n" +
                                    "Kanalga ulanish \uD83D\uDC49\uD83C\uDFFB @pdpuz\n" +
                                    "\n" +
                                    "\uD83D\uDD17 Roʻyxatdan oʻtish: @PDPSupportBot\n" +
                                    "\n" +
                                    "Veb-sayt  (https://bit.ly/3Dw0jJJ)/ Instagram (https://bit.ly/3ouaZnM) / Youtube (https://bit.ly/33bX6mh) / TikTok (https://bit.ly/3lJDQTw)")
                            .build());
        }
    }


    @SneakyThrows
    public void newsSend() {
        Connection connect = Jsoup.connect("https://kun.uz/uz/news/category/sport");
        org.jsoup.nodes.Document document = connect.get();
        Elements dateList = document.getElementsByClass("small-news__content");
//        Elements titleList = document.getElementsByClass("small-news__title");

        StringBuilder builder = new StringBuilder();

        builder.append("\uD83D\uDDDE BREAKING NEWS: \n");
        builder.append("                     \n");

        for (org.jsoup.nodes.Element element : dateList) {
            Elements link = element.getElementsByTag("a");

            for (org.jsoup.nodes.Element element1 : link) {
                builder.append("⚡️"+element1.ownText()+"\n");
            }
            builder.append("-------------------------------------------------------------"+"\n");
        }

        Gson gson = new Gson();
        File file = new File("src/main/resources/users.json");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<List<User>> users = gson.fromJson(reader, new com.google.gson.reflect.TypeToken<List<List<User>>>() {
        }.getType());
        List<User> userList = users.get(0);

        for (User user : userList) {
            new TelegramBot().execute(SendMessage.builder()
                    .chatId(user.getChatId())
                    .text(builder.toString())
                    .build());

        }
    }
}