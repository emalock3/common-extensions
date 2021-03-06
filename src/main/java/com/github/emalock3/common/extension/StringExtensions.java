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
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Policy;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;

import lombok.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * provides utility methods for java.lang.CharSequence.
 *
 * @author Shinobu Aoki
 */
public final class StringExtensions {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(StringExtensions.class);

    private StringExtensions() {
    }

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    public static final Optional<Charset> DEFAULT_CHARSET_OPT = Optional.of(DEFAULT_CHARSET);

    /**
     * @param cs the CharSequence
     * @return cs.toString() when cs is not null, otherwise returns null.
     */
    public static String s(CharSequence cs) {
        if (cs == null) {
            return null;
        }
        return cs.toString();
    }

    /**
     * @param cs the CharSequence
     * @param args
     * @return the String
     * @see String#format(String, Object...)
     */
    public static String fmt(CharSequence cs, Object... args) {
        return fmt(cs, Optional.empty(), args);
    }

    /**
     * @param cs the CharSequence
     * @param lopt
     * @param args
     * @return the String
     * @see String#format(Locale, String, Object...)
     */
    public static String fmt(CharSequence cs, Optional<Locale> lopt, Object... args) {
        if (cs == null) {
            return null;
        }
        return lopt.map(l -> String.format(l, cs.toString(), args)).orElse(String.format(cs.toString(), args));
    }

    /**
     * @param cs the CharSequence
     * @return the encoded URL if encoding is needed; the unchanged URL otherwise.
     * @see #DEFAULT_CHARSET
     * @see URLEncoder#encode(String, String)
     */
    public static String encodeURL(CharSequence cs) {
        return encodeURL(cs, DEFAULT_CHARSET_OPT);
    }

