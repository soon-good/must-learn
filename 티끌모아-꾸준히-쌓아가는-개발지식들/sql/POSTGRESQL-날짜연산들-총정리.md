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

## 오늘날짜에서 연/월/일 추출하기

```sql
select extract ('YEAR' FROM Now());
2021

select extract (MONTH FROM CURRENT_TIMESTAMP)
10

select extract ('DAY' FROM Now());
14
```

<br>

출력결과

```plain
2021
08
```

<br>



## 문자열 -> 날짜, 날짜 -> 문자열

### 문자열에서 timestamp 구하기

단순 날짜 구하기

```sql
SELECT 	'2018-01-01'::timestamp as start_date,
				'2018-01-20'::timestamp as end_date
```

<br>

### 특정 날짜에 대한 Timestamp 구하기

```sql
select '2021-01-01'::TIMESTAMP
```

<br>

### 특정 TIMESTAMP 에서 문자열 구하기

```sql
SELECT TO_CHAR('2021-01-01'::TIMESTAMP, 'YYYY')
```

<br>

### 문자열 기반으로 검색조건 만들어보기

```sql
SELECT price
FROM books b1
WHERE b1.publish_dt > '2020-01-01'::date
	AND b1.publish_dt <= '2020-12-31'::date + '1 year'
```

<br>

## timestamp -> DATE

```sql
SELECT ('2021-01-01'::TIMESTAMP)::DATE
```

<br>

## 시간축 만들기

Database로 시간축을 만드는 것은 좋지 않은 방식이긴 하지만, 일단한번 정리해봤다.

```sql
SELECT generate_series(
  SELECT '2018-01-01'::timestamp,
  SELECT '2018-12-31'::timestamp,
  interval '1 day'
)
```

<br>

## 특정 기간 이전의 날짜 구하기

2021-10-04 일에서 한달 전의 날짜 구하기 

```sql
SELECT '2021-10-04'::timestamp - interval '1 month';
```

이렇게 `'2021-10-04'::timestamp - interval '1 month'` 과 같은 식으로 특정 기간 이전의 날짜 이후의 날짜를 구하면, where 절에서 이것을 활용해서 기간 검색 조건에 활용하기 쉬워진다.



찾을 때 마다 그때 그때 정리하쟈!!!!!!!!!!!!!









