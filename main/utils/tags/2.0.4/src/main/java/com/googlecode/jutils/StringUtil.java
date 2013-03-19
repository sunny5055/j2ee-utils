package com.googlecode.jutils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.collection.CollectionUtil;

/**
 * Class StringUtil.
 */
public final class StringUtil extends StringUtils {

	private StringUtil() {
		super();
	}

	/**
	 * To uppercase first character.
	 * 
	 * @param string2convert
	 *            the string2convert
	 * @return the string
	 */
	public static String toUppercaseFirstCharacter(String string2convert) {
		return toUppercaseFirstCharacters(string2convert, 1);
	}

	/**
	 * To uppercase first characters.
	 * 
	 * @param string2convert
	 *            the string2convert
	 * @param charactersNumber
	 *            the characters number
	 * @return the string
	 */
	public static String toUppercaseFirstCharacters(String string2convert, int charactersNumber) {
		String result = null;
		if (StringUtil.isBlank(string2convert) || charactersNumber <= 0) {
			result = string2convert;
		} else if (charactersNumber >= string2convert.length()) {
			result = string2convert.toUpperCase();
		} else {
			final String leftStr = string2convert.substring(0, charactersNumber);
			final String rightStr = string2convert.substring(charactersNumber);
			final StringBuffer sb = new StringBuffer();
			sb.append(leftStr.toUpperCase());
			sb.append(rightStr);
			result = sb.toString();
		}
		return result;
	}

	/**
	 * To uppercase last characters.
	 * 
	 * @param string2convert
	 *            the string2convert
	 * @param charactersNumber
	 *            the characters number
	 * @return the string
	 */
	public static String toUppercaseLastCharacters(String string2convert, int charactersNumber) {
		String result = null;
		if (StringUtil.isBlank(string2convert) || charactersNumber <= 0) {
			result = string2convert;
		} else if (charactersNumber >= string2convert.length()) {
			result = string2convert.toUpperCase();
		} else {
			final String leftStr = string2convert.substring(0, string2convert.length() - charactersNumber);
			final String rightStr = string2convert.substring(string2convert.length() - charactersNumber);
			final StringBuffer sb = new StringBuffer();
			sb.append(leftStr);
			sb.append(rightStr.toUpperCase());
			result = sb.toString();
		}
		return result;
	}

	/**
	 * To lowercase first character.
	 * 
	 * @param string2convert
	 *            the string2convert
	 * @return the string
	 */
	public static String toLowercaseFirstCharacter(String string2convert) {
		return toLowercaseFirstCharacters(string2convert, 1);
	}

	/**
	 * To lowercase first characters.
	 * 
	 * @param string2convert
	 *            the string2convert
	 * @param charactersNumber
	 *            the characters number
	 * @return the string
	 */
	public static String toLowercaseFirstCharacters(String string2convert, int charactersNumber) {
		String result = null;
		if (StringUtil.isBlank(string2convert) || charactersNumber <= 0) {
			result = string2convert;
		} else if (charactersNumber >= string2convert.length()) {
			result = string2convert.toLowerCase();
		} else {
			final String leftStr = string2convert.substring(0, charactersNumber);
			final String rightStr = string2convert.substring(charactersNumber);
			final StringBuffer sb = new StringBuffer();
			sb.append(leftStr.toLowerCase());
			sb.append(rightStr);
			result = sb.toString();
		}
		return result;
	}

	/**
	 * Method toLowercaseLastCharacters.
	 * 
	 * @param string2convert
	 *            the string2convert
	 * @param charactersNumber
	 *            the charactersNumber
	 * @return String
	 */
	public static String toLowercaseLastCharacters(String string2convert, int charactersNumber) {
		if (StringUtil.isBlank(string2convert) || charactersNumber <= 0) {
			return string2convert;
		} else {
			if (charactersNumber >= string2convert.length()) {
				return string2convert.toLowerCase();
			} else {
				final String leftStr = string2convert.substring(0, string2convert.length() - charactersNumber);
				final String rightStr = string2convert.substring(string2convert.length() - charactersNumber);
				final StringBuffer sb = new StringBuffer();
				sb.append(leftStr);
				sb.append(rightStr.toLowerCase());
				return sb.toString();
			}
		}
	}

