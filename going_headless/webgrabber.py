#!/usr/bin/env python
## See: https://gist.github.com/dropmeaword/9372cbeb29e8390521c2
import csv
from os.path import expanduser
import sqlite3
import time, datetime
import os
import os  
from selenium import webdriver  
from selenium.webdriver.chrome.options import Options  
from selenium.webdriver.common.keys import Keys

chrome_options = Options()  
chrome_options.add_argument("--headless")  
chrome_options.binary_location ='/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'  
driver = webdriver.Chrome(executable_path=os.path.abspath("chromedriver"),chrome_options=chrome_options)



HISTORY_DB='~/Library/Application Support/FireFox/Profiles/r6ha5y55.default/places.sqlite'

conn = sqlite3.connect(expanduser(HISTORY_DB))
c = conn.cursor()

def firefox_list_history():
    result = c.execute('SELECT datetime(moz_historyvisits.visit_date/1000000,"unixepoch"), moz_places.url, moz_places.title FROM moz_places, moz_historyvisits WHERE moz_places.id = moz_historyvisits.place_id;')
    for row in result:
       now = datetime.datetime.now()
       file = "dom_"
       file += now.strftime("%Y.%m.%d %H:%M:%S")
       file +=".html"
       driver.get(row[1])
       driver.get_screenshot_as_file(file+".png")
       element =driver.find_element_by_xpath("//*")
       domtree = element.get_attribute("outerHTML")
       with open(file, 'w') as f:
           f.write(domtree)
    driver.close()

firefox_list_history()
