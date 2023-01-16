#!/usr/bin/env python3

import csv
import pathlib

def write(data, fp):
    print(data, end='')
    fp.write(data)

def main():
    base_path = pathlib.Path(__file__).parent.parent
    lcid_to_lang_tag = {}
    lang_tag_to_lcid = {}
    with open(base_path/'src/test/resources/lcid_to_lang_tag.csv') as csv_fp:
        for row in csv.DictReader(csv_fp):
            lcid_to_lang_tag[row['lcid']] = row['lang_tag']
            lang_tag_to_lcid[row['lang_tag']] = row['lcid']

    with open(base_path/'src/main/java/io/github/djsutho/lcid/converter/Data.java', 'w') as java_fp:
        write('''\
package io.github.djsutho.lcid.converter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class Data {
    static Map<Integer, Locale> lcidToLocale = loadLcidToLocale();
    static Map<Locale, Integer> localeToLcid = loadLocaleToLcid();

    static Map<Integer, Locale> loadLcidToLocale() {
        Map<Integer, Locale> data = new HashMap();
''', java_fp)

        for lcid, lang_tag in sorted(lcid_to_lang_tag.items()):
            write(f'''\
        data.put({lcid}, Locale.forLanguageTag("{lang_tag}"));
''', java_fp)

        write('''\
        return data;
    }

    static Map<Locale, Integer> loadLocaleToLcid() {
        Map<Locale, Integer> data = new HashMap<>();
''', java_fp)

        for lang_tag, lcid in sorted(lang_tag_to_lcid.items()):
            write(f'''\
        data.put(Locale.forLanguageTag("{lang_tag}"), {lcid});
''', java_fp)

        write('''\
        return data;
    }
}
''', java_fp)

if __name__ == "__main__":
    main()
