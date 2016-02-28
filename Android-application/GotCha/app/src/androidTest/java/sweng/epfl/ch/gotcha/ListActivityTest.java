package sweng.epfl.ch.gotcha;


import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import java.util.List;


/**
 * Created by emmawestman on 13/11/15.
 */
public class ListActivityTest extends ActivityInstrumentationTestCase2<ListActivity> {

    private ListActivity mActivity;

    public ListActivityTest() {
        super(ListActivity.class);
        }


    protected void setUp() {
        mActivity = getActivity();
    }

    public void testListIsNotNull() {
        assertNotNull(mActivity.getItemList());
    }

    @UiThreadTest
    public void testCorrectCatagoryBooks() {

        boolean totalResult = true;

        mActivity.setCategory(Item.Category.BOOK);

        mActivity.onRefresh();
        Item.Category currentCategory = mActivity.getCurrentCategory();

        assertEquals(Item.Category.BOOK, currentCategory);

        List<Item> items = mActivity.getItemList();
        for (Item item : items) {
            totalResult = item.getCategory().equals(currentCategory) && totalResult;
        }
        assertTrue(totalResult);
    }

    @UiThreadTest
    public void testCorrectCatagoryClothes() {

        boolean totalResult = true;

        mActivity.setCategory(Item.Category.CLOTHING);

        mActivity.onRefresh();
        Item.Category currentCategory = mActivity.getCurrentCategory();

        assertEquals(Item.Category.CLOTHING, currentCategory);

        List<Item> items = mActivity.getItemList();
        for (Item item : items) {
            totalResult = item.getCategory().equals(currentCategory) && totalResult;
        }
        assertTrue(totalResult);
    }

    @UiThreadTest
    public void testCorrectCatagoryHousehold() {

        boolean totalResult = true;

        mActivity.setCategory(Item.Category.HOUSE);

        mActivity.onRefresh();
        Item.Category currentCategory = mActivity.getCurrentCategory();

        assertEquals(Item.Category.HOUSE, currentCategory);

        List<Item> items = mActivity.getItemList();
        for (Item item : items) {
            totalResult = item.getCategory().equals(currentCategory) && totalResult;
        }
        assertTrue(totalResult);
    }


    @UiThreadTest
    public void testCorrectCatagoryElectronics() {

        boolean totalResult = true;

        mActivity.setCategory(Item.Category.ELECTRONIC);

        mActivity.onRefresh();
        Item.Category currentCategory = mActivity.getCurrentCategory();

        assertEquals(Item.Category.ELECTRONIC, currentCategory);

        List<Item> items = mActivity.getItemList();
        for (Item item : items) {
            totalResult = item.getCategory().equals(currentCategory) && totalResult;
        }
        assertTrue(totalResult);
    }

    @UiThreadTest
    public void testCorrectCatagorySport() {

        boolean totalResult = true;

        mActivity.setCategory(Item.Category.SPORT);

        mActivity.onRefresh();
        Item.Category currentCategory = mActivity.getCurrentCategory();

        assertEquals(Item.Category.SPORT, currentCategory);

        List<Item> items = mActivity.getItemList();
        for (Item item : items) {
            totalResult = item.getCategory().equals(currentCategory) && totalResult;
        }
        assertTrue(totalResult);
    }

    @UiThreadTest
    public void testCorrectCatagoryOther() {

        boolean totalResult = true;

        mActivity.setCategory(Item.Category.OTHER);

        mActivity.onRefresh();
        Item.Category currentCategory = mActivity.getCurrentCategory();

        assertEquals(Item.Category.OTHER, currentCategory);

        List<Item> items = mActivity.getItemList();
        for (Item item : items) {
            totalResult = item.getCategory().equals(currentCategory) && totalResult;
        }
        assertTrue(totalResult);
    }

}
