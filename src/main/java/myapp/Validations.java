package myapp;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Victor Vlad Corcodel on 19/05/2017.
 */
public class Validations {

    public static boolean isValidOID(String oid) {
        if (oid == null || oid.trim().equals("")) return false;
        List<String> oidParts = Arrays.asList(oid.split("\\."));
        List<String> invalidOIDParts = oidParts.stream().filter(e -> !isInteger(e)).collect(Collectors.toList());
        return invalidOIDParts.size() == 0;
    }

    public static boolean isInteger(String str) {
        if (str == null || str.trim().equals("")) return false;
        try {
            Integer value = Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }
}
