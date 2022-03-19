package ForUser;

import java.util.HashMap;
import java.util.Map;

public interface CurrencyModeService {
    static final Map<String, Currencies> originalCurrency = new HashMap<>();
    static final Map<String, Currencies> targetCurrency = new HashMap<>();
    public static Currencies getOriginalCurrency(String  chatId){return  originalCurrency.getOrDefault(chatId,Currencies.USD);}
    public static Currencies getTargetCurrency(String chatId){return  targetCurrency.getOrDefault(chatId,Currencies.USD);}
    public static void setOriginalCurrency(String chatId, Currencies constant){
        originalCurrency.put(chatId,constant);
    }
    public static void setTargetCurrency(String chatId,Currencies constant){
        targetCurrency.put(chatId,constant);
    }

}
