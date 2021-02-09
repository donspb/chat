import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class MainTest {

    Main main = new Main();

    @ParameterizedTest
    @MethodSource("testArray")

    public void testAfterFour(int[] data, int[] result) {
        Assertions.assertArrayEquals(result, main.afterFour(data));
    }

    public static Stream<Arguments> testArray() {
        List<Arguments> arraysTestSet = new ArrayList<> ();
        arraysTestSet.add(Arguments.arguments(new int[]{3, 3, 3, 3, 4, 1, 2, 3}, new int[]{1, 2, 3}));
        arraysTestSet.add(Arguments.arguments(new int[]{3, 3, 3, 1, 2, 3, 5, 4}, new int[]{}));
        arraysTestSet.add(Arguments.arguments(new int[]{3, 4, 5, 8, 4, 1, 2, 6}, new int[]{1, 2, 6}));

        return arraysTestSet.stream();
    }

    @Test
    public void afterFourExceptionTest() {
        int[] inpArray = {3, 3, 3, 3, 1, 2, 3};
        Assertions.assertThrows(RuntimeException.class , () -> main.afterFour(inpArray));
    }



    @ParameterizedTest
    @MethodSource("testArrayTwo")

    public void testOnesAndFours(int[] data, boolean result) {
        Assertions.assertEquals(result, main.onesAndFours(data));
    }

    public static Stream<Arguments> testArrayTwo() {
        List<Arguments> arraysTestSet = new ArrayList<> ();
        arraysTestSet.add(Arguments.arguments(new int[]{1, 1, 1, 1, 4, 4, 4}, true));
        arraysTestSet.add(Arguments.arguments(new int[]{1, 1, 4, 4, 1, 0}, false));
        arraysTestSet.add(Arguments.arguments(new int[]{3, 5, 4, 1, 6}, false));
        arraysTestSet.add(Arguments.arguments(new int[]{1, 1, 1, 1, 1}, false));
        arraysTestSet.add(Arguments.arguments(new int[]{4, 4}, false));

        return arraysTestSet.stream();
    }
}