package com.github.emalock3.common.extension;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Policy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;
import java.util.regex.Pattern;

import lombok.experimental.ExtensionMethod;

import org.junit.Test;

@ExtensionMethod({StringExtensions.class})
public class StringExtensionsTest {

    @Test
    public void testS() {
        assertThat(((String) null).s(), is(nullValue()));
        assertThat("".s(), is(""));
        assertThat("hoge".s(), is("hoge"));
    }

    @Test
    public void testFmtCharSequenceObjectArray() {
        assertThat(((String) null).fmt(), is(nullValue()));
        assertThat("".fmt(), is(""));
        assertThat("%s, World!".fmt("Hello"), is("Hello, World!"));
        assertThat("%s, %d".fmt("hoge", Integer.valueOf(123)), is("hoge, 123"));
		// java.lang.VerifyError: Bad type on operand stack
        // Reason: Type integer (current frame, stack[4]) is not assignable to 'java/lang/Object'
        // This only happens in eclipse
        // assertThat("%s, %d".fmt("hoge", 123), is("hoge, 123"));
    }

    @Test
    public void testFmtCharSequenceLocaleObjectArray() {
        assertThat(((String) null).fmt(Optional.of(Locale.JAPAN)), is(nullValue()));
        assertThat("".fmt(Optional.of(Locale.US)), is(""));
        assertThat("%d %ta".fmt(Optional.of(Locale.US), Integer.valueOf(123), Date.from(LocalDate.parse("2014-03-11").atTime(LocalTime.MIN).toInstant(ZoneOffset.UTC))), is("123 Tue"));
    }

    @Test
    public void testEncodeURLCharSequence() {
        assertThat(((String) null).encodeURL(), is(nullValue()));
        assertThat("".encodeURL(), is(""));
        assertThat("a b".encodeURL(), is("a+b"));
    }

    @Test(expected = NullPointerException.class)
    public void testEncodeURLCharSequenceCharset() {
        Optional<Charset> utf8 = Optional.of(Charset.forName("UTF-8"));
        assertThat(((String) null).encodeURL(utf8), is(nullValue()));
        assertThat("".encodeURL(utf8), is(""));
        assertThat("c d".encodeURL(utf8), is("c+d"));
        "".encodeURL(null);
    }

    @Test
    public void testDecodeURLCharSequence() {
        assertThat(((String) null).decodeURL(), is(nullValue()));
        assertThat("".decodeURL(), is(""));
        assertThat("e+f".decodeURL(), is("e f"));
    }

    @Test(expected = NullPointerException.class)
    public void testDecodeURLCharSequenceCharset() {
        Optional<Charset> utf8 = Optional.of(Charset.forName("UTF-8"));
        assertThat(((String) null).decodeURL(utf8), is(nullValue()));
        assertThat("".decodeURL(utf8), is(""));
        assertThat("e+f".decodeURL(utf8), is("e f"));
        "".decodeURL(null);
    }

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private String readInputStream(InputStream in) throws IOException {
        try {
            StringBuilder sb = new StringBuilder();
            byte[] bytes = new byte[1024];
            for (int len = 0; (len = in.read(bytes)) != -1;) {
                sb.append(new String(bytes, 0, len, UTF8));
            }
            return sb.toString();
        } finally {
            in.close();
        }
    }

    @Test
    public void testToInputStreamCharSequence() throws IOException {
        assertThat(((String) null).toInputStream(), is(nullValue()));
        assertThat(readInputStream("".toInputStream()), is(""));
        assertThat(readInputStream("hoge".toInputStream()), is("hoge"));
    }

    @Test(expected = NullPointerException.class)
    public void testToInputStreamCharSequenceCharset() throws IOException {
        Optional<Charset> utf8 = Optional.of(Charset.forName("UTF-8"));
        assertThat(((String) null).toInputStream(utf8), is(nullValue()));
        assertThat(readInputStream("".toInputStream(utf8)), is(""));
        assertThat(readInputStream("hoge".toInputStream(utf8)), is("hoge"));
        "aaa".toInputStream(null);
    }

