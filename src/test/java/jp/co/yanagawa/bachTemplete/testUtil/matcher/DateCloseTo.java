package jp.co.yanagawa.bachTemplete.testUtil.matcher;

import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class DateCloseTo extends TypeSafeMatcher<Date> {

    private final long delta;
    private final Date value;

    public DateCloseTo(Date value, long error) {
        this.delta = error;
        this.value = value;
    }

    @Override
    public boolean matchesSafely(Date item) {
        return actualDelta(item) <= 0;
    }

    @Override
    public void describeMismatchSafely(Date item, Description mismatchDescription) {
        mismatchDescription.appendValue(item)
                .appendText(" differed by ")
                .appendValue(actualDelta(item));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a Date value within ")
                .appendValue(delta)
                .appendText(" of ")
                .appendValue(value);
    }

    private long actualDelta(Date item) {
        return Math.abs(item.getTime() - value.getTime()) - delta * 1000;
    }

    /**
     * Creates a matcher of {@link java.util.Date}selectRuleId that matches when an examined Date is equal
     * to the specified <code>operand</code>, within a range of +/- <code>error</code>. The comparison for equality
     * is done by Dates {@link java.util.Date#compareTo(java.util.Date)} method.
     * <p/>
     * For example:
     * <pre>
     * SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
     * assertThat(sdf.parse("2016/05/30 11:12:13"), is(closeTo(sdf.parse("2016/05/30 11:12:15"), 2)))
     * </pre>
     * 
     * @param operand
     *     the expected value of matching Dates
     * @param error
     *     the delta (+/-) within which matches will be allowed
     */
    @Factory
    public static Matcher<Date> closeTo(Date operand, long error) {
        return new DateCloseTo(operand, error);
    }


}
