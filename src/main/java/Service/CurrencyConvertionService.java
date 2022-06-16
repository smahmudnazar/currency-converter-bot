package Service;

import Api.Api;
import Model.Enums.Currencies;

public interface CurrencyConvertionService {
    static double getConversionRatio(Currencies original, Currencies target, double value){

        Double natija;
        if(original.equals(Currencies.UZS)){
            double targetRate = Double.parseDouble(Api.getCurrency(String.valueOf(target)).getRate());
            natija=1/targetRate;
        }else if (target.equals(Currencies.UZS)){
            double originalRate = Double.parseDouble(Api.getCurrency(String.valueOf(original)).getRate());
            natija=originalRate*value;
        }else {
            double originalRate = Double.parseDouble(Api.getCurrency(String.valueOf(original)).getRate());
            double targetRate = Double.parseDouble(Api.getCurrency(String.valueOf(target)).getRate());
            natija= originalRate / targetRate;}
        return natija;
    }
}
