package jp.co.yanagawa.bachTemplete.testUtil.mock;

import java.time.LocalTime;

import mockit.Mock;
import mockit.MockUp;

public class LocalTimeMock extends MockUp<LocalTime> {

    @Mock
    public static LocalTime now() {
      return LocalTime.of(13, 14, 15);
    }
}
