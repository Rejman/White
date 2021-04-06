package Utils;

import java.math.BigDecimal;

public class UnitConverter {
    public static String toPrice(BigDecimal value){
        String number = String.format("%.2f", value);
        number = number.replaceAll(",",".");
        char[] totalArray = number.toCharArray();
        char dot = totalArray[totalArray.length-2];
        if (dot == '.') number+="0";
        return number;
    }
}
