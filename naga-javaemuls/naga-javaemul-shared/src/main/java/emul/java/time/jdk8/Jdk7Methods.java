/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package emul.java.time.jdk8;

/**
 * A set of utility methods that provide backward compatibility with java < 1.7.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class Jdk7Methods {

  /**
   * Construction forbidden.
   */
  private Jdk7Methods() {

  }

  public static <T> T Objects_requireNonNull(T obj, String message) {

    if (obj == null) {
      throw new NullPointerException(message);
    }
    return obj;
  }

  public static boolean Objects_equals(Object a, Object b) {

    return (a == b) || (a != null && a.equals(b));
  }

  public static int Integer_compare(int x, int y) {

    return (x < y) ? -1 : ((x == y) ? 0 : 1);
  }

  public static int Long_compare(long x, long y) {

    return (x < y) ? -1 : ((x == y) ? 0 : 1);
  }

}
