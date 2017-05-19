package myapp.api;

import myapp.Validations;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Victor Vlad Corcodel on 19/05/2017.
 */
public class ValidationsTest {

    @Test
    public void testValidateOID() {
        Assert.assertTrue(Validations.isValidOID("2.3.43.4.45.4.3.2"));
        Assert.assertFalse(Validations.isValidOID("3.3.3.3.2,2"));
        Assert.assertFalse(Validations.isValidOID("3.3.3.3..3"));
        Assert.assertFalse(Validations.isValidOID("3.3.3.3.d.3"));
        Assert.assertFalse(Validations.isValidOID("3.3.3.3.3.3 "));
        Assert.assertFalse(Validations.isValidOID("3.3.3.3. .3"));
    }
}
