package org.komodo.utils.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.komodo.utils.i18n.UtilsI18nTest;

/**
 * All Util Tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( { TestKLog.class,
                       UtilsI18nTest.class } )

public class AllTests {
    // nothing to do
}
