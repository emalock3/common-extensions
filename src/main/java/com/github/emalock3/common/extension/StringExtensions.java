package com.github.emalock3.common.extension;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Policy;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Signature;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import lombok.NonNull;
import lombok.experimental.ExtensionMethod;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * provides utility methods for java.lang.CharSequence.
 * 
 * @author Shinobu Aoki
 */
@ExtensionMethod({ObjectExtensions.class})
public final class StringExtensions {
	
	private static final Logger LOGGER = 
			LoggerFactory.getLogger(StringExtensions.class);
	
	private StringExtensions() {
	}
	
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	
	/**
	 * @param cs
	 * @return cs.toString() when cs is not null, otherwise returns null.
	 */
	public static String s(CharSequence cs) {
		if (cs == null) {
			return null;
		}
		return cs.toString();
	}
	
	/**
	 * @param cs
	 * @param args
	 * @return the String
	 * @see String#format(String, Object...)
	 */
	public static String fmt(CharSequence cs, Object... args) {
		if (cs == null) {
			return null;
		}
		return String.format(cs.toString(), args);
	}
	
	/**
	 * @param cs
	 * @param l
	 * @param args
	 * @return the String
	 * @see String#format(Locale, String, Object...)
	 */
	public static String fmt(CharSequence cs, Locale l, Object... args) {
		if (cs == null) {
			return null;
		}
		return String.format(l, cs.toString(), args);
	}
	
	/**
	 * @param cs
	 * @return the encoded URL if encoding is needed; the unchanged URL otherwise.
	 * @see #DEFAULT_CHARSET
	 * @see URLEncoder#encode(String, String)
	 */
	public static String encodeURL(CharSequence cs) {
		return encodeURL(cs, DEFAULT_CHARSET);
	}
	
