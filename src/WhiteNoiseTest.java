import java.io.*;
import java.text.*;

public class WhiteNoiseTest {

    public static void main(String[] args) throws Exception {
        int n = 8; // N = (n-1)

        double[] y, r;

        y = new double[n];
        r = new double[n];

        // Open file for reading ratings
        BufferedReader reader = new BufferedReader(new FileReader("KalmanOutResiduals"));

        for (int i = 0; i < n; i++) {
            y[i] = Double.parseDouble(reader.readLine());
        }

        reader.close();

        // calculate r(i)
        for (int i = 0; i < n; i++) {
            r[i] = 0.0;
            for (int t = 0; t < (n - i); t++)
                r[i] += y[t] * y[t + i];
            r[i] = r[i] / (n - 1);
        }

        int countWhite = 0, countNonWhite = 0;

        for (int k = 1200; k < n; k++) {
            double rhs = (k + 1.65 * Math.sqrt(2.0 * k)) * r[0] * r[0] / (n - 1);

            double lhs = 0.0;

            for (int i = 1; i <= k; i++)
                lhs += r[i] * r[i];

            if (lhs <= rhs)
                countWhite++;
            else
                countNonWhite++;
        }

        System.out.println("Count of white: " + countWhite);
        System.out.println("Count of non white: " + countNonWhite);
        System.out.println(((double) countWhite) / (countWhite + countNonWhite));
    }
}
