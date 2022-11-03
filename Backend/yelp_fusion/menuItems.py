"""
scrape yelp.com/menu/{biz} sites

"""

import requests
from bs4 import BeautifulSoup
import time
from urllib.parse import urlparse
import sqlalchemy


db_u = 'sjdefran'
db_p = 'pass'
db_host = 'localhost'
db_name = 'cy_rate'
conn = sqlalchemy.create_engine(f"mysql+mysqlconnector://{db_u}:{db_p}@{db_host}/{db_name}")

