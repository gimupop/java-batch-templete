package jp.co.yanagawa.bachTemplete.testUtil.mock;

import java.time.LocalDate;

import mockit.Mock;
import mockit.MockUp;

public class LocalDateMock extends MockUp<LocalDate> {

    @Mock
    public static LocalDate now() {
      return LocalDate.of(2016, 5, 26);
    }
}
