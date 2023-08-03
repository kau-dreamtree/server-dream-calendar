package org.standard.dreamcalendar.domain.mail;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AuthCodeGenerator {

    private boolean lowerCheck;
    private int size;

    public String getAuthCode(int size, boolean lowerCheck) {
        this.lowerCheck = lowerCheck;
        this.size = size;
        return init();
    }

    private String init() {

        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();

        int num = 0;

        do {
            num = random.nextInt(75) + 48;
            if ((num >= 48 && num <= 57) || (num >= 65 && num <= 90) || (num >= 97) && (num <= 122)) {
                stringBuffer.append((char) num);
            }
        } while (stringBuffer.length() < size);

        if (lowerCheck) {
            return stringBuffer.toString().toLowerCase();
        }

        return stringBuffer.toString();
    }
}
