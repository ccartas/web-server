package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Cosmin on 9/4/2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ContentTypeResponseTest.class,
        ResponseStatusTest.class,
        HttpMethodTest.class
})
public class AllTests {

}
