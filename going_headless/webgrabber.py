#!/usr/bin/env python

## Created by Adam Standke to use the browser automation tool of Selenium
## webdriver to control a web brower in a progrmatic fashion. The program 
## first examines firefox's history file (that is in sql format) and gets 
## the urls of each webpage that was typed in the browser's url address bar.
## Then each webpage is grabbed from the browser before being rendered to the display
## and each webpage is cleaned\prerpocessed by removing primarily scripts and javascripts. 
## The html files and their modified doms are placced in a seperate file that CoreEx will
## use to extract the main content  
## Also See: https://gist.github.com/dropmeaword/9372cbeb29e8390521c2 for discussion 
## of accessing a browser history for chrome and firefox

from os.path import expanduser
import sqlite3
import time, datetime
import os
from selenium import webdriver  
from selenium.webdriver.chrome.options import Options  
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.firefox.options import Options
from selenium.webdriver.firefox.firefox_binary import FirefoxBinary
import contextlib
import binascii
import base64
import lxml.html.clean as clean

'''sets up chrome driver for version 74 and makes it headless'''
# chrome_options = Options()  
# chrome_options.add_argument("--headless")  
# chrome_options.binary_location ='/Applications/Google Chrome.app/Contents/MacOS/Google Chrome'  
# driver = webdriver.Chrome(executable_path=os.path.abspath("chromedriver"),chrome_options=chrome_options)

'''sets up firefox driver and makes it headless'''
firefox_options = Options()
firefox_options.set_headless(headless=True)
binary= FirefoxBinary(firefox_path='/Applications/Firefox.app/Contents/MacOS/firefox-bin')
fp = webdriver.FirefoxProfile()
fp.set_preference("http.response.timeout", 5)
fp.set_preference("dom.max_script_run_time", 5)
driver = webdriver.Firefox(firefox_options=firefox_options, firefox_binary=binary, executable_path=os.path.abspath("geckodriver"), firefox_profile=fp)


## creates connection with Firefox's history file/database
HISTORY_DB='~/Library/Application Support/FireFox/Profiles/r6ha5y55.default/places.sqlite'
conn = sqlite3.connect(expanduser(HISTORY_DB))
c = conn.cursor()


## main function that executes sql querey on Firefox's history file/database to get urls and then calls
## either firefox (or chrome) to get the rendered webpage, cleans it by removing unwanted tags, 
## and then outputs the preprocessed HTML document to a file for further processing by CoreEx
def firefox_list_history():
    result = c.execute('SELECT DISTINCT  moz_places.url FROM moz_places, moz_historyvisits WHERE moz_places.id = moz_historyvisits.place_id AND moz_historyvisits.visit_type=2;')
    for row in result:
       file = row[0]
       #encodes url into base64 format for later link target analysis use(ie file name is the url of the webpage)
       file = base64.urlsafe_b64encode(file.encode())
       driver.get(row[0])
       content = driver.page_source
       #preprocesses the html dodcument(want to get rid of script tags and forms)
       cleaner = clean.Cleaner(style=False, page_structure=False, safe_attrs_only=False, 
                               remove_unknown_tags=True, links=False, scripts=True,
                               javascript=True, forms=True, embedded=False, comments=True, meta=False,
                               add_nofollow=False, frames=False, annoying_tags=False, processing_instructions=True)
       content = cleaner.clean_html(content)
       with open(file, 'w+') as f:
            f.write(content)
    driver.close()

## calls the function to download the html documents
firefox_list_history()
