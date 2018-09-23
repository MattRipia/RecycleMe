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