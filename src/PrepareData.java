
import java.io.*;
import java.util.*;

public class PrepareData {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// Use BufferedReader rather than RandomAccessFile; it's much faster
		BufferedReader helpin = new BufferedReader(new FileReader("helpful"));
		PrintWriter helpout = new PrintWriter(new BufferedWriter(new FileWriter("helpfulout")));

		BufferedReader ratingin = new BufferedReader(new FileReader("ratingin"));
		PrintWriter ratingout = new PrintWriter(new BufferedWriter(new FileWriter("ratingout")));

		BufferedReader reviewin = new BufferedReader(new FileReader("reviewin"));
		PrintWriter reviewout = new PrintWriter(new BufferedWriter(new FileWriter("reviewout")));

		String line = reviewin.readLine();
		while (line != null) {
			reviewout.println(new StringTokenizer(line).countTokens());
			line = reviewin.readLine();
		}
		reviewout.close();

		line = helpin.readLine();
		while (line != null) {
			StringTokenizer scan = new StringTokenizer(line);
			if (scan.countTokens() == 0) {
				helpout.println("0");
			} else {
				String temp = scan.nextToken();
				if (temp.equals("One"))
					helpout.println(1);
				else
					helpout.println(temp);
			}
			line = helpin.readLine();
		}
		helpout.close();

		line = ratingin.readLine();
		while (line != null) {
			StringTokenizer scan = new StringTokenizer(line);
			if (scan.countTokens() == 0) {
				ratingout.println(" ");
			} else {
				ratingout.println(scan.nextToken());
			}
			line = ratingin.readLine();
		}
		ratingout.close();

	}
}