#!/usr/bin/env python3

import csv
import collections
import locale
import pathlib

import pandas as pd

# another useful url though the chosen one seems more complete:
# https://learn.microsoft.com/en-us/openspecs/office_standards/ms-oe376/6c085406-a698-4e12-9d4d-c3b0ee3dbc4a'
LCID_LIST_URL = 'https://learn.microsoft.com/en-us/openspecs/windows_protocols/ms-lcid/63d3d639-7fd2-4afb-abbe-0d5b5551eef8'

def write_to_csv(data, headers):
    base_path = pathlib.Path(__file__).parent.parent
    file_name = '%s.csv' % '_to_'.join(headers)
    with open(base_path/'src/test/resources'/file_name, 'w') as csv_fp:
        writer = csv.writer(csv_fp)
        writer.writerow(headers)
        for k, v in sorted(data.items()):
            writer.writerow([k, v])

def main():
    tables = pd.read_html(LCID_LIST_URL)
    lcid_to_lang_tag = parse_table(tables[2]) | parse_table(tables[3])

    if len(lcid_to_lang_tag.keys()) != len(set(lcid_to_lang_tag.values())):
        print('keys=%s' % len(lcid_to_lang_tag.keys()))
        print('values=%s' % len(set(lcid_to_lang_tag.values())))
        print('common=%s' % collections.Counter(lcid_to_lang_tag.values()).most_common(5))
        raise Exception('number of lcids does not match number of locales '
            + 'aborting as we want this to be symmetrical.')

    write_to_csv(lcid_to_lang_tag, ['lcid', 'lang_tag'])

def parse_table(table):
    lcid_to_lang_tag = {}
    for row in table.values.tolist():
        lcid, lang_tag_cell = row[0], row[1]
        lcid = lcid.split('<')[0]
        try:
            lcid = int(lcid, 16)
        except ValueError as err:
            print('lcid=%s invalid' % lcid)
            continue

        lang_tag = (lang_tag_cell.split(',')[0]
            .replace('_', '-')
            .replace(' (math alphanumeric sorting)', '')
            .replace('reserved', ' '))
        if lang_tag.find(' ') > -1:
            print(f'invalid locale={lang_tag_cell} lcid={lcid}')
        else:
            lcid_to_lang_tag[lcid] = lang_tag
    return lcid_to_lang_tag

if __name__ == "__main__":
    main()