    /**
     * @param cs the CharSequence
     * @param charsetOpt
     * @return the encoded URL if encoding is needed; the unchanged URL otherwise.
     * @see URLEncoder#encode(String, String)
     */
    public static String encodeURL(CharSequence cs, @NonNull Optional<Charset> charsetOpt) {
        if (cs == null) {
            return null;
        }
        try {
            return URLEncoder.encode(cs.toString(), charsetOpt.orElse(DEFAULT_CHARSET).name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param cs the CharSequence
     * @return the decoded URL
     * @see #DEFAULT_CHARSET
     * @see URLDecoder#decode(String, String)
     */
    public static String decodeURL(CharSequence cs) {
        return decodeURL(cs, DEFAULT_CHARSET_OPT);
    }

    /**
     * @param cs the CharSequence
     * @param charsetOpt
     * @return the decoded URL
     * @see URLDecoder#decode(String, String)
     */
    public static String decodeURL(CharSequence cs, @NonNull Optional<Charset> charsetOpt) {
        if (cs == null) {
            return null;
        }
        try {
            return URLDecoder.decode(cs.toString(), charsetOpt.orElse(DEFAULT_CHARSET).name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param cs the CharSequence to convert
     * @return the InputStream
     */
    public static InputStream toInputStream(CharSequence cs) {
        return toInputStream(cs, DEFAULT_CHARSET_OPT);
    }

    /**
     * @param cs the CharSequence to convert
     * @param charsetOpt for converting byte array from CharSequence
     * @return the InputStream
     */
    public static InputStream toInputStream(CharSequence cs, @NonNull Optional<Charset> charsetOpt) {
        if (cs == null) {
            return null;
        }
        return new ByteArrayInputStream(cs.toString().getBytes(charsetOpt.orElse(DEFAULT_CHARSET)));
    }

    /**
     * @param cs the CharSequence
     * @return the Reader
     */
    public static Reader toReader(CharSequence cs) {
        if (cs == null) {
            return null;
        }
        return new StringReader(cs.toString());
    }

    /**
     * @param cs the CharSequence
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
     * @param cs the CharSequence
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
     * @param cs the CharSequence
     * @return the File
     */
    public static File toFile(CharSequence cs) {
        if (cs == null) {
            return null;
        }
        return new File(cs.toString());
    }

    /**
     * @param cs the CharSequence
     * @return the Path
     */
    public static Path toPath(CharSequence cs) {
        if (cs == null) {
            return null;
        }
        return toFile(cs).toPath();
    }

    /**
     * @param cs the CharSequence
     * @return the Pattern
     * @see Pattern#compile(String)
     */
    public static Pattern toPattern(CharSequence cs) {
        return toPattern(cs, 0);
    }

    /**
     * @param cs the CharSequence
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
     * @param cs the CharSequence
     * @return byte
     * @see Byte#parseByte(String)
     */
    public static byte toByte(CharSequence cs) {
        return toByte(cs, 10);
    }

    /**
     * @param cs the CharSequence
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
     * @param cs the CharSequence
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
     * @param cs the CharSequence
     * @return byte
     * @see Byte#valueOf(String)
     */
    public static Byte toByteObject(CharSequence cs) {
        return toByteObject(cs, 10);
    }

    /**
     * @param cs the CharSequence
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
            LOGGER.debug(
                    "{} has occurred at {}#toByteObject(CharSequence, int). {}",
                    ignore.getClass().getName(),
                    StringExtensions.class.getName(), ignore.getMessage());
            return null;
        }
    }

    /**
     * @param cs the CharSequence
     * @return short
     * @see Short#parseShort(String)
     */
    public static short toShort(CharSequence cs) {
        return toShort(cs, 10);
    }

    /**
     * @param cs the CharSequence
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
     * @param cs the CharSequence
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
     * @param cs the CharSequence
     * @return Short
     * @see Short#valueOf(String)
     */
    public static Short toShortObject(CharSequence cs) {
        return toShortObject(cs, 10);
    }

    /**
     * @param cs the CharSequence
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
            LOGGER.debug(
                    "{} has occurred at {}#toShortObject(CharSequence, int). {}",
                    ignore.getClass().getName(),
                    StringExtensions.class.getName(), ignore.getMessage());
            return null;
        }
    }

    /**
     * @param cs the CharSequence
     * @return int
     * @see Integer#parseInt(String)
     */
    public static int toInt(CharSequence cs) {
        return toInt(cs, 10);
    }

    /**
     * @param cs the CharSequence
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
     * @param cs the CharSequence
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
     * @param cs the CharSequence
     * @return Integer
     * @see Integer#valueOf(String)
     */
    public static Integer toIntObject(CharSequence cs) {
        return toIntObject(cs, 10);
    }

    /**
     * @param cs the CharSequence
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
            LOGGER.debug(
                    "{} has occurred at {}#toIntObject(CharSequence, int). {}",
                    ignore.getClass().getName(),
                    StringExtensions.class.getName(), ignore.getMessage());
            return null;
        }
    }

    /**
     * @param cs the CharSequence
     * @return long
     * @see Long#parseLong(String)
     */
    public static long toLong(CharSequence cs) {
        return toLong(cs, 10);
    }

    /**
     * @param cs the CharSequence
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
     * @param cs the CharSequence
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
     * @param cs the CharSequence
     * @return Long
     * @see Long#valueOf(String)
     */
    public static Long toLongObject(CharSequence cs) {
        return toLongObject(cs, 10);
    }

    /**
     * @param cs the CharSequence
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
            LOGGER.debug(
                    "{} has occurred at {}#toLongObject(CharSequence, int). {}",
                    ignore.getClass().getName(),
                    StringExtensions.class.getName(), ignore.getMessage());
            return null;
        }
    }

    /**
     * @param cs the CharSequence
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
            LOGGER.debug(
                    "{} has occurred at {}#toBigInteger(CharSequence, int). {}",
                    ignore.getClass().getName(),
                    StringExtensions.class.getName(), ignore.getMessage());
            return null;
        }
    }

    /**
     * @param cs the CharSequence
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
     * @param cs the CharSequence
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
     * @param cs the CharSequence
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
            LOGGER.debug(
                    "{} has occurred at {}#toFloatObject(CharSequence, int). {}",
                    ignore.getClass().getName(),
                    StringExtensions.class.getName(), ignore.getMessage());
            return null;
        }
    }

    /**
     * @param cs the CharSequence
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
     * @param cs the CharSequence
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
     * @param cs the CharSequence
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
            LOGGER.debug(
                    "{} has occurred at {}#toDoubleObject(CharSequence, int). {}",
                    ignore.getClass().getName(),
                    StringExtensions.class.getName(), ignore.getMessage());
            return null;
        }
    }

    /**
     * @param cs the CharSequence
     * @return the BigDecimal
     * @see BigDecimal#BigDecimal(String)
     */
    public static BigDecimal toBigDecimal(CharSequence cs) {
        if (cs == null) {
            return null;
        }
        try {
            return new BigDecimal(cs.toString());
        } catch (NumberFormatException ignore) {
            LOGGER.debug(
                    "{} has occurred at {}#toBigDecimal(CharSequence, int). {}",
                    ignore.getClass().getName(),
                    StringExtensions.class.getName(), ignore.getMessage());
            return null;
        }
    }

    /**
     * @param cs the CharSequence
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
     * @param cs the CharSequence
     * @return the ByteBuffer
     * @see ByteBuffer#wrap(byte[])
     */
    public static ByteBuffer toByteBuffer(CharSequence cs) {
        return toByteBuffer(cs, DEFAULT_CHARSET_OPT);
    }

    /**
     * @param cs the CharSequence
     * @param charsetOpt
     * @return the ByteBuffer
     * @see ByteBuffer#wrap(byte[])
     */
    public static ByteBuffer toByteBuffer(CharSequence cs, @NonNull Optional<Charset> charsetOpt) {
        if (cs == null) {
            return null;
        }
        return ByteBuffer.wrap(cs.toString().getBytes(charsetOpt.orElse(DEFAULT_CHARSET)));
    }

    /**
     * @param cs the CharSequence
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
     * @param cs the CharSequence
     * @return the parsed date-time
     * @throws DateTimeParseException if the text to parse is invalid
     * @see LocalDateTime#parse(java.lang.CharSequence)
     */
    public static LocalDateTime toLocalDateTime(CharSequence cs) throws DateTimeParseException {
        return toLocalDateTime(cs, Optional.empty());
    }

    /**
     * @param cs the CharSequence
     * @param patternOpt
     * @return the parsed date-time
     * @throws DateTimeParseException if the text to parse is invalid
     * @see LocalDateTime#parse(java.lang.CharSequence, java.time.format.DateTimeFormatter)
     */
    public static LocalDateTime toLocalDateTime(CharSequence cs, @NonNull Optional<String> patternOpt) throws DateTimeParseException {
        if (cs == null) {
            return null;
        }
        return patternOpt.map(pattern -> LocalDateTime.parse(cs, DateTimeFormatter.ofPattern(pattern)))
                .orElse(LocalDateTime.parse(cs));
    }

    /**
     * @param cs the CharSequence
     * @return the parsed date
     * @throws DateTimeParseException if the text to parse is invalid
     * @see OffsetDateTime#parse(java.lang.CharSequence)
     */
    public static Date toDate(CharSequence cs) throws DateTimeParseException {
        return toDate(cs, Optional.empty(), Optional.empty());
    }

    /**
     * @param cs the CharSequence
     * @param patternOpt
     * @return the parsed date
     * @throws DateTimeParseException if the text to parse is invalid
     * @see OffsetDateTime#parse(java.lang.CharSequence, java.time.format.DateTimeFormatter)
     */
    public static Date toDate(CharSequence cs, @NonNull Optional<String> patternOpt) throws IllegalArgumentException {
        return toDate(cs, patternOpt, Optional.empty());
    }

    /**
     * @param cs the CharSequence
     * @param patternOpt
     * @param zoOpt
     * @return the parsed date
     * @throws DateTimeParseException if the text to parse is invalid
     * @see OffsetDateTime#parse(java.lang.CharSequence)
     */
    public static Date toDate(CharSequence cs, @NonNull Optional<String> patternOpt, @NonNull Optional<ZoneOffset> zoOpt) throws IllegalArgumentException {
        if (cs == null) {
            return null;
        }
        ZoneOffset zo = zoOpt.orElse(ZoneOffset.UTC);
        return Date.from(toLocalDateTime(cs, patternOpt).toInstant(zo));
    }

    /**
     * @param cs the CharSequence
     * @return the DateFormat
     * @see SimpleDateFormat#SimpleDateFormat(java.lang.String)
     */
    public static DateFormat toDateFormat(CharSequence cs) {
        return toDateFormat(cs, Optional.empty());
    }

    /**
     * @param cs the CharSequence
     * @param localeOpt
     * @return the DateFormat
     * @see SimpleDateFormat#SimpleDateFormat(java.lang.String, java.util.Locale)
     */
    public static DateFormat toDateFormat(CharSequence cs, @NonNull Optional<Locale> localeOpt) {
        if (cs == null) {
            return null;
        }
        return localeOpt.map(locale -> new SimpleDateFormat(cs.toString(), locale))
                .orElse(new SimpleDateFormat(cs.toString()));
    }

    /**
     * @param cs the CharSequence
     * @return the DateFormat
     * @throws IllegalArgumentException if the pattern is invalid
     * @see DateTimeFormatter#ofPattern(java.lang.String)
     */
    public static DateTimeFormatter toDateTimeFormatter(CharSequence cs) throws IllegalArgumentException {
        return toDateTimeFormatter(cs, Optional.empty());
    }

    /**
     * @param cs the CharSequence
     * @param localeOpt
     * @return the DateFormat
     * @throws IllegalArgumentException if the pattern is invalid
     * @see DateTimeFormatter#ofPattern(java.lang.String, java.util.Locale)
     */
    public static DateTimeFormatter toDateTimeFormatter(CharSequence cs, @NonNull Optional<Locale> localeOpt) throws IllegalArgumentException {
        if (cs == null) {
            return null;
        }
        return localeOpt.map(locale -> DateTimeFormatter.ofPattern(cs.toString(), locale))
                .orElse(DateTimeFormatter.ofPattern(cs.toString()));
    }

    /**
     * @param language
     * @return the Locale
     * @see Locale#Locale(String)
     */
    public static Locale toLocale(CharSequence language) {
        return toLocale(language, Optional.empty(), Optional.empty());
    }

    /**
     * @param language
     * @param countryOpt
     * @return the Locale
     * @see Locale#Locale(String, String)
     */
    public static Locale toLocale(CharSequence language, @NonNull Optional<String> countryOpt) {
        return toLocale(language, countryOpt, Optional.empty());
    }

    /**
     * @param language
     * @param countryOpt
     * @param variantOpt
     * @return the Locale
     * @see Locale#Locale(String, String, String)
     */
    public static Locale toLocale(CharSequence language, @NonNull Optional<String> countryOpt, @NonNull Optional<String> variantOpt) {
        if (language == null) {
            return null;
        }
        return countryOpt.map(country -> 
                variantOpt.map(variant -> new Locale(language.toString(), country, variant))
                .orElse(new Locale(language.toString(), country)))
                .orElse(new Locale(language.toString()));
    }

    /**
     * @param cs the CharSequence
     * @return the TimeZone
     * @see TimeZone#getTimeZone(String)
     */
    public static TimeZone toTimeZone(CharSequence cs) {
        if (cs == null) {
            return null;
        }
        return TimeZone.getTimeZone(cs.toString());
    }

    public static Optional<Provider> toSecurityProvider(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(Security.getProvider(name));
    }
    
    /**
     * @param algorithm
     * @return the MessageDigest
     * @throws IllegalArgumentException
     * @see MessageDigest#getInstance(String)
     */
    public static MessageDigest toMessageDigest(CharSequence algorithm) throws IllegalArgumentException {
        return toMessageDigest(algorithm, Optional.empty());
    }

    /**
     * @param algorithm
     * @param providerOpt
     * @return the MessageDigest
     * @throws IllegalArgumentException
     * @see MessageDigest#getInstance(String, Provider)
     */
    public static MessageDigest toMessageDigest(CharSequence algorithm, @NonNull Optional<Provider> providerOpt) throws IllegalArgumentException {
        if (algorithm == null) {
            return null;
        }
        try {
            if (providerOpt.isPresent()) {
                return MessageDigest.getInstance(algorithm.toString(), providerOpt.get());
            }
            return MessageDigest.getInstance(algorithm.toString());
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
        return toKeyFactory(algorithm, Optional.empty());
    }

    /**
     * @param algorithm
     * @param providerOpt
     * @return the KeyFactory
     * @throws IllegalArgumentException
     * @see KeyFactory#getInstance(String, Provider)
     */
    public static KeyFactory toKeyFactory(CharSequence algorithm, @NonNull Optional<Provider> providerOpt) throws IllegalArgumentException {
        if (algorithm == null) {
            return null;
        }
        try {
            if (providerOpt.isPresent()) {
                return KeyFactory.getInstance(algorithm.toString(), providerOpt.get());
            }
            return KeyFactory.getInstance(algorithm.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param cs the CharSequence
     * @return the KeyStore
     * @throws IllegalArgumentException
     */
    public static KeyStore toKeyStore(CharSequence cs) throws IllegalArgumentException {
        return toKeyStore(cs, Optional.empty());
    }

    /**
     * @param cs the CharSequence
     * @param providerOpt
     * @return the KeyStore
     * @throws IllegalArgumentException
     */
    public static KeyStore toKeyStore(CharSequence cs, @NonNull Optional<Provider> providerOpt) throws IllegalArgumentException {
        if (cs == null) {
            return null;
        }
        try {
            if (providerOpt.isPresent()) {
                return KeyStore.getInstance(cs.toString(), providerOpt.get());
            }
            return KeyStore.getInstance(cs.toString());
        } catch (KeyStoreException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param cs the CharSequence
     * @return the Signature
     * @throws IllegalArgumentException
     */
    public static Signature toSignature(CharSequence cs) throws IllegalArgumentException {
        return toSignature(cs, Optional.empty());
    }

    /**
     * @param cs the CharSequence
     * @param providerOpt
     * @return the Signature
     * @throws IllegalArgumentException
     */
    public static Signature toSignature(CharSequence cs, @NonNull Optional<Provider> providerOpt) throws IllegalArgumentException {
        if (cs == null) {
            return null;
        }
        try {
            if (providerOpt.isPresent()) {
                return Signature.getInstance(cs.toString(), providerOpt.get());
            }
            return Signature.getInstance(cs.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param cs the CharSequence
     * @return the SecureRandom
     * @throws IllegalArgumentException
     */
    public static SecureRandom toSecureRandom(CharSequence cs) throws IllegalArgumentException {
        return toSecureRandom(cs, Optional.empty());
    }

    /**
     * @param cs the CharSequence
     * @param providerOpt
     * @return the SecureRandom
     * @throws IllegalArgumentException
     */
    public static SecureRandom toSecureRandom(CharSequence cs, @NonNull Optional<Provider> providerOpt) throws IllegalArgumentException {
        if (cs == null) {
            return null;
        }
        try {
            if (providerOpt.isPresent()) {
                return SecureRandom.getInstance(cs.toString(), providerOpt.get());
            }
            return SecureRandom.getInstance(cs.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param cs the CharSequence
     * @return the KeyPairGenerator
     * @throws IllegalArgumentException
     */
    public static KeyPairGenerator toKeyPairGenerator(CharSequence cs) throws IllegalArgumentException {
        return toKeyPairGenerator(cs, Optional.empty());
    }

    /**
     * @param cs the CharSequence
     * @param providerOpt
     * @return the KeyPairGenerator
     * @throws IllegalArgumentException
     */
    public static KeyPairGenerator toKeyPairGenerator(CharSequence cs, @NonNull Optional<Provider> providerOpt) throws IllegalArgumentException {
        if (cs == null) {
            return null;
        }
        try {
            if (providerOpt.isPresent()) {
                return KeyPairGenerator.getInstance(cs.toString(), providerOpt.get());
            }
            return KeyPairGenerator.getInstance(cs.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param cs the CharSequence
     * @return the AlgorithmParameters
     * @throws IllegalArgumentException
     */
    public static AlgorithmParameters toAlgorithmParameters(CharSequence cs) throws IllegalArgumentException {
        return toAlgorithmParameters(cs, Optional.empty());
    }

    /**
     * @param cs the CharSequence
     * @param providerOpt
     * @return the AlgorithmParameters
     * @throws IllegalArgumentException
     */
    public static AlgorithmParameters toAlgorithmParameters(CharSequence cs, @NonNull Optional<Provider> providerOpt) throws IllegalArgumentException {
        if (cs == null) {
            return null;
        }
        try {
            if (providerOpt.isPresent()) {
                return AlgorithmParameters.getInstance(cs.toString(), providerOpt.get());
            }
            return AlgorithmParameters.getInstance(cs.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param cs the CharSequence
     * @return the AlgorithmParameterGenerator
     * @throws IllegalArgumentException
     */
    public static AlgorithmParameterGenerator toAlgorithmParameterGenerator(CharSequence cs) throws IllegalArgumentException {
        return toAlgorithmParameterGenerator(cs, Optional.empty());
    }

    /**
     * @param cs the CharSequence
     * @param providerOpt
     * @return the AlgorithmParameterGenerator
     * @throws IllegalArgumentException
     */
    public static AlgorithmParameterGenerator toAlgorithmParameterGenerator(CharSequence cs, @NonNull Optional<Provider> providerOpt) throws IllegalArgumentException {
        if (cs == null) {
            return null;
        }
        try {
            if (providerOpt.isPresent()) {
                return AlgorithmParameterGenerator.getInstance(cs.toString(), providerOpt.get());
            }
            return AlgorithmParameterGenerator.getInstance(cs.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param cs the CharSequence
     * @param params
     * @return the Policy
     * @throws IllegalArgumentException
     */
    public static Policy toPolicy(CharSequence cs, Policy.Parameters params) throws IllegalArgumentException {
        return toPolicy(cs, params, Optional.empty());
    }

    /**
     * @param cs the CharSequence
     * @param params
     * @param providerOpt
     * @return the Policy
     * @throws IllegalArgumentException
     */
    public static Policy toPolicy(CharSequence cs, Policy.Parameters params, @NonNull Optional<Provider> providerOpt) throws IllegalArgumentException {
        if (cs == null) {
            return null;
        }
        try {
            if (providerOpt.isPresent()) {
                return Policy.getInstance(cs.toString(), params, providerOpt.get());
            }
            return Policy.getInstance(cs.toString(), params);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param cs the CharSequence
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

    /**
     * @param cs transformation
     * @return the Cipher
     * @throws IllegalArgumentException
     */
    public static Cipher toCipher(CharSequence cs) throws IllegalArgumentException {
        return toCipher(cs, Optional.empty());
    }

    /**
     * @param cs transformation
     * @param providerOpt
     * @return the Cipher
     * @throws IllegalArgumentException
     */
    public static Cipher toCipher(CharSequence cs, @NonNull Optional<Provider> providerOpt) throws IllegalArgumentException {
        if (cs == null) {
            return null;
        }
        try {
            if (providerOpt.isPresent()) {
                return Cipher.getInstance(cs.toString(), providerOpt.get());
            }
            return Cipher.getInstance(cs.toString());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param cs algorithm
     * @return the KeyAgreement
     * @throws IllegalArgumentException
     */
    public static KeyAgreement toKeyAgreement(CharSequence cs) throws IllegalArgumentException {
        return toKeyAgreement(cs, Optional.empty());
    }
    /**
     * @param cs algorithm
     * @param providerOpt
     * @return the KeyAgreement
     * @throws IllegalArgumentException
     */
    public static KeyAgreement toKeyAgreement(CharSequence cs, @NonNull Optional<Provider> providerOpt) throws IllegalArgumentException {
        if (cs == null) {
            return null;
        }
        try {
            if (providerOpt.isPresent()) {
                return KeyAgreement.getInstance(cs.toString(), providerOpt.get());
            }
            return KeyAgreement.getInstance(cs.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param cs algorithm
     * @return the KeyGenerator
     * @throws IllegalArgumentException
     */
    public static KeyGenerator toKeyGenerator(CharSequence cs) throws IllegalArgumentException {
        return toKeyGenerator(cs, Optional.empty());
    }

    /**
     * @param cs algorithm
     * @param providerOpt
     * @return the KeyGenerator
     * @throws IllegalArgumentException
     */
    public static KeyGenerator toKeyGenerator(CharSequence cs, @NonNull Optional<Provider> providerOpt) throws IllegalArgumentException {
        if (cs == null) {
            return null;
        }
        try {
            if (providerOpt.isPresent()) {
                return KeyGenerator.getInstance(cs.toString(), providerOpt.get());
            }
            return KeyGenerator.getInstance(cs.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param cs algorithm
     * @return the Mac
     * @throws IllegalArgumentException
     */
    public static Mac toMAC(CharSequence cs) throws IllegalArgumentException {
        return toMAC(cs, Optional.empty());
    }

    /**
     * @param cs algorithm
     * @param providerOpt
     * @return the Mac
     * @throws IllegalArgumentException
     */
    public static Mac toMAC(CharSequence cs, @NonNull Optional<Provider> providerOpt) throws IllegalArgumentException {
        if (cs == null) {
            return null;
        }
        try {
            if (providerOpt.isPresent()) {
                return Mac.getInstance(cs.toString(), providerOpt.get());
            }
            return Mac.getInstance(cs.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param cs algorithm
     * @return the SecretKeyFactory
     * @throws IllegalArgumentException
     */
    public static SecretKeyFactory toSecretKeyFactory(CharSequence cs) throws IllegalArgumentException {
        return toSecretKeyFactory(cs, Optional.empty());
    }

    /**
     * @param cs algorithm
     * @param providerOpt
     * @return the SecretKeyFactory
     * @throws IllegalArgumentException
     */
    public static SecretKeyFactory toSecretKeyFactory(CharSequence cs, @NonNull Optional<Provider> providerOpt) throws IllegalArgumentException {
        if (cs == null) {
            return null;
        }
        try {
            if (providerOpt.isPresent()) {
                return SecretKeyFactory.getInstance(cs.toString(), providerOpt.get());
            }
            return SecretKeyFactory.getInstance(cs.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
