package com.billetera_virtual.account.infra.data.external;

import com.billetera_virtual.account.domain.port.CvuGeneratorPort;
import org.springframework.stereotype.Component;

@Component
public class CvuGeneratorAdapter implements CvuGeneratorPort {

    private static final String PSP_CODE = "000";
    private static final String BRANCH_CODE = "0001";

    @Override
    public String generate(Long accountId) {
        // PSP + Control + Nro de Sucursal
        String firstPart = PSP_CODE + "0" + BRANCH_CODE;

        // ID de cuenta con ceros a la izquierda (13 dígitos)
        String accountNumber = String.format("%013d", accountId);

        int controlDigit = calculateControlDigit(accountNumber);

        return firstPart + accountNumber + controlDigit;
    }

    // Metodo que calcula un digito de control
    private int calculateControlDigit(String number) {
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            sum += Character.getNumericValue(number.charAt(i));
        }
        return sum % 10;
    }

}
