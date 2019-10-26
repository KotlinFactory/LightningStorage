package de.leonhard.storage.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtils {


	public static String getExtension(String path) {
		return path.lastIndexOf(".") > 0 ? path.substring(path.lastIndexOf(".") + 1) : "";
	}

	public static String getExtension(File file) {
		return getExtension(file.getName());
	}


	public static String replaceExtensions(final String fileName) {
		if (!fileName.contains(".")) {
			return fileName;
		}
		return fileName.replace(getExtension(fileName), "");
	}

	public static boolean hasChanged(final File file, final long timeStamp) {
		return timeStamp < file.lastModified();
	}

	public static File getAndMake(final String name, final String path) {
		return getAndMake(new File(path, name));
	}

	public static InputStream createNewInputStream(final File file) {
		try {
			return Files.newInputStream(file.toPath());
		} catch (IOException e) {
			System.err.println("Exception while creating InputStream from '" + file.getName() + "'");
			System.err.println("At: '" + file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException("InputStream would be null");
		}
	}

	public static void writeToFile(final File file, final InputStream inputStream) {
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			if (!file.exists()) {
				Files.copy(inputStream, file.toPath());
			} else {
				final byte[] data = new byte[8192];
				int count;
				while ((count = inputStream.read(data, 0, 8192)) != -1) {
					outputStream.write(data, 0, count);
				}
			}
		} catch (IOException e) {
			System.err.println("Exception while copying to + '" + file.getAbsolutePath() + "'");
			e.printStackTrace();
		}
	}

	public static File getAndMake(final File file) {
		try {
			if (file.getParentFile() != null && !file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

		} catch (IOException ioException) {
			System.err.println("Error while creating file '" + file.getName() + "'.");
			System.err.println("Path: '" + file.getAbsolutePath() + "'");
			ioException.printStackTrace();
			throw new IllegalStateException();
		}
		return file;
	}

	public static List<String> readAllLines(final File file) throws IOException {
		final byte[] fileBytes = Files.readAllBytes(file.toPath());
		final String asString = new String(fileBytes);
		return new ArrayList<>(Arrays.asList(asString.split("\n")));
	}
}