	/**
	 * @param cs
	 * @param charset
	 * @return the encoded URL if encoding is needed; the unchanged URL otherwise.
	 * @see URLEncoder#encode(String, String)
	 */
	public static String encodeURL(CharSequence cs, @NonNull Charset charset) {
		if (cs == null) {
			return null;
		}
		try {
			return URLEncoder.encode(cs.toString(), charset.name());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @param cs
	 * @return the decoded URL
	 * @see #DEFAULT_CHARSET
	 * @see URLDecoder#decode(String, String)
	 */
	public static String decodeURL(CharSequence cs) {
		return decodeURL(cs, DEFAULT_CHARSET);
	}
	
	/**
	 * @param cs
	 * @param charset
	 * @return the decoded URL
	 * @see URLDecoder#decode(String, String)
	 */
	public static String decodeURL(CharSequence cs, @NonNull Charset charset) {
		if (cs == null) {
			return null;
		}
		try {
			return URLDecoder.decode(cs.toString(), charset.name());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @param cs the CharSequence to convert
	 * @return the InputStream
	 */
	public static InputStream toInputStream(CharSequence cs) {
		return toInputStream(cs, DEFAULT_CHARSET);
	}
	
	/**
	 * @param cs the CharSequence to convert
	 * @param charset for converting byte array from CharSequence
	 * @return the InputStream
	 */
	public static InputStream toInputStream(CharSequence cs, @NonNull Charset charset) {
		if (cs == null) {
			return null;
		}
		return new ByteArrayInputStream(cs.toString().getBytes(charset));
	}
	
	/**
	 * @param cs
	 * @return the Reader
	 */
	public static Reader toReader(CharSequence cs) {
		if (cs == null) {
			return null;
		}
		return new StringReader(cs.toString());
	}
	
	/**
	 * @param cs
	 * @return the URL
	 */
	public static URL toURL(CharSequence cs) {
		if (cs == null) {
			return null;
		}
		try {
			return new URL(cs.toString());
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @return the URI
	 */
	public static URI toURI(CharSequence cs) {
		if (cs == null) {
			return null;
		}
		try {
			return new URI(cs.toString());
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @return the File
	 */
	public static File toFile(CharSequence cs) {
		if (cs == null) {
			return null;
		}
		return new File(cs.toString());
	}
	
	/**
	 * @param cs
	 * @return the Path
	 */
	public static Path toPath(CharSequence cs) {
		if (cs == null) {
			return null;
		}
		return Paths.get(toURI(cs));
	}
	
	/**
	 * @param cs
	 * @return the Pattern
	 * @see Pattern#compile(String)
	 */
	public static Pattern toPattern(CharSequence cs) {
		return toPattern(cs, 0);
	}
	
	/**
	 * @param cs
	 * @param flags
	 * @return the Pattern
	 * @see Pattern#compile(String, int)
	 */
	public static Pattern toPattern(CharSequence cs, int flags) {
		if (cs == null) {
			return null;
		}
		return Pattern.compile(cs.toString(), flags);
	}
	
	/**
	 * @param cs
	 * @return byte
	 * @see Byte#parseByte(String)
	 */
	public static byte toByte(CharSequence cs) {
		return toByte(cs, 10);
	}
	
	/**
	 * @param cs
	 * @param radix
	 * @return byte
	 * @see Byte#parseByte(String, int)
	 */
	public static byte toByte(CharSequence cs, int radix) {
		if (cs == null) {
			return (byte) 0;
		}
		return Byte.parseByte(cs.toString(), radix);
	}
	
	/**
	 * @param cs
	 * @param radix
	 * @param def
	 * @return byte
	 * @see Byte#parseByte(String, int)
	 */
	public static byte toByte(CharSequence cs, int radix, byte def) {
		if (cs == null) {
			return def;
		}
		try {
			return Byte.parseByte(cs.toString(), radix);
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	/**
	 * @param cs
	 * @return byte
	 * @see Byte#valueOf(String)
	 */
	public static Byte toByteObject(CharSequence cs) {
		return toByteObject(cs, 10);
	}
	
	/**
	 * @param cs
	 * @param radix
	 * @return byte
	 * @see Byte#valueOf(String, int)
	 */
	public static Byte toByteObject(CharSequence cs, int radix) {
		if (cs == null) {
			return null;
		}
		String s = cs.toString();
		try {
			return Byte.valueOf(s, radix);
		} catch (NumberFormatException ignore) {
			LOGGER.warn(ignore.getMessage());
			return null;
		}
	}
	
	/**
	 * @param cs
	 * @return short
	 * @see Short#parseShort(String)
	 */
	public static short toShort(CharSequence cs) {
		return toShort(cs, 10);
	}
	
	/**
	 * @param cs
	 * @param radix
	 * @return short
	 * @see Short#parseShort(String, int)
	 */
	public static short toShort(CharSequence cs, int radix) {
		if (cs == null) {
			return (byte) 0;
		}
		return Short.parseShort(cs.toString(), radix);
	}
	
	/**
	 * @param cs
	 * @param radix
	 * @param def
	 * @return short
	 * @see Short#parseShort(String, int)
	 */
	public static short toShort(CharSequence cs, int radix, short def) {
		if (cs == null) {
			return def;
		}
		try {
			return Short.parseShort(cs.toString(), radix);
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	/**
	 * @param cs
	 * @return Short
	 * @see Short#valueOf(String)
	 */
	public static Short toShortObject(CharSequence cs) {
		return toShortObject(cs, 10);
	}
	
	/**
	 * @param cs
	 * @param radix
	 * @return Short
	 * @see Short#valueOf(String, int)
	 */
	public static Short toShortObject(CharSequence cs, int radix) {
		if (cs == null) {
			return null;
		}
		try {
			return Short.valueOf(cs.toString(), radix);
		} catch (NumberFormatException ignore) {
			LOGGER.warn(ignore.getMessage());
			return null;
		}
	}
	
	/**
	 * @param cs
	 * @return int
	 * @see Integer#parseInt(String)
	 */
	public static int toInt(CharSequence cs) {
		return toInt(cs, 10);
	}
	
	/**
	 * @param cs
	 * @param radix
	 * @return int
	 * @see Integer#parseInt(String, int)
	 */
	public static int toInt(CharSequence cs, int radix) {
		if (cs == null) {
			return 0;
		}
		return Integer.parseInt(cs.toString(), radix);
	}
	
	/**
	 * @param cs
	 * @param radix
	 * @param def
	 * @return int
	 * @see Integer#parseInt(String, int)
	 */
	public static int toInt(CharSequence cs, int radix, int def) {
		if (cs == null) {
			return def;
		}
		try {
			return Integer.parseInt(cs.toString(), radix);
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	/**
	 * @param cs
	 * @return Integer
	 * @see Integer#valueOf(String)
	 */
	public static Integer toIntObject(CharSequence cs) {
		return toIntObject(cs, 10);
	}
	
	/**
	 * @param cs
	 * @param radix
	 * @return Integer
	 * @see Integer#valueOf(String, int)
	 */
	public static Integer toIntObject(CharSequence cs, int radix) {
		if (cs == null) {
			return null;
		}
		try {
			return Integer.valueOf(cs.toString(), radix);
		} catch (NumberFormatException ignore) {
			LOGGER.warn(ignore.getMessage());
			return null;
		}
	}
	
	/**
	 * @param cs
	 * @return long
	 * @see Long#parseLong(String)
	 */
	public static long toLong(CharSequence cs) {
		return toLong(cs, 10);
	}
	
	/**
	 * @param cs
	 * @param radix
	 * @return long
	 * @see Long#parseLong(String, int)
	 */
	public static long toLong(CharSequence cs, int radix) {
		if (cs == null) {
			return 0;
		}
		return Long.parseLong(cs.toString(), radix);
	}
	
	/**
	 * @param cs
	 * @param radix
	 * @param def
	 * @return long
	 * @see Long#parseLong(String, int)
	 */
	public static long toLong(CharSequence cs, int radix, long def) {
		if (cs == null) {
			return def;
		}
		try {
			return Long.parseLong(cs.toString(), radix);
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	/**
	 * @param cs
	 * @return Long
	 * @see Long#valueOf(String)
	 */
	public static Long toLongObject(CharSequence cs) {
		return toLongObject(cs, 10);
	}
	
	/**
	 * @param cs
	 * @param radix
	 * @return Long
	 * @see Long#valueOf(String, int)
	 */
	public static Long toLongObject(CharSequence cs, int radix) {
		if (cs == null) {
			return null;
		}
		try {
			return Long.valueOf(cs.toString(), radix);
		} catch (NumberFormatException ignore) {
			LOGGER.warn(ignore.getMessage());
			return null;
		}
	}
	
	/**
	 * @param cs
	 * @return the BigInteger
	 * @see BigInteger#BigInteger(String)
	 */
	public static BigInteger toBigInteger(CharSequence cs) {
		if (cs == null) {
			return null;
		}
		try {
			return new BigInteger(cs.toString());
		} catch (NumberFormatException ignore) {
			LOGGER.warn(ignore.getMessage());
			return null;
		}
	}
	
	/**
	 * @param cs
	 * @return float
	 * @see Float#parseFloat(String)
	 */
	public static float toFloat(CharSequence cs) {
		if (cs == null) {
			return 0.0f;
		}
		return Float.parseFloat(cs.toString());
	}
	
	/**
	 * @param cs
	 * @param def
	 * @return float
	 * @see Float#parseFloat(String)
	 */
	public static float toFloat(CharSequence cs, float def) {
		if (cs == null) {
			return def;
		}
		try {
			return Float.parseFloat(cs.toString());
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	/**
	 * @param cs
	 * @return Float
	 * @see Float#valueOf(String)
	 */
	public static Float toFloatObject(CharSequence cs) {
		if (cs == null) {
			return null;
		}
		try {
			return Float.valueOf(cs.toString());
		} catch (NumberFormatException ignore) {
			LOGGER.warn(ignore.getMessage());
			return null;
		}
	}
	
	/**
	 * @param cs
	 * @return double
	 * @see Double#parseDouble(String)
	 */
	public static double toDouble(CharSequence cs) {
		if (cs == null) {
			return 0.0;
		}
		return Double.parseDouble(cs.toString());
	}
	
	/**
	 * @param cs
	 * @param def
	 * @return double
	 * @see Double#parseDouble(String)
	 */
	public static double toDouble(CharSequence cs, double def) {
		if (cs == null) {
			return def;
		}
		try {
			return Double.parseDouble(cs.toString());
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	/**
	 * @param cs
	 * @return Double
	 * @see Double#parseDouble(String)
	 */
	public static Double toDoubleObject(CharSequence cs) {
		if (cs == null) {
			return null;
		}
		try {
			return Double.valueOf(cs.toString());
		} catch (NumberFormatException ignore) {
			LOGGER.warn(ignore.getMessage());
			return null;
		}
	}
	
	/**
	 * @param cs
	 * @return the BigDecimal
	 * @see BigDecimal#BigDecimal(String)
	 */
	public static BigDecimal toBigDecimal(CharSequence cs) {
		if (cs == null) {
			return null;
		}
		try {
			return new BigDecimal(cs.toString());
		} catch (NumberFormatException e) {
			LOGGER.warn(e.getMessage());
			return null;
		}
	}
	
	/**
	 * @param cs
	 * @return the CharBuffer
	 * @see CharBuffer#wrap(CharSequence)
	 */
	public static CharBuffer toCharBuffer(CharSequence cs) {
		if (cs == null) {
			return null;
		}
		return CharBuffer.wrap(cs);
	}
	
	/**
	 * @param cs
	 * @return the ByteBuffer
	 * @see ByteBuffer#wrap(byte[])
	 */
	public static ByteBuffer toByteBuffer(CharSequence cs) {
		return toByteBuffer(cs, DEFAULT_CHARSET);
	}
	
	/**
	 * @param cs
	 * @param charset
	 * @return the ByteBuffer
	 * @see ByteBuffer#wrap(byte[])
	 */
	public static ByteBuffer toByteBuffer(CharSequence cs, Charset charset) {
		if (cs == null) {
			return null;
		}
		return ByteBuffer.wrap(cs.toString().getBytes(charset));
	}
	
	/**
	 * @param cs
	 * @return the InetAddress
	 * @throws IllegalArgumentException If the given string is illegal
	 * @see InetAddress#getByName(String)
	 */
	public static InetAddress toInetAddress(CharSequence cs) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return InetAddress.getByName(cs.toString());
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @return the parsed date-time
	 * @throws IllegalArgumentException if the text to parse is invalid
	 * @see DateTime#parse(String)
	 */
	public static DateTime toDateTime(CharSequence cs) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		return DateTime.parse(cs.toString());
	}
	
	/**
	 * @param cs
	 * @param pattern
	 * @return the parsed date-time
	 * @throws IllegalArgumentException if the pattern is invalid or the text to parse is invalid
	 * @see DateTime#parse(String, org.joda.time.format.DateTimeFormatter)
	 */
	public static DateTime toDateTime(CharSequence cs, String pattern) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		return DateTime.parse(cs.toString(), DateTimeFormat.forPattern(pattern));
	}
	
	/**
	 * @param cs
	 * @return the parsed date
	 * @throws IllegalArgumentException if the text to parse is invalid
	 * @see DateTime#parse(String)
	 */
	public static Date toDate(CharSequence cs) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		return DateTime.parse(cs.toString()).toDate();
	}
	
	/**
	 * @param cs
	 * @param pattern
	 * @return the parsed date
	 * @throws IllegalArgumentException if the pattern is invalid or the text to parse is invalid
	 * @see DateTime#parse(String, org.joda.time.format.DateTimeFormatter)
	 */
	public static Date toDate(CharSequence cs, String pattern) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		return DateTime.parse(cs.toString(), DateTimeFormat.forPattern(pattern)).toDate();
	}
	
	/**
	 * @param cs
	 * @return the DateFormat
	 * @see SimpleDateFormat#SimpleDateFormat(String)
	 */
	public static DateFormat toDateFormat(CharSequence cs) {
		if (cs == null) {
			return null;
		}
		return new SimpleDateFormat(cs.toString());
	}
	
	/**
	 * @param cs
	 * @param locale
	 * @return the DateFormat
	 * @see SimpleDateFormat#SimpleDateFormat(String, Locale)
	 */
	public static DateFormat toDateFormat(CharSequence cs, @NonNull Locale locale) {
		if (cs == null) {
			return null;
		}
		return new SimpleDateFormat(cs.toString(), locale);
	}
	
	/**
	 * @param language
	 * @return the Locale
	 * @see Locale#Locale(String)
	 */
	public static Locale toLocale(CharSequence language) {
		if (language == null) {
			return null;
		}
		return new Locale(language.toString());
	}
	
	/**
	 * @param language
	 * @param country
	 * @return the Locale
	 * @see Locale#Locale(String, String)
	 */
	public static Locale toLocale(CharSequence language, String country) {
		if (language == null || country == null) {
			return null;
		}
		return new Locale(language.toString(), country);
	}
	
	/**
	 * @param language
	 * @param country
	 * @param variant
	 * @return the Locale
	 * @see Locale#Locale(String, String, String)
	 */
	public static Locale toLocale(CharSequence language, String country, String variant) {
		if (language == null || country == null || variant == null) {
			return null;
		}
		return new Locale(language.toString(), country, variant);
	}
	
	/**
	 * @param cs
	 * @return the TimeZone
	 * @see TimeZone#getTimeZone(String)
	 */
	public static TimeZone toTimeZone(CharSequence cs) {
		if (cs == null) {
			return null;
		}
		return TimeZone.getTimeZone(cs.toString());
	}
	
	/**
	 * @param algorithm
	 * @return the MessageDigest
	 * @throws IllegalArgumentException
	 * @see MessageDigest#getInstance(String)
	 */
	public static MessageDigest toMessageDigest(CharSequence algorithm) throws IllegalArgumentException {
		if (algorithm == null) {
			return null;
		}
		try {
			return MessageDigest.getInstance(algorithm.toString());
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param algorithm
	 * @param provider
	 * @return the MessageDigest
	 * @throws IllegalArgumentException
	 * @see MessageDigest#getInstance(String, String)
	 */
	public static MessageDigest toMessageDigest(CharSequence algorithm, String provider) throws IllegalArgumentException {
		if (algorithm == null) {
			return null;
		}
		try {
			return MessageDigest.getInstance(algorithm.toString(), provider);
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param algorithm
	 * @param provider
	 * @return the MessageDigest
	 * @throws IllegalArgumentException
	 * @see MessageDigest#getInstance(String, Provider)
	 */
	public static MessageDigest toMessageDigest(CharSequence algorithm, Provider provider) throws IllegalArgumentException {
		if (algorithm == null) {
			return null;
		}
		try {
			return MessageDigest.getInstance(algorithm.toString(), provider);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param algorithm
	 * @return the KeyFactory
	 * @throws IllegalArgumentException
	 * @see KeyFactory#getInstance(String)
	 */
	public static KeyFactory toKeyFactory(CharSequence algorithm) throws IllegalArgumentException {
		if (algorithm == null) {
			return null;
		}
		try {
			return KeyFactory.getInstance(algorithm.toString());
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param algorithm
	 * @param provider
	 * @return the KeyFactory
	 * @throws IllegalArgumentException
	 * @see KeyFactory#getInstance(String, String)
	 */
	public static KeyFactory toKeyFactory(CharSequence algorithm, String provider) throws IllegalArgumentException {
		if (algorithm == null) {
			return null;
		}
		try {
			return KeyFactory.getInstance(algorithm.toString(), provider);
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param algorithm
	 * @param provider
	 * @return the KeyFactory
	 * @throws IllegalArgumentException
	 * @see KeyFactory#getInstance(String, Provider)
	 */
	public static KeyFactory toKeyFactory(CharSequence algorithm, Provider provider) throws IllegalArgumentException {
		if (algorithm == null) {
			return null;
		}
		try {
			return KeyFactory.getInstance(algorithm.toString(), provider);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @return the KeyStore
	 * @throws IllegalArgumentException
	 */
	public static KeyStore toKeyStore(CharSequence cs) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return KeyStore.getInstance(cs.toString());
		} catch (KeyStoreException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @param provider
	 * @return the KeyStore
	 * @throws IllegalArgumentException
	 */
	public static KeyStore toKeyStore(CharSequence cs, String provider) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return KeyStore.getInstance(cs.toString(), provider);
		} catch (KeyStoreException | NoSuchProviderException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @param provider
	 * @return the KeyStore
	 * @throws IllegalArgumentException
	 */
	public static KeyStore toKeyStore(CharSequence cs, Provider provider) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return KeyStore.getInstance(cs.toString(), provider);
		} catch (KeyStoreException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @return the Signature
	 * @throws IllegalArgumentException
	 */
	public static Signature toSignature(CharSequence cs) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return Signature.getInstance(cs.toString());
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @param provider
	 * @return the Signature
	 * @throws IllegalArgumentException
	 */
	public static Signature toSignature(CharSequence cs, String provider) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return Signature.getInstance(cs.toString(), provider);
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @param provider
	 * @return the Signature
	 * @throws IllegalArgumentException
	 */
	public static Signature toSignature(CharSequence cs, Provider provider) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return Signature.getInstance(cs.toString(), provider);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @return the SecureRandom
	 * @throws IllegalArgumentException
	 */
	public static SecureRandom toSecureRandom(CharSequence cs) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return SecureRandom.getInstance(cs.toString());
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @param provider
	 * @return the SecureRandom
	 * @throws IllegalArgumentException
	 */
	public static SecureRandom toSecureRandom(CharSequence cs, String provider) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return SecureRandom.getInstance(cs.toString(), provider);
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @param provider
	 * @return the SecureRandom
	 * @throws IllegalArgumentException
	 */
	public static SecureRandom toSecureRandom(CharSequence cs, Provider provider) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return SecureRandom.getInstance(cs.toString(), provider);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @return the KeyPairGenerator
	 * @throws IllegalArgumentException
	 */
	public static KeyPairGenerator toKeyPairGenerator(CharSequence cs) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return KeyPairGenerator.getInstance(cs.toString());
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @param provider
	 * @return the KeyPairGenerator
	 * @throws IllegalArgumentException
	 */
	public static KeyPairGenerator toKeyPairGenerator(CharSequence cs, String provider) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return KeyPairGenerator.getInstance(cs.toString(), provider);
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @param provider
	 * @return the KeyPairGenerator
	 * @throws IllegalArgumentException
	 */
	public static KeyPairGenerator toKeyPairGenerator(CharSequence cs, Provider provider) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return KeyPairGenerator.getInstance(cs.toString(), provider);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @return the AlgorithmParameters
	 * @throws IllegalArgumentException
	 */
	public static AlgorithmParameters toAlgorithmParameters(CharSequence cs) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return AlgorithmParameters.getInstance(cs.toString());
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @param provider
	 * @return the AlgorithmParameters
	 * @throws IllegalArgumentException
	 */
	public static AlgorithmParameters toAlgorithmParameters(CharSequence cs, String provider) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return AlgorithmParameters.getInstance(cs.toString(), provider);
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @param provider
	 * @return the AlgorithmParameters
	 * @throws IllegalArgumentException
	 */
	public static AlgorithmParameters toAlgorithmParameters(CharSequence cs, Provider provider) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return AlgorithmParameters.getInstance(cs.toString(), provider);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @return the AlgorithmParameterGenerator
	 * @throws IllegalArgumentException
	 */
	public static AlgorithmParameterGenerator toAlgorithmParameterGenerator(CharSequence cs) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return AlgorithmParameterGenerator.getInstance(cs.toString());
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @param provider
	 * @return the AlgorithmParameterGenerator
	 * @throws IllegalArgumentException
	 */
	public static AlgorithmParameterGenerator toAlgorithmParameterGenerator(CharSequence cs, String provider) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return AlgorithmParameterGenerator.getInstance(cs.toString(), provider);
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @param provider
	 * @return the AlgorithmParameterGenerator
	 * @throws IllegalArgumentException
	 */
	public static AlgorithmParameterGenerator toAlgorithmParameterGenerator(CharSequence cs, Provider provider) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return AlgorithmParameterGenerator.getInstance(cs.toString(), provider);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @param params
	 * @return the Policy
	 * @throws IllegalArgumentException
	 */
	public static Policy toPolicy(CharSequence cs, Policy.Parameters params) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return Policy.getInstance(cs.toString(), params);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @param provider
	 * @return the Policy
	 * @throws IllegalArgumentException
	 */
	public static Policy toPolicy(CharSequence cs, Policy.Parameters params, String provider) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return Policy.getInstance(cs.toString(), params, provider);
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * @param cs
	 * @param provider
	 * @return the Policy
	 * @throws IllegalArgumentException
	 */
	public static Policy toPolicy(CharSequence cs, Policy.Parameters params, Provider provider) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		try {
			return Policy.getInstance(cs.toString(), params, provider);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	
	/**
	 * @param cs
	 * @return the Charset
	 * @throws IllegalArgumentException
	 * @see Charset#forName(String)
	 */
	public static Charset toCharset(CharSequence cs) throws IllegalArgumentException {
		if (cs == null) {
			return null;
		}
		return Charset.forName(cs.toString());
	}
}
