# Postgresql 날짜 연산 총정리



## 날짜 포맷팅

```sql
select to_char(now(), 'YYYY-MM-DD');
```

<br>

출력결과

```plain
2021-10-08
```

<br>

## 오늘날짜 추출하기

```sql
select extract ('YEAR' FROM Now());
select extract ('DAY' FROM Now());
```

<br>

출력결과

```plain
2021
08
```



나머지는 오늘 저녁에 더 정리할 예정... 빠샤





