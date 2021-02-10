
import java.util.Arrays;

public class Main {

public int[] afterFour(int[] inArr) {
    for (int i = inArr.length-1; i >= 0 ; i--) {
        if (inArr[i] == 4) return Arrays.copyOfRange(inArr, i+1, inArr.length);
    }
    throw new RuntimeException("There is no one 4 in array");
}

public boolean onesAndFours(int[] inArr) {
    boolean isOne = false, isFour = false;

    for (int i = 0; i < inArr.length; i++) {
        switch (inArr[i]) {
            case 1:
                isOne = true;
                break;
            case 4:
                isFour = true;
                break;
            default:
                return false;
        }
    }

    return isOne && isFour;
}

}
