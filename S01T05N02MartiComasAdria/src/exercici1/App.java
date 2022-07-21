package exercici1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class App {
	public static void main(String[] args) {
		ArrayList<String> output = new ArrayList<String>();

		Properties p = new Properties();
		InputStream is = null;

		try {
			is = new FileInputStream("config.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			p.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String path = p.getProperty("dirInput");
		String fileOutput = p.getProperty("fileOutput");

		arbreDirectorisToTxt(path, output, fileOutput);

	}

	public static void arbreDirectorisToTxt(String path, ArrayList<String> output, String fileOutput) {
		File file = new File(path);

		mostrarArxiusTxt(path, output);
		String parentFolder = file.getParent();

		if (parentFolder.equals("/")) {
			output.add("\nROOT");

			try {
				BufferedWriter sortida = new BufferedWriter(new FileWriter(fileOutput));

				for (String o : output) {
					sortida.write(o);
				}
				sortida.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {

			arbreDirectorisToTxt(parentFolder, output, fileOutput);

		}

	}

	public static void mostrarArxiusTxt(String path, ArrayList<String> output) {
		File file = new File(path);
		File folder[] = file.listFiles();
		FileTime fileTime = null;
		int nivell = path.length() - path.replace("/", "").length();
		String espaiEsquerra = new String(new char[nivell * 2]).replace('\0', ' ');
		Path path2 = Paths.get(path);
		String print = "";

		Arrays.sort(folder);
		print = print + "\n" + espaiEsquerra + file.getName().toUpperCase() + "\n";

		for (File f : folder) {
			try {
				fileTime = Files.getLastModifiedTime(path2);

			} catch (IOException e) {
				System.err.println("Cannot get the last modified time - " + e);
			}
			if (f.isFile()) {

				print = print + espaiEsquerra + "File: " + f.getName() + " - " + fileTime + "\n";
			} else if (f.isDirectory()) {
				print = print + espaiEsquerra + "Directory: " + f.getName() + " - " + fileTime + "\n";
			} else {
				print = print + espaiEsquerra + "Not Know:" + f.getName() + " - " + fileTime + "\n";
			}
		}
		output.add(print);

	}

	public static void printFileTime(FileTime fileTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  -  hh:mm:ss");
		System.out.println(dateFormat.format(fileTime.toMillis()));
	}

}