    private String readerToString(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] cb = new char[1024];
        for (int len = 0; (len = reader.read(cb)) != -1;) {
            sb.append(cb, 0, len);
        }
        return sb.toString();
    }

    @Test
    public void testToReader() throws IOException {
        assertThat(((String) null).toReader(), is(nullValue()));
        assertThat(readerToString("".toReader()), is(""));
        assertThat(readerToString("hoge".toReader()), is("hoge"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToURL() throws MalformedURLException {
        assertThat(((String) null).toURL(), is(nullValue()));
        assertThat("http://test.test/".toURL(), is(new URL("http://test.test/")));
        "".toURL();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToURI() throws URISyntaxException {
        assertThat(((String) null).toURI(), is(nullValue()));
        assertThat("file://.".toURI(), is(new URI("file://.")));
        ":".toURI();
    }

    @Test
    public void testToFile() {
        assertThat(((String) null).toFile(), is(nullValue()));
        assertThat(".".toFile(), is(new File(".")));
    }

    @Test
    public void testToPath() {
        assertThat(((String) null).toPath(), is(nullValue()));
        assertThat(".".toPath(), is(new File(".").toPath()));
    }

    @Test
    public void testToPatternCharSequence() {
        assertThat(((String) null).toPattern(), is(nullValue()));
        assertThat("".toPattern().toString(), is(Pattern.compile("").toString()));
        assertThat("\\d".toPattern().toString(), is(Pattern.compile("\\d").toString()));
    }

    @Test
    public void testToPatternCharSequenceInt() {
        assertThat(((String) null).toPattern(0), is(nullValue()));
        assertThat("".toPattern(0).toString(), is(Pattern.compile("", 0).toString()));
        assertThat("\\d".toPattern(0).toString(), is(Pattern.compile("\\d", 0).toString()));
    }

    @Test(expected = NumberFormatException.class)
    public void testToByteCharSequence() {
        assertThat(((String) null).toByte(), is((byte) 0));
        assertThat("10".toByte(), is((byte) 10));
        "abc".toByte();
    }

    @Test(expected = NumberFormatException.class)
    public void testToByteCharSequenceInt() {
        assertThat(((String) null).toByte(10), is((byte) 0));
        assertThat("10".toByte(10), is((byte) 10));
        assertThat("1A".toByte(16), is((byte) 0x1A));
        "abc".toByte(10);
    }

    @Test
    public void testToByteCharSequenceIntByte() {
        assertThat(((String) null).toByte(10, (byte) 0), is((byte) 0));
        assertThat("10".toByte(10, (byte) 0), is((byte) 10));
        assertThat("1A".toByte(16, (byte) 0), is((byte) 0x1A));
        assertThat("abc".toByte(10, (byte) 0), is((byte) 0));
        assertThat("abc".toByte(10, (byte) -1), is((byte) -1));
    }

    @Test
    public void testToByteObjectCharSequence() {
        assertThat(((String) null).toByteObject(), is(nullValue()));
        assertThat("12".toByteObject(), is(Byte.valueOf((byte) 12)));
        assertThat("abc".toByteObject(), is(nullValue()));
    }

    @Test
    public void testToByteObjectCharSequenceInt() {
        assertThat(((String) null).toByteObject(10), is(nullValue()));
        assertThat("12".toByteObject(10), is(Byte.valueOf((byte) 12)));
        assertThat("2b".toByteObject(16), is(Byte.valueOf((byte) 0x2B)));
        assertThat("abc".toByteObject(10), is(nullValue()));
    }

    @Test(expected = NumberFormatException.class)
    public void testToShortCharSequence() {
        assertThat(((String) null).toShort(), is((short) 0));
        assertThat("10".toShort(), is((short) 10));
        "abc".toShort();
    }

    @Test(expected = NumberFormatException.class)
    public void testToShortCharSequenceInt() {
        assertThat(((String) null).toShort(10), is((short) 0));
        assertThat("10".toShort(10), is((short) 10));
        assertThat("1A".toShort(16), is((short) 0x1A));
        "abc".toShort(10);
    }

    @Test
    public void testToShortCharSequenceIntShort() {
        assertThat(((String) null).toShort(10, (short) 0), is((short) 0));
        assertThat("10".toShort(10, (short) 0), is((short) 10));
        assertThat("1A".toShort(16, (short) 0), is((short) 0x1A));
        assertThat("abc".toShort(10, (short) 0), is((short) 0));
        assertThat("abc".toShort(10, (short) -1), is((short) -1));
    }

    @Test
    public void testToShortObjectCharSequence() {
        assertThat(((String) null).toShortObject(), is(nullValue()));
        assertThat("12".toShortObject(), is(Short.valueOf((short) 12)));
        assertThat("abc".toShortObject(), is(nullValue()));
    }

    @Test
    public void testToShortObjectCharSequenceInt() {
        assertThat(((String) null).toShortObject(10), is(nullValue()));
        assertThat("12".toShortObject(10), is(Short.valueOf((short) 12)));
        assertThat("2b".toShortObject(16), is(Short.valueOf((short) 0x2B)));
        assertThat("abc".toShortObject(10), is(nullValue()));
    }

    @Test(expected = NumberFormatException.class)
    public void testToIntCharSequence() {
        assertThat(((String) null).toInt(), is(0));
        assertThat("10".toInt(), is(10));
        "abc".toInt();
    }

    @Test(expected = NumberFormatException.class)
    public void testToIntCharSequenceInt() {
        assertThat(((String) null).toInt(10), is(0));
        assertThat("10".toInt(10), is(10));
        assertThat("1A".toInt(16), is(0x1A));
        "abc".toInt(10);
    }

    @Test
    public void testToIntCharSequenceIntInt() {
        assertThat(((String) null).toInt(10, 0), is(0));
        assertThat("10".toInt(10, 0), is(10));
        assertThat("1A".toInt(16, 0), is(0x1A));
        assertThat("abc".toInt(10, 0), is(0));
        assertThat("abc".toInt(10, -1), is(-1));
    }

    @Test
    public void testToIntObjectCharSequence() {
        assertThat(((String) null).toIntObject(), is(nullValue()));
        assertThat("12".toIntObject(), is(Integer.valueOf(12)));
        assertThat("abc".toIntObject(), is(nullValue()));
    }

    @Test
    public void testToIntObjectCharSequenceInt() {
        assertThat(((String) null).toIntObject(10), is(nullValue()));
        assertThat("12".toIntObject(10), is(Integer.valueOf(12)));
        assertThat("2b".toIntObject(16), is(Integer.valueOf(0x2B)));
        assertThat("abc".toIntObject(10), is(nullValue()));
    }

    @Test(expected = NumberFormatException.class)
    public void testToLongCharSequence() {
        assertThat(((String) null).toLong(), is(0L));
        assertThat("10".toLong(), is(10L));
        "abc".toLong();
    }

    @Test(expected = NumberFormatException.class)
    public void testToLongCharSequenceInt() {
        assertThat(((String) null).toLong(10), is(0L));
        assertThat("10".toLong(10), is(10L));
        assertThat("1A".toLong(16), is(0x1AL));
        "abc".toLong(10);
    }

    @Test
    public void testToLongCharSequenceIntLong() {
        assertThat(((String) null).toLong(10, 0L), is(0L));
        assertThat("10".toLong(10, 0L), is(10L));
        assertThat("1A".toLong(16, 0L), is(0x1AL));
        assertThat("abc".toLong(10, 0L), is(0L));
        assertThat("abc".toLong(10, -1L), is(-1L));
    }

    @Test
    public void testToLongObjectCharSequence() {
        assertThat(((String) null).toLongObject(), is(nullValue()));
        assertThat("12".toLongObject(), is(Long.valueOf(12L)));
        assertThat("abc".toLongObject(), is(nullValue()));
    }

    @Test
    public void testToLongObjectCharSequenceInt() {
        assertThat(((String) null).toLongObject(10), is(nullValue()));
        assertThat("12".toLongObject(10), is(Long.valueOf(12L)));
        assertThat("2b".toLongObject(16), is(Long.valueOf(0x2BL)));
        assertThat("abc".toLongObject(10), is(nullValue()));
    }

    @Test
    public void testToBigInteger() {
        assertThat(((String) null).toBigInteger(), is(nullValue()));
        assertThat("10".toBigInteger(), is(new BigInteger("10")));
        assertThat("abc".toBigInteger(), is(nullValue()));
    }

    @Test(expected = NumberFormatException.class)
    public void testToFloatCharSequence() {
        assertThat(((String) null).toFloat(), is(0f));
        assertThat("10.0".toFloat(), is(10.0f));
        "abc".toFloat();
    }

    @Test
    public void testToFloatCharSequenceFloat() {
        assertThat(((String) null).toFloat(0f), is(0f));
        assertThat("10".toFloat(0f), is(10f));
        assertThat("abc".toFloat(0f), is(0f));
        assertThat("abc".toFloat(-1f), is(-1f));
    }

    @Test
    public void testToFloatObject() {
        assertThat(((String) null).toFloatObject(), is(nullValue()));
        assertThat("12".toFloatObject(), is(Float.valueOf(12f)));
        assertThat("abc".toFloatObject(), is(nullValue()));
    }

    @Test(expected = NumberFormatException.class)
    public void testToDoubleCharSequence() {
        assertThat(((String) null).toDouble(), is(0.0));
        assertThat("10.0".toDouble(), is(10.0));
        "abc".toFloat();
    }

    @Test
    public void testToDoubleCharSequenceDouble() {
        assertThat(((String) null).toDouble(0.0), is(0.0));
        assertThat("10".toDouble(0.0), is(10.0));
        assertThat("abc".toDouble(0.0), is(0.0));
        assertThat("abc".toDouble(-1.0), is(-1.0));
    }

    @Test
    public void testToDoubleObject() {
        assertThat(((String) null).toDoubleObject(), is(nullValue()));
        assertThat("12".toDoubleObject(), is(Double.valueOf(12)));
        assertThat("abc".toDoubleObject(), is(nullValue()));
    }

    @Test
    public void testToBigDecimal() {
        assertThat(((String) null).toBigDecimal(), is(nullValue()));
        assertThat("12".toBigDecimal(), is(new BigDecimal("12")));
        assertThat("abc".toBigDecimal(), is(nullValue()));
    }

    @Test
    public void testToCharBuffer() {
        assertThat(((String) null).toCharBuffer(), is(nullValue()));
        assertThat("".toCharBuffer().toString(), is(""));
        assertThat("hoge".toCharBuffer().toString(), is("hoge"));
    }

    @Test
    public void testToByteBufferCharSequence() {
        assertThat(((String) null).toByteBuffer(), is(nullValue()));
        assertThat("".toByteBuffer().limit(), is(0));
        assertThat("hoge".toByteBuffer().limit(), is(4));
    }

    @Test
    public void testToByteBufferCharSequenceCharset() {
        Optional<Charset> utf8 = Optional.of(Charset.forName("UTF-8"));
        assertThat(((String) null).toByteBuffer(utf8), is(nullValue()));
        assertThat("".toByteBuffer(utf8).limit(), is(0));
        assertThat("hoge".toByteBuffer(utf8).limit(), is(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToInetAddress() throws IllegalArgumentException, UnknownHostException {
        assertThat(((String) null).toInetAddress(), is(nullValue()));
        assertThat("localhost".toInetAddress(), is(InetAddress.getByName("localhost")));
        "333.444.555.666".toInetAddress();
    }

    @Test(expected = DateTimeParseException.class)
    public void testToDateTimeCharSequence() {
        assertThat(((String) null).toLocalDateTime(), is(nullValue()));
        assertThat("2014-03-11".toLocalDateTime(), is(LocalDateTime.parse("2014-03-11")));
        assertThat("2014-03-11T14:32:14".toLocalDateTime(), is(LocalDateTime.parse("2014-03-11T14:32:14")));
        "hoge".toLocalDateTime();
    }

    @Test(expected = DateTimeParseException.class)
    public void testToDateTimeCharSequenceString() {
        assertThat(((String) null).toLocalDateTime(Optional.of("yyyyMMdd")), is(nullValue()));
        assertThat("20140311".toLocalDateTime(Optional.of("yyyyMMdd")), is(LocalDateTime.parse("2014-03-11")));
        assertThat("2014/03/11 14:32:14".toLocalDateTime(Optional.of("yyyy/MM/dd HH:mm:ss")), is(LocalDateTime.parse("2014-03-11T14:32:14")));
        "hoge".toLocalDateTime(Optional.of("yyyy-MM-dd"));
    }

    @Test(expected = DateTimeParseException.class)
    public void testToDateCharSequence() {
        assertThat(((String) null).toDate(), is(nullValue()));
        assertThat("2014-03-11".toDate(), is(new Date(OffsetDateTime.parse("2014-03-11").toInstant().toEpochMilli())));
        assertThat("2014-03-11T14:32:14".toDate(), is(new Date(OffsetDateTime.parse("2014-03-11T14:32:14").toInstant().toEpochMilli())));
        "hoge".toDate();
    }

    @Test(expected = DateTimeParseException.class)
    public void testToDateCharSequenceString() {
        assertThat(((String) null).toDate(Optional.of("yyyyMMdd")), is(nullValue()));
//		assertThat("20140311".toDate("yyyyMMdd"), is(new Date(OffsetDateTime.parse("2014-03-11").toInstant().toEpochMilli())));
        assertThat("2014/03/11 14:32:14".toDate(Optional.of("yyyy/MM/dd HH:mm:ss")), is(Date.from(LocalDateTime.parse("2014-03-11T14:32:14").toInstant(ZoneOffset.UTC))));
        "hoge".toDate(Optional.of("yyyy-MM-dd"));
    }

    @Test
    public void testToDateFormatCharSequence() {
        assertThat(((String) null).toDateFormat(), is(nullValue()));
        assertThat("yyyy".toDateFormat(), is((DateFormat) new SimpleDateFormat("yyyy")));
    }

    @Test
    public void testToDateFormatCharSequenceLocale() {
        assertThat(((String) null).toDateFormat(Optional.of(Locale.US)), is(nullValue()));
        assertThat("yyyy".toDateFormat(Optional.of(Locale.US)), is((DateFormat) new SimpleDateFormat("yyyy", Locale.US)));
    }

    @Test
    public void testToLocaleCharSequence() {
        assertThat(((String) null).toLocale(), is(nullValue()));
        assertThat("ENGLISH".toLocale(), is(new Locale("ENGLISH")));
    }

    @Test
    public void testToLocaleCharSequenceString() {
        assertThat(((String) null).toLocale(Optional.of("US")), is(nullValue()));
        assertThat("ENGLISH".toLocale(Optional.of("US")), is(new Locale("ENGLISH", "US")));
    }

    @Test
    public void testToLocaleCharSequenceStringString() {
        assertThat(((String) null).toLocale(Optional.of("US"), Optional.of("")), is(nullValue()));
        assertThat("ENGLISH".toLocale(Optional.of("US"), Optional.of("")), is(new Locale("ENGLISH", "US", "")));
    }

    @Test
    public void testToTimeZone() {
        assertThat(((String) null).toTimeZone(), is(nullValue()));
        assertThat("GMT".toTimeZone(), is(TimeZone.getTimeZone("GMT")));
        assertThat("+09:00".toTimeZone(), is(TimeZone.getTimeZone("+09:00")));
        assertThat("JAPAN/Tokyo".toTimeZone(), is(TimeZone.getTimeZone("JAPAN/Tokyo")));
    }

    @Test
    public void testToMessageDigestCharSequence() throws IllegalArgumentException, NoSuchAlgorithmException {
        assertThat(((String) null).toMessageDigest(), is(nullValue()));
        assertThat("SHA-1".toMessageDigest().getAlgorithm(), is(MessageDigest.getInstance("SHA-1").getAlgorithm()));
    }

    @Test
    public void testToMessageDigestCharSequenceProvider() {
        assertThat(((String) null).toMessageDigest(Optional.empty()), is(nullValue()));
    }

    @Test
    public void testToKeyFactoryCharSequence() {
        assertThat(((String) null).toKeyFactory(), is(nullValue()));
    }

    @Test
    public void testToKeyFactoryCharSequenceProvider() {
        assertThat(((String) null).toKeyFactory(Optional.empty()), is(nullValue()));
    }

    @Test
    public void testToKeyStoreCharSequence() {
        assertThat(((String) null).toKeyStore(), is(nullValue()));
    }

    @Test
    public void testToKeyStoreCharSequenceProvider() {
        assertThat(((String) null).toKeyStore(Optional.empty()), is(nullValue()));
    }

    @Test
    public void testToSignatureCharSequence() {
        assertThat(((String) null).toSignature(), is(nullValue()));
    }

    @Test
    public void testToSignatureCharSequenceProvider() {
        assertThat(((String) null).toSignature(Optional.empty()), is(nullValue()));
    }

    @Test
    public void testToSecureRandomCharSequence() {
        assertThat(((String) null).toSecureRandom(), is(nullValue()));
    }

    @Test
    public void testToSecureRandomCharSequenceProvider() {
        assertThat(((String) null).toSecureRandom(Optional.empty()), is(nullValue()));
    }

    @Test
    public void testToKeyPairGeneratorCharSequence() {
        assertThat(((String) null).toKeyPairGenerator(), is(nullValue()));
    }

    @Test
    public void testToKeyPairGeneratorCharSequenceProvider() {
        assertThat(((String) null).toKeyPairGenerator(Optional.empty()), is(nullValue()));
    }

    @Test
    public void testToAlgorithmParametersCharSequence() {
        assertThat(((String) null).toAlgorithmParameters(), is(nullValue()));
    }

    @Test
    public void testToAlgorithmParametersCharSequenceProvider() {
        assertThat(((String) null).toAlgorithmParameters(Optional.empty()), is(nullValue()));
    }

    @Test
    public void testToAlgorithmParameterGeneratorCharSequence() {
        assertThat(((String) null).toAlgorithmParameterGenerator(), is(nullValue()));
    }

    @Test
    public void testToAlgorithmParameterGeneratorCharSequenceProvider() {
        assertThat(((String) null).toAlgorithmParameterGenerator(Optional.empty()), is(nullValue()));
    }

    @Test
    public void testToPolicyCharSequenceParameters() {
        assertThat(((String) null).toPolicy(null), is(nullValue()));
    }

    @Test
    public void testToPolicyCharSequenceParametersProvider() {
        assertThat(((String) null).toPolicy((Policy.Parameters) null, Optional.empty()), is(nullValue()));
    }

    @Test
    public void testToCharset() {
        assertThat(((String) null).toCharset(), is(nullValue()));
        assertThat("UTF-8".toCharset(), is(Charset.forName("UTF-8")));
    }

    @Test
    public void testToCipherCharSequenceParameters() {
        assertThat(((String) null).toCipher(), is(nullValue()));
    }

    @Test
    public void testToCipherCharSequenceParametersProvider() {
        assertThat(((String) null).toCipher(Optional.empty()), is(nullValue()));
    }

    @Test
    public void testToKeyAgreementCharSequenceParameters() {
        assertThat(((String) null).toKeyAgreement(), is(nullValue()));
    }

    @Test
    public void testToKeyAgreementCharSequenceParametersProvider() {
        assertThat(((String) null).toKeyAgreement(Optional.empty()), is(nullValue()));
    }

    @Test
    public void testToKeyGeneratorCharSequenceParameters() {
        assertThat(((String) null).toKeyGenerator(), is(nullValue()));
    }

    @Test
    public void testToKeyGeneratorCharSequenceParametersProvider() {
        assertThat(((String) null).toKeyGenerator(Optional.empty()), is(nullValue()));
    }

    @Test
    public void testToMACCharSequenceParameters() {
        assertThat(((String) null).toMAC(), is(nullValue()));
    }

    @Test
    public void testToMACCharSequenceParametersProvider() {
        assertThat(((String) null).toMAC(Optional.empty()), is(nullValue()));
    }

    @Test
    public void testToSecretKeyFactoryCharSequenceParameters() {
        assertThat(((String) null).toSecretKeyFactory(), is(nullValue()));
    }

    @Test
    public void testToSecretKeyFactoryCharSequenceParametersProvider() {
        assertThat(((String) null).toSecretKeyFactory(Optional.empty()), is(nullValue()));
    }

}
