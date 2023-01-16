package io.github.djsutho.lcid.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class LcidTest {
    private static Map<String, Locale> lcid_to_locale = new HashMap<>();
    static {
        lcid_to_locale.put("", Locale.US);
    }

    @Test
    void testFrom() {
        assertEquals(9, Lcid.from(Locale.ENGLISH));
        assertEquals(1033, Lcid.from(Locale.US));
        assertEquals(1028, Lcid.from(Locale.TRADITIONAL_CHINESE));
        assertEquals(3084, Lcid.from(Locale.CANADA_FRENCH));
        assertNull(Lcid.from(Locale.ROOT));
        assertNull(Lcid.from(null));
    }

    @Test
    void testToWithIntegerInput() {
        assertEquals(Locale.ENGLISH, Lcid.to(9));
        assertEquals(Locale.US, Lcid.to(1033));
        assertEquals(Locale.TRADITIONAL_CHINESE, Lcid.to(1028));
        assertEquals(Locale.CANADA_FRENCH, Lcid.to(3084));
        assertNull(Lcid.to(Integer.MIN_VALUE));
        assertNull(Lcid.to((Integer) null));
    }

    @Test
    void testToWithIntegerStringInput() {
        assertEquals(Locale.ENGLISH, Lcid.to("9"));
        assertEquals(Locale.US, Lcid.to("1033"));
        assertEquals(Locale.TRADITIONAL_CHINESE, Lcid.to("1028"));
        assertEquals(Locale.CANADA_FRENCH, Lcid.to("3084"));
        assertNull(Lcid.to(String.valueOf(Integer.MIN_VALUE)));
        assertNull(Lcid.to((String) null));
    }
    @Test
    void testToWithHexStringInput() {
        assertEquals(Locale.FRENCH, Lcid.to("0x000C"));
        assertEquals(Locale.US, Lcid.to("0x0409"));
        assertEquals(Locale.CANADA_FRENCH, Lcid.to("0x0c0c"));
        assertNull(Lcid.to("0xffffff"));
    }

    @Test
    void testToWithInvalidStringInput() {
        // Trying to stick with java 7 if possible so can't use assertThrows like:
        // assertThrows(NumberFormatException.class, () -> Lcid.to("000C"));
        for (String input : Arrays.asList("000C", "0xfoo", "foo")) {
            try {
                Lcid.to(input);
                fail("NumberFormatException not thrown for invalid input=" + input);
            } catch (NumberFormatException ex) {}
        }

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/lcid_to_lang_tag.csv", useHeadersInDisplayName=true)
    void testToUsingGeneratedList(Integer lcid, String langTag) {
        assertEquals(Locale.forLanguageTag(langTag), Lcid.to(lcid));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/lcid_to_lang_tag.csv", useHeadersInDisplayName=true)
    void testFromUsingGeneratedList(Integer lcid, String langTag) {
        assertEquals(lcid, Lcid.from(Locale.forLanguageTag(langTag)));
    }

    @Test
    void testMappingsAreEqualInBothDirections() {
        // If we find we have unequal mappings it means we have some duplicates
        // e.g. one Locale having multiple lcids, and we'd need to make a
        // decision on how to handle that.
        assertEquals(Lcid.getLcidToLocaleData().size(), Lcid.getLocaleToLcidData().size());
    }
}