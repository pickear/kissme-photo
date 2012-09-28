package com.kissme.photo.infrastructure.util;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class FileUtils {

	public static final String UNIX_SEPERATOR = "/";
	public static final String WINDOW_SEPERATOR = "\\";

	/**
	 * 
	 * @param directory
	 * @return
	 */
	public static File[] list(String directory) {
		return list(new File(directory));
	}

	/**
	 * 
	 * @param directory
	 * @param filter
	 * @return
	 */
	public static File[] list(String directory, FileFilter filter) {
		return list(new File(directory), filter);
	}

	/**
	 * 
	 * @param directory
	 * @return
	 */
	public static File[] list(File directory) {
		return list(directory, new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return true;
			}
		});
	}

	/**
	 * 
	 * @param directory
	 * @param filter
	 * @return
	 */
	public static File[] list(File directory, FileFilter filter) {
		if (directory.isFile()) {
			return new File[] {};
		}

		List<File> files = new LinkedList<File>();
		list(files, directory, filter);

		return files.toArray(new File[files.size()]);
	}

	private static void list(List<File> files, File directory, FileFilter fileFilter) {
		if (directory.isFile()) {
			files.add(directory);
			return;
		}

		File[] innerFiles = directory.listFiles(fileFilter);
		if (null == innerFiles || innerFiles.length == 0) {
			return;
		}

		for (File inner : innerFiles) {
			list(files, inner, fileFilter);
		}
	}

	/**
	 * 
	 * @return
	 */
	public static String home() {
		return System.getProperty("user.path");
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static String canonical(File file) {
		try {
			return file.getCanonicalPath();
		} catch (Exception e) {
			throw ExceptionUtils.uncheck(e);
		}
	}

	/**
	 * 
	 * @param base
	 * @param paths
	 * @return
	 */
	public static String join(String base, String... paths) {
		StringBuilder buf = new StringBuilder().append(base);
		for (String path : paths) {
			buf.append(File.separator).append(path);
		}

		return asPlatform(buf.toString());
	}

	/**
	 * 
	 * @param filename
	 * @return
	 */
	public static String[] split(String filename) {
		if (StringUtils.isBlank(filename)) {
			return new String[] { "", "" };
		}

		filename = asUnix(filename);
		int index = filename.lastIndexOf(UNIX_SEPERATOR);
		if (index == -1) {
			return new String[] { filename, "" };
		}

		return new String[] { filename.substring(0, index), filename.substring(index) };
	}

	/**
	 * 
	 * @param filename
	 * @return
	 */
	public static String major(String filename) {

		String[] split = split(filename);
		String major = StringUtils.isBlank(split[1]) ? split[0] : split[1];
		int dotIndex = major.lastIndexOf(".");
		if (dotIndex == -1) {
			return major;
		}

		return major.substring(0, dotIndex);
	}

	/**
	 * 
	 * @param filename
	 * @return
	 */
	public static String suffix(String filename) {
		if (StringUtils.isBlank(filename)) {
			return "";
		}

		int dotIndex = filename.lastIndexOf(".");
		if (dotIndex == -1) {
			return "";
		}

		return filename.substring(dotIndex + 1);
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static String asPlatform(String path) {
		if (StringUtils.isBlank(path)) {
			return path;
		}
		return path.replaceAll("[/|\\\\]+", Matcher.quoteReplacement(File.separator));
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static String asUnix(String path) {
		if (StringUtils.isBlank(path)) {
			return path;
		}
		return path.replaceAll("[/|\\\\]+", UNIX_SEPERATOR);
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static String asWindow(String path) {
		if (StringUtils.isBlank(path)) {
			return path;
		}
		return path.replaceAll("[/|\\\\]+", Matcher.quoteReplacement(WINDOW_SEPERATOR));
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static String asPackage(String path) {
		if (StringUtils.isBlank(path)) {
			return path;
		}
		return path.replaceAll("[/|\\\\]+", "\\.");
	}

	/**
	 * 
	 * @param packageName
	 * @return
	 */
	public static String asPath(String packageName) {
		return packageName.replaceAll("\\.", "/");
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static FileType guessType(byte[] bytes) {
		Preconditions.checkArgument(bytes.length >= 32);
		String hexHeader = fileHexHeader(bytes);
		for (FileType type : FileType.values()) {

			if (hexHeader.startsWith(type.getHexHeader())) {
				return type;
			}
		}
		return null;
	}

	private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static String fileHexHeader(byte[] bytes) {
		int length = 32;
		char[] out = new char[length << 1];
		for (int i = 0, j = 0; i < length; i++) {
			out[j++] = DIGITS_UPPER[(0xF0 & bytes[i]) >>> 4];
			out[j++] = DIGITS_UPPER[0x0F & bytes[i]];
		}

		return new String(out);
	}

	public static enum FileType {
		JPG("jpg", "FFD8FF"), ICO("ico", "00000100"), BMP("bmp", "424D"), GIF("gif", "47494638"), PNG("png", "89504E47"),
		ZIP("zip", "504B0304"), RAR("rar", "52617221"),
		PDF("pdf", "25504446"), DOC_OR_XLS_OR_PPT("doc;xls;ppt", "D0CF11E0"), DOCX("docx", "504B0304");

		private final String name;
		private final String hexHeader;

		private FileType(String name, String hexHeader) {
			this.name = name;
			this.hexHeader = hexHeader;
		}

		public String getName() {
			return name;
		}

		public String getHexHeader() {
			return hexHeader;
		}

	}

	private FileUtils() {}
}
