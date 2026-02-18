package com.ead.authuser.helpers;

import java.util.regex.Pattern;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public final class JsonMatchers {

  private static final Pattern UUID_PATTERN =
      Pattern.compile(
          "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

  // pattern: dd-MM-yyyy HH:mm:ss
  private static final Pattern BR_DATETIME_PATTERN =
      Pattern.compile("^\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2}$");

  public static Matcher<String> isUuid() {
    return new TypeSafeMatcher<>() {
      @Override
      protected boolean matchesSafely(String item) {
        return item != null && UUID_PATTERN.matcher(item).matches();
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("a valid UUID (xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx)");
      }
    };
  }

  public static Matcher<String> isBrDateTime() {
    return new TypeSafeMatcher<>() {
      @Override
      protected boolean matchesSafely(String item) {
        return item != null && BR_DATETIME_PATTERN.matcher(item).matches();
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("a date-time in format dd-MM-yyyy HH:mm:ss");
      }
    };
  }
}
