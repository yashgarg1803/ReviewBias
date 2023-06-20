import java.io.*;
import java.text.*;

public class Kalman {
    static PrintWriter writer, writer2;

    public static void main(String[] args) throws Exception {
        int n = 2;

        double[] x, C, K;
        double[][] A, P, Sigma1, temp;
        double g, residual, sigma2 = 0.1;

        x = new double[n];
        C = new double[n];
        K = new double[n];

        A = new double[n][n];
        P = new double[n][n];
        Sigma1 = new double[n][n];
        temp = new double[n][n];

        // initialize A = I
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                if (i == j)
                    A[i][j] = 1.0;
                else
                    A[i][j] = 0.0;
            }
        }
        // A[1][1] = 1.01;
        // A[1][0] = 0.01;

        // initialize P
        for (int i = 0; i < P.length; i++)
            for (int j = 0; j < P[0].length; j++)
                // if ( i == j )
                P[i][j] = 1.0;
        /*
         * else
         * P[i][j] = 0.0;
         */

        // initialize C
        for (int i = 0; i < C.length; i++)
            C[i] = 1.0;

        // initialize Sigma1
        for (int i = 0; i < Sigma1.length; i++)
            for (int j = 0; j < Sigma1[0].length; j++)
                /*
                 * if ( i == j )
                 * Sigma1[i][j] = 0.01;
                 * else
                 */
                Sigma1[i][j] = 0.0;
        Sigma1[Sigma1[0].length - 1][Sigma1[0].length - 1] = 0.01;

        // Open file for reading ratings
        BufferedReader reader = new BufferedReader(new FileReader("ratingout"));

        // Open file for writing
        // writer = new PrintWriter( new FileWriter( "Altima08ResidualResults.txt" ),
        // true );
        writer = new PrintWriter(new FileWriter("KalmanOut"), true);
        writer2 = new PrintWriter(new FileWriter("KalmanOutResiduals"), true);

        String line = reader.readLine();

        // initialize x0
        x[0] = Double.parseDouble(line);
        x[1] = 0.0;

        // read the next rating and update the variables
        line = reader.readLine();

        while (line != null && !line.equals(" ") && !line.equals("")) {
            g = Double.parseDouble(line);
            x = product(A, x);
            P = sum(product(product(A, P), transpose(A)), Sigma1);
            K = product(product(P, C), 1 / (sigma2 + product(C, product(P, C))));
            residual = (g - product(C, x));
            x = sum(x, product(K, residual));

            product(K, C, temp);
            P = product(diff(A, temp), P);

            printArray(x);
            // writer.println( "K: " );
            printArray(K);
            // writer.println( "x: " );
            // writer.println( "P: " );
            printArray(P);
            writer.println();
            // writer.println( x[0] + "\t" + x[1] + "\t" + residual );
            writer2.println(residual);

            line = reader.readLine();
        }

    } // main

    public static double[][] transpose(double[][] a) {
        double[][] temp = new double[a[0].length][a.length];

        for (int row = 0; row < a.length; row++)
            for (int column = 0; column < a[0].length; column++)
                temp[row][column] = a[column][row];

        return temp;
    }

    // (1 x N) * (N x 1) = c
    public static double product(double[] a1, double[] a2) {

        double sum = 0.0;

        for (int column = 0; column < a2.length; column++)
            sum += a1[column] * a2[column];

        return sum;
    }

    // (M x N) * (N x P) = (M x P)
    public static double[][] product(double[][] a1, double[][] a2) {
        double[][] prod = new double[a1.length][a2[0].length];

        for (int row = 0; row < a1.length; row++) {
            for (int column = 0; column < a2[0].length; column++) {
                double sum = 0.0;

                for (int i = 0; i < a2.length; i++)
                    sum += a1[row][i] * a2[i][column];

                prod[row][column] = sum;
            }
        }

        return prod;

    }

    // (M x N) * (N x 1) = (M x 1)
    public static double[] product(double[][] a1, double[] a2) {
        double[] prod = new double[a1.length];

        for (int row = 0; row < a1.length; row++) {
            double sum = 0.0;

            for (int column = 0; column < a2.length; column++)
                sum += a1[row][column] * a2[column];

            prod[row] = sum;
        }

        return prod;

    }

    // (N x 1) * (1 x N) = (N x N)
    public static void product(double[] a1, double[] a2, double[][] prod) {
        for (int row = 0; row < a1.length; row++)
            for (int column = 0; column < a2.length; column++)
                prod[row][column] = a1[row] * a2[column];

    }

    // (N x 1) * a = (N x 1)
    public static double[] product(double[] a1, double a2) {
        double[] prod = new double[a1.length];

        for (int column = 0; column < a1.length; column++)
            prod[column] = a1[column] * a2;

        return prod;
    }

    public static double[][] sum(double[][] a1, double[][] a2) {
        double[][] sum = new double[a1.length][a1[0].length];

        for (int row = 0; row < a1.length; row++)
            for (int column = 0; column < a1[0].length; column++)
                sum[row][column] = a1[row][column] + a2[row][column];

        return sum;
    }

    public static double[] sum(double[] a1, double[] a2) {
        double[] sum = new double[a1.length];

        for (int row = 0; row < a1.length; row++)
            sum[row] = a1[row] + a2[row];

        return sum;
    }

    public static double[][] diff(double[][] a1, double[][] a2) {
        double[][] diff = new double[a1.length][a1[0].length];

        for (int row = 0; row < a1.length; row++)
            for (int column = 0; column < a1[0].length; column++)
                diff[row][column] = a1[row][column] - a2[row][column];

        return diff;
    }

    public static void printArray(double[][] a) throws Exception {
        DecimalFormat format = new DecimalFormat("0.00");

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++)
                writer.print(format.format(a[i][j]) + "\t");
            // writer.println();
        }
    }

    public static void printArray(double[] a) throws Exception {
        DecimalFormat format = new DecimalFormat("0.00");

        for (int i = 0; i < a.length; i++)
            writer.print(format.format(a[i]) + "\t");

        // writer.println();

    }
}
