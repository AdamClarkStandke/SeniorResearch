#!/usr/bin/env python
## Created by Adam Standke
## Also See: https://gist.github.com/dropmeaword/9372cbeb29e8390521c2

from os.path import expanduser
import sqlite3
import time, datetime
import os
from selenium import webdriver  
from selenium.webdriver.chrome.options import Options  
from selenium.webdriver.common.keys import Keys
import contextlib
import lxml.html.clean as clean

## sets up chrome driver for version 74 and makes it headless
chrome_options = Options()  
chrome_options.add_argument("--headless")  
chrome_options.binary_location ='/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'  
driver = webdriver.Chrome(executable_path=os.path.abspath("chromedriver"),chrome_options=chrome_options)


## creates connection with Monzillia's history file
HISTORY_DB='~/Library/Application Support/FireFox/Profiles/r6ha5y55.default/places.sqlite'
conn = sqlite3.connect(expanduser(HISTORY_DB))
c = conn.cursor()

## main function that executes sql querey on Monzillia history file to get urls from web browser then calls
## google chrome to get the rendered webpage, cleans it of javascript tags, script and other weird tags, 
## and then outputs rendered dom tree to a file
def firefox_list_history():
    result = c.execute('SELECT datetime(moz_historyvisits.visit_date/1000000,"unixepoch"), moz_places.url, moz_places.title FROM moz_places, moz_historyvisits WHERE moz_places.id = moz_historyvisits.place_id;')
    for row in result:
       now = datetime.datetime.now()
       file = "dom_"
       file += now.strftime("%Y.%m.%d %H:%M:%S")
       file +=".html"
       driver.get(row[1])
       content = driver.page_source
       cleaner = clean.Cleaner(links=False, meta=False, page_structure=False)
       content = cleaner.clean_html(content)
       with open(file, 'w') as f:
            f.write(content)
    driver.close()

## calls the function to download the html documents
firefox_list_history()
