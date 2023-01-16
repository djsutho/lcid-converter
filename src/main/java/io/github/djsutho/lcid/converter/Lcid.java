package io.github.djsutho.lcid.converter;

import java.util.Locale;
import java.util.Map;

/**
 * {@code Lcid} is a utility class used to map between {@link Locale} and lcid
 * in {@link Integer} form. The mapping is described
 * <a href="https://learn.microsoft.com/en-us/openspecs/windows_protocols/ms-lcid/63d3d639-7fd2-4afb-abbe-0d5b5551eef8">here</a>
 *
 */
public class Lcid {
    private Lcid() {}

    /**
     * Returns the lcid for the given locale or {@code null} if the locale does
     * not have a corresponding lcid.
     *
     * @param locale the Locale object to lookup.
     * @return the corresponding lcid in Integer form that matches the locale.
     *         If no match is found then {@code null} is returned.
     */
    public static Integer from(Locale locale) {
        return Data.localeToLcid.get(locale);
    }

    /**
     * Returns the locale for the given lcid or {@code null} if the lcid does
     * not have a corresponding locale.
     *
     * @param lcid the Integer value for the lcid to lookup.
     * @return the corresponding locale object that matches the lcid. If no
     *         match is found then {@code null} is returned.
     */
    public static Locale to(Integer lcid) {
        return Data.lcidToLocale.get(lcid);
    }

    /**
     * Returns the locale for the given lcid or {@code null} if the lcid does
     * not have a corresponding locale.
     *
     * @param lcid the {@link String} form of either a base 10 {@link Integer}
     *             or a hex value when it is preceded by {@code 0x}.
     *             e.g. {@code 0x0c0c -> 3084}.
     * @return the corresponding locale object that matches the lcid. If no
     *         match is found then {@code null} is returned.
     * @throws NumberFormatException if the {@code lcid} does not contain a
     *         parsable {@code int}.
     */
    public static Locale to(String lcid) throws NumberFormatException {
        if (lcid == null) return null;
        if (lcid.startsWith("0x")) {
            return to(Integer.parseInt(lcid.substring(2), 16));
        }
        return to(Integer.parseInt(lcid));
    }

    /**
     * @deprecated This returns the data backing the {@link #from(Locale) from}
     * method. Returned data can be edited causing unexpected behaviour. Only
     * use this if you know what you're doing (most likely to override a lookup
     * that does not meet your requirements).
     *
     * @return a map of locales to lcids.
     */
    @Deprecated
    public static Map<Locale, Integer> getLocaleToLcidData() {
        return Data.localeToLcid;
    }

    /**
     * @deprecated This returns the data backing the {@link #to(Integer) to}
     * method. Returned data can be edited causing unexpected behaviour. Only
     * use this if you know what you're doing (most likely to override a lookup
     * that does not meet your requirements).
     *
     * @return a map of lcids to Locales.
     */
    @Deprecated
    public static java.util.Map<Integer, Locale> getLcidToLocaleData() {
        return Data.lcidToLocale;
    }
}
