package sample;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestSamp {
    public static void main(String[] args) {
        System.out.println(new SimpleDateFormat("YYYYMMddhhmmss", Locale.getDefault()).format(new Date()));
    }
}
