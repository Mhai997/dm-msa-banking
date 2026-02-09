package com.pichincha.dm.msa.banking.util;

public final class EcuadorIdValidator {

    private EcuadorIdValidator() {}

    public static boolean isValid(String id) {
        if (id == null || !id.matches("\\d{10}")) {
            return false;
        }

        int province = Integer.parseInt(id.substring(0, 2));
        if (province < 1 || province > 24) {
            return false;
        }

        int thirdDigit = Character.getNumericValue(id.charAt(2));
        if (thirdDigit > 5) {
            return false;
        }

        int[] coefficients = {2,1,2,1,2,1,2,1,2};
        int sum = 0;

        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(id.charAt(i));
            int result = digit * coefficients[i];
            sum += (result >= 10) ? result - 9 : result;
        }

        int verifier = Character.getNumericValue(id.charAt(9));
        int computed = (10 - (sum % 10)) % 10;

        return verifier == computed;
    }
}
