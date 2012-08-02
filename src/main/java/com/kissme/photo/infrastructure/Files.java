package com.kissme.photo.infrastructure;

import org.apache.commons.codec.binary.Hex;

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
		JPG("jpg", "FFD8FFE0"), ICO("ico", "00000100"), BMP("bmp", "424D"), GIF("gif", "47494638"), PNG("png", "89504E47"),
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
		String hexHeader = Hex.encodeHexString(bytes);

		for (FileType type : FileType.values()) {

			if (hexHeader.startsWith(type.getHexHeader())) {
				return type;
			}
		}

		return null;
	}

	private Files() {}
}