	/**
	 * To upper case.
	 * 
	 * @param values
	 *            le values
	 * @return le list
	 */
	public static List<String> toUpperCase(String... values) {
		List<String> upperCaseValues = null;
		if (!ArrayUtil.isEmpty(values)) {
			upperCaseValues = new ArrayList<String>();
			for (final String value : values) {
				upperCaseValues.add(value.toUpperCase());
			}
		}
		return upperCaseValues;
	}

	/**
	 * To upper case.
	 * 
	 * @param values
	 *            le values
	 * @return le list
	 */
	public static List<String> toUpperCase(List<String> values) {
		List<String> upperCaseValues = null;
		if (!CollectionUtil.isEmpty(values)) {
			upperCaseValues = new ArrayList<String>();
			for (final String value : values) {
				upperCaseValues.add(value.toUpperCase());
			}
		}
		return upperCaseValues;
	}

	/**
	 * @param string2convert
	 * @return
	 */
	public static String toCamelCase(String string2convert) {
		String result = null;
		if (StringUtil.isBlank(string2convert)) {
			result = string2convert;
		} else {
			final StringBuilder sb = new StringBuilder();
			boolean toUpperCase = false;
			for (int i = 0; i < string2convert.length(); i++) {
				final char c = string2convert.charAt(i);
				if (Character.isWhitespace(c) || c == '_' || c == '-') {
					toUpperCase = true;
				} else {
					if (toUpperCase) {
						sb.append(Character.toUpperCase(c));
					} else {
						sb.append(c);
					}
					toUpperCase = false;
				}
			}

			result = sb.toString();
			result = toLowercaseFirstCharacter(result);
		}
		return result;
	}

	/**
	 * @param string2convert
	 * @return
	 */
	public static String toPascalCase(String string2convert) {
		String result = null;
		if (!StringUtil.isBlank(string2convert)) {
			result = toCamelCase(string2convert);
			result = toUppercaseFirstCharacter(result);
		}
		return result;
	}

	/**
	 * To underscore case.
	 * 
	 * @param string2convert
	 *            the string2convert
	 * @param upperCase
	 *            the upper case
	 * @return the string
	 */
	public static String toUnderscoreCase(String string2convert, boolean upperCase) {
		String result = null;
		if (StringUtil.isBlank(string2convert)) {
			result = string2convert;
		} else {
			final StringBuilder sb = new StringBuilder();
			boolean underscore = false;
			for (int i = 0; i < string2convert.length(); i++) {
				final char c = string2convert.charAt(i);

				if (Character.isWhitespace(c) || c == '_' || c == '-') {
					underscore = true;
				} else {
					if (Character.isUpperCase(c)) {
						underscore = true;
					}

					if (i != 0 && underscore) {
						sb.append("_");
					}

					if (upperCase) {
						sb.append(Character.toUpperCase(c));
					} else {
						sb.append(Character.toLowerCase(c));
					}

					underscore = false;
				}
			}

			result = sb.toString();
		}
		return result;
	}

	/**
	 * Method toAscii.
	 * 
	 * @param string
	 *            the string
	 * @return String
	 */
	public static String toAscii(String string) {
		String value = null;
		if (!StringUtil.isBlank(string)) {
			value = string;
			value = value.replaceAll("[àáâãäå]", "a");
			value = value.replaceAll("[ÀÁÂÃÄÅ]", "A");
			value = value.replaceAll("[èéêë]", "e");
			value = value.replaceAll("[ÈÉÊË]", "E");
			value = value.replaceAll("[ìíîï]", "i");
			value = value.replaceAll("[ÌÍÎÏ]", "I");
			value = value.replaceAll("[òóôõöø]", "o");
			value = value.replaceAll("[ÒÓÔÕÖØ]", "O");
			value = value.replaceAll("[ùúûü]", "u");
			value = value.replaceAll("[ÙÚÛÜ]", "U");
			value = value.replaceAll("[ç]", "c");
			value = value.replaceAll("[Ç]", "C");
			value = value.replaceAll("[ÿý]", "y");
			value = value.replaceAll("[ŸÝ]", "Y");
			value = value.replaceAll("[ñ]", "n");
			value = value.replaceAll("[Ñ]", "N");
			value = value.replaceAll("[æ]", "ae");
			value = value.replaceAll("[Æ]", "AE");
		}
		return value;
	}

