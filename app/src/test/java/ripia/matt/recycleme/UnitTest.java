package ripia.matt.recycleme;

import org.junit.Assert;
import org.junit.Test;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {

    @Test
    // This method ensures that the location received from 'GetCurrentLocation' is formatted
    // correctly with no null values or repeating variables, as in some cases 'NULL' is
    // returned or the postal code is the same as the street number which is incorrect.
    public void checkLocationDetails(){

        AccountFragment account = new AccountFragment();

        Assert.assertEquals("Checking location details are correct", true, account.checkLocationDetails("12", "Bing Street", "0600"));
        Assert.assertEquals("Checking location details are correct", true, account.checkLocationDetails("1065", "Tts not real Street", "2100"));
        Assert.assertEquals("Checking location details are correct", true, account.checkLocationDetails("122", "Actual Ave", "0110"));
        Assert.assertEquals("Checking location details are correct", true, account.checkLocationDetails("1", "Fake Street", "0600"));

        Assert.assertEquals("Checking post code is a number", false, account.checkLocationDetails("1024", "Megabyte Lane", "55555"));
        Assert.assertEquals("Checking post code is a number", false, account.checkLocationDetails("1024", "Megabyte Lane", "not a number"));
        Assert.assertEquals("Checking duplicated variables", false, account.checkLocationDetails("12", "1010", "1010"));
        Assert.assertEquals("Checking duplicated variables", false, account.checkLocationDetails("1010", "1010", "2001"));
        Assert.assertEquals("Checking duplicated variables", false, account.checkLocationDetails("1010", "Some Street", "1010"));
        Assert.assertEquals("Checking empty strings", false, account.checkLocationDetails("", "", ""));
        Assert.assertEquals("Checking spaces with no other text", false, account.checkLocationDetails(" ", " ", " "));
        Assert.assertEquals("Checking no null variables", false, account.checkLocationDetails("null", "Hello Ave", "0600"));
        Assert.assertEquals("Checking no null variables", false, account.checkLocationDetails("1065", "null", "9911"));
        Assert.assertEquals("Checking no null variables", false, account.checkLocationDetails("122", "Account Road", "null"));
        Assert.assertEquals("Checking no null variables", false, account.checkLocationDetails("null", "null", "null"));
        Assert.assertEquals("Checking no null values", false, account.checkLocationDetails(null, null, null));
        Assert.assertEquals("Checking no null values", false, account.checkLocationDetails(null, "A Street", "2115"));
        Assert.assertEquals("Checking no null values", false, account.checkLocationDetails("18", null, "200"));
        Assert.assertEquals("Checking no null values", false, account.checkLocationDetails("18", "A Street", null));
    }

    @Test
    // this unit test tests the boundaries of a valid number.
    // We want the values 1 - 8 to return true, everything else must return false.
    public void checkItemDetails() {

        ItemFormFragment fragment = new ItemFormFragment();

        // These tests should return true
        Assert.assertEquals("Checking input is a number", true, fragment.checkItemDetails("1"));
        Assert.assertEquals("Checking input is a number", true, fragment.checkItemDetails("2"));
        Assert.assertEquals("Checking input is a number", true, fragment.checkItemDetails("3"));
        Assert.assertEquals("Checking input is a number", true, fragment.checkItemDetails("4"));
        Assert.assertEquals("Checking input is a number", true, fragment.checkItemDetails("5"));
        Assert.assertEquals("Checking input is a number", true, fragment.checkItemDetails("6"));
        Assert.assertEquals("Checking input is a number", true, fragment.checkItemDetails("7"));
        Assert.assertEquals("Checking input is a number", true, fragment.checkItemDetails("8"));

        // these tests should return false
        Assert.assertEquals("Checking input is a number", false, fragment.checkItemDetails("0"));
        Assert.assertEquals("Checking input is a number", false, fragment.checkItemDetails("9"));
        Assert.assertEquals("Checking input is a number", false, fragment.checkItemDetails(""));
        Assert.assertEquals("Checking input is a number", false, fragment.checkItemDetails(" "));
        Assert.assertEquals("Checking input is a number", false, fragment.checkItemDetails("abc"));
        Assert.assertEquals("Checking input is a number", false, fragment.checkItemDetails("123"));
        Assert.assertEquals("Checking input is a number", false, fragment.checkItemDetails("021"));
        Assert.assertEquals("Checking input is a number", false, fragment.checkItemDetails("Very large string@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
        Assert.assertEquals("Checking input is a number", false, fragment.checkItemDetails("Special Character: ¢"));
    }
}