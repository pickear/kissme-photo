package com.kissme.photo.infrastructure;


/**
 * 
 * @author loudyn
 * 
 */
public class Files {

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	public static enum FileType {
		JPG("jpg", "FFD8FF"), ICO("ico", "00000100"), BMP("bmp", "424D"), GIF("gif", "47494638"), PNG("png", "89504E47"),
		ZIP("zip", "504B0304"), RAR("rar", "52617221"),
		PDF("pdf", "25504446"), DOC("doc", "D0CF11E0"), DOCX("docx", "504B0304"), XLS_OR_PPT("xls;ppt", "D0CF11E0");

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

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static FileType guess(byte[] bytes) {
		if (bytes.length <= 32) {
			return null;
		}

		byte[] header = new byte[32];
		System.arraycopy(bytes, 0, header, 0, 32);
		String hexHeader = fileHexHeader(header);
		
		for (FileType type : FileType.values()) {

			if (hexHeader.startsWith(type.getHexHeader())) {
				return type;
			}
		}

		return null;
	}

	private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static String fileHexHeader(byte[] bytes) {
		int length = bytes.length;
		char[] out = new char[length << 1];
		for (int i = 0, j = 0; i < length; i++) {
			out[j++] = DIGITS_UPPER[(0xF0 & bytes[i]) >>> 4];
			out[j++] = DIGITS_UPPER[0x0F & bytes[i]];
		}

		return new String(out);
	}

	private Files() {}
}
