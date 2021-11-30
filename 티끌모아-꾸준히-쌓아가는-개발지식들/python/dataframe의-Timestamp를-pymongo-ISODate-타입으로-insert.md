# pymongo ISODate 타입으로 insert

설명을 자세히 정리할 생각은 아니다. 어떤 코드로 해결했는지 정리해보려 한다. 오늘 부터 코딩테스트 대비를 해야 한다. 6개월 뒤에는 퇴사를 하고, 두달간 이직 준비를 해야 해서다.<br>

내용이 다소 매끄럽지 못하다. 사무실의 일하는 위치가 사람들이 뺀질나게 드나드는 곳이라 뭐하는 지 다들 한번씩 쳐다보고 가서 짜증날정도로 가끔 뒤통수가 따가울때도 있고, 이유없이 두통이 올때도 있고, 매너 없는 사람들도 많고 아침에는 안녕하세요만 3분에 한번씩 듣는데 이것도 꽤 고역이다.. 문 앞이나 통로에 앉는 사람들의 고충이 아닐까 싶다. 이게 바로 눈물나게 허름한 ㅈㅅ기업에 온 특혜인듯 하다. 이런 이유로 눈치봐가면서 정리하느라 내용이 조금 이상할 수 있다.<br>

오늘 정리하는 내용은 dataframe 의 Timestamp 타입을 Datetime으로 변환 후에 MongoDB 의 ISODate으로 변환하는 과정에 대한 내용이다.<br>

yfinance 를 이용해서 데이터를 크롤링해서 mongodb 에 저장하는 로직을 작성 중에 yfiance에서 전달해주는 dataframe 형식 데이터를 파싱하면서 날짜 자료형을 MongoDB의 ISODate 타입으로 인서트 되게끔 해줘야 했었다.<br>

datetime 은 아래와 같이 dataframe의 timestamp 를 `pd.to_datetime()` 을 이용해서 datetime 으로 변환 후에 insert 하면 MongoDb에는 `ISODate` 로 저장된다.<br>

```python
dateTime = pd.to_datetime(row.Index, unit='s')
```

<br>

그리고 아래와 같이 바꾼 것을 출력해보면 아래와 같다.

```python
dateTime = pd.to_datetime(row.Index, unit='s')
===>>>> 2021-11-29 09:30:00-05:00
```

<br>

## 찾아본 자료들

- [Stackoverflow - How to insert a timestamp value into the ISODate type on MongoDB by python?](https://stackoverflow.com/questions/35623472/how-to-insert-a-timestamp-value-into-the-isodate-type-on-mongodb-by-python)
  - datetime.datetime 객체로 변환하면, 파이몽고는 이것을 ISO로 바로 변환해서 저장한다. 라고 이야기하고 있다.
- Create an ISODate with pyMongo
  - https://www.generacodice.com/en/articolo/2972891/create-an-isodate-with-pymongo 

<br>

## 전체 코드

```python
import datetime
import pytz
from pymongo import MongoClient
import yfinance as yf
import pandas as pd
from datetime import timezone, timedelta

host = "[아이피 or 호스트 주소]"
port = 27017
mongo_client = MongoClient(host=host, port=int(port))

def download_hour_and_save_mongo(yfinance_collection):
    ticker = 'AAPL'
    data = yf.download(tickers=ticker, period="0d", interval="1h")
    if data.empty:
        print("dataframe is empty. ticker = " + ticker)
    if not data.empty:
        list_buffer = []
        column_datetime = u'datetime'
        column_open     = u'open'
        column_high     = u'high'
        column_low      = u'low'
        column_close    = u'close'
        column_volume   = u'volume'
        column_ticker   = u'ticker'

        for row in data.itertuples():
            print(type(row.Index))
            dateTime = pd.to_datetime(row.Index, unit='s')
            print("===>>>> {}".format(dateTime))
            # dateTime = pd.to_datetime(row.Index, unit='s').replace(tzinfo=pytz.timezone('America/New_York'))
            # dateTime = pd.to_datetime(dateTime, unit='s').replace(tzinfo=pytz.timezone('America/New_York')).strftime('%Y-%m-%d %H:%M:%S%Z')
            # dateTime = pd.to_datetime(row.Index, format="%Y-%m-%d'T'%H:%M:%S%Z").isoformat()
            # dateTime = pd.to_datetime(row.Index).replace(tzinfo=pytz.timezone('America/New_York')).strftime("%Y-%m-%d %H:%M:%S%Z")
            print(type(dateTime))
            open = data.Open.loc[row.Index]
            high = data.High.loc[row.Index]
            low = data.Low.loc[row.Index]
            close = data.Close.loc[row.Index]
            adjClose = data['Adj Close'].loc[row.Index]
            volume = str(data.Volume.loc[row.Index])

            print('index = {}', dateTime)

            print(str.format("Open = {}, High = {}, low = {}, close = {}, Adj Close = {}, volume = {} ",
                             open, high, low, close, adjClose, volume))

            dict_buffer = {
                column_datetime: dateTime,
                column_open: open,
                column_high: high,
                column_low: low,
                column_close: close,
                column_volume: volume,
                column_ticker: ticker
            }

            document = dict_buffer
            list_buffer.append(document)

            filter = {
                '_id': '0'
            }

            yfinance_collection.update_one(
                # filter,
                {column_datetime : dateTime},
                {
                    '$set': document
                },
                upsert=True
            )

if __name__ == '__main__':
    yfinance_db = mongo_client['yfinance']
    yfinance_hour_collection = yfinance_db['yfinance_hour']
    yfinance_minute_collection = yfinance_db['yfinance_minute']
    yfinance_day_collection = yfinance_db['yfinance_day']

    download_hour_and_save_mongo(yfinance_hour_collection)
    download_minute_and_save_mongo(yfinance_minute_collection)
    download_day_and_save_mongo(yfinance_day_collection)
    
```