	/**
	 * Split pascal case.
	 * 
	 * @param pascalCase
	 *            le pascal case
	 * @return le string
	 */
	public static String splitPascalCase(String pascalCase) {
		String value = null;
		if (!StringUtil.isBlank(pascalCase)) {
			final String[] array = StringUtil.splitPascalCaseToArray(pascalCase);
			if (!ArrayUtil.isEmpty(array)) {
				value = "";
				for (int i = 0; i < array.length; i++) {
					value += array[i];
					if (i + 1 < array.length) {
						value += " ";
					}
				}
			}
		}
		return value;
	}

	/**
	 * Split pascal case to array.
	 * 
	 * @param pascalCase
	 *            the pascal case
	 * @return the string[]
	 */
	public static String[] splitPascalCaseToArray(String pascalCase) {
		String[] array = null;
		if (!StringUtil.isBlank(pascalCase)) {
			array = StringUtil.splitByCharacterTypeCamelCase(pascalCase);
			if (!ArrayUtil.isEmpty(array)) {
				for (int i = 0; i < array.length; i++) {
					array[i] = StringUtil.toLowercaseFirstCharacter(array[i]);
				}
			}
		}
		return array;
	}

	public static boolean endsWithAnyIgnoreCase(String string, String... searchStrings) {
		boolean endsWith = false;
		if (!StringUtil.isBlank(string) && !ArrayUtil.isEmpty(searchStrings)) {
			for (final String searchString : searchStrings) {
				if (StringUtils.endsWithIgnoreCase(string, searchString)) {
					endsWith = true;
					break;
				}
			}
		}
		return endsWith;
	}

	/**
	 * Like.
	 * 
	 * @param str
	 *            the str
	 * @param searchString
	 *            the search string
	 * @return true, if successful
	 */
	public static boolean like(final String str, final String searchString) {
		boolean like = false;
		if (!StringUtil.isBlank(str)) {
			String regex = escapeRegex(searchString);
			if (!StringUtil.isBlank(regex)) {
				regex = regex.replace("?", ".");
				regex = regex.replace("*", ".*");

				final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
				like = pattern.matcher(str).matches();
			}
		}
		return like;
	}

	/**
	 * Parses the given input text into a String list without duplicates.
	 * 
	 * @param inputText
	 *            the input text
	 * @param separators
	 *            the separators
	 * @return the list
	 */
	public static List<String> parseIntoList(String inputText, String... separators) {
		final List<String> result = new ArrayList<String>();
		if (!StringUtil.isBlank(inputText) && separators.length > 0) {

			final String masterSeparator = separators[0];
			for (final String sep : separators) {
				inputText = StringUtil.replace(inputText, sep, masterSeparator);
			}
			final String[] chunks = StringUtil.split(inputText, masterSeparator);

			if (!ArrayUtil.isEmpty(chunks)) {
				for (final String chunk : chunks) {
					if (!StringUtil.isBlank(chunk)) {
						final String trimmed = chunk.trim();
						if (!result.contains(trimmed)) {
							result.add(trimmed);
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Parses the given input text into an Integer list without duplicates.
	 * 
	 * @param inputText
	 *            the input text
	 * @param separators
	 *            the separators
	 * @return the list
	 */
	public static List<Integer> parseIntoIntegerList(String inputText, String... separators) {
		final List<Integer> result = new ArrayList<Integer>();
		if (!StringUtil.isBlank(inputText) && separators.length > 0) {

			final String masterSeparator = separators[0];
			for (final String sep : separators) {
				inputText = StringUtil.replace(inputText, sep, masterSeparator);
			}
			final String[] chunks = StringUtil.split(inputText, masterSeparator);

			if (!ArrayUtil.isEmpty(chunks)) {
				for (final String chunk : chunks) {
					if (!StringUtil.isBlank(chunk)) {
						final Integer value = Integer.valueOf(chunk);
						if (!result.contains(value)) {
							result.add(value);
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Escape regex.
	 * 
	 * @param str
	 *            the str
	 * @return the string
	 */
	private static String escapeRegex(String str) {
		final StringBuilder builder = new StringBuilder();
		if (!StringUtil.isBlank(str)) {
			for (int i = 0; i < str.length(); i++) {
				final char c = str.charAt(i);
				if (StringUtil.indexOf("[](){}.+$^|#\\", c) != -1) {
					builder.append("\\");
				}
				builder.append(c);
			}
		}
		return builder.toString();
	}
}
