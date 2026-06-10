# Football DNA Data Import

- Imported At: 2026-06-10T15:29:02.493835100
- Overall Success: true
- Artifact Strategy: single aggregated artifact

## Target Results

- `clubs` status=SUCCEEDED, sourceType=CSV, records=20
- `emotional-dna` status=SUCCEEDED, sourceType=CSV, records=20
- `playstyle-dna` status=SUCCEEDED, sourceType=CSV, records=20
- `club-status` status=SUCCEEDED, sourceType=CSV, records=20
- `club-tags` status=SUCCEEDED, sourceType=CSV, records=80
- `dna-rubric` status=SUCCEEDED, sourceType=CSV, records=30

## Target: clubs

- Source Type: CSV
- Source Location: https://docs.google.com/spreadsheets/d/e/2PACX-1vTzTKY1ayi89TfAbUft6_jX38auEvg50dDvfhXoqiEe9oZ1abJkIQXkVSecFV1_8EqCC-HSEVBMTlDn/pub?gid=0&single=true&output=csv
- Columns: club_id, club_name, league, season, country, city, is_active, note
- Record Count: 20

### Raw Payload

```text
club_id,club_name,league,season,country,city,is_active,note
ARS,Arsenal,EPL,2026-27,England,London,TRUE,2026-27 EPL 기준 초기 데이터
AVL,Aston Villa,EPL,2026-27,England,Birmingham,TRUE,2026-27 EPL 기준 초기 데이터
BOU,Bournemouth,EPL,2026-27,England,Bournemouth,TRUE,2026-27 EPL 기준 초기 데이터
BRE,Brentford,EPL,2026-27,England,London,TRUE,2026-27 EPL 기준 초기 데이터
BHA,Brighton & Hove Albion,EPL,2026-27,England,Brighton and Hove,TRUE,2026-27 EPL 기준 초기 데이터
CHE,Chelsea,EPL,2026-27,England,London,TRUE,2026-27 EPL 기준 초기 데이터
COV,Coventry City,EPL,2026-27,England,Coventry,TRUE,2026-27 EPL 기준 초기 데이터
CRY,Crystal Palace,EPL,2026-27,England,London,TRUE,2026-27 EPL 기준 초기 데이터
EVE,Everton,EPL,2026-27,England,Liverpool,TRUE,2026-27 EPL 기준 초기 데이터
FUL,Fulham,EPL,2026-27,England,London,TRUE,2026-27 EPL 기준 초기 데이터
HUL,Hull City,EPL,2026-27,England,Kingston upon Hull,TRUE,2026-27 EPL 기준 초기 데이터
IPS,Ipswich Town,EPL,2026-27,England,Ipswich,TRUE,2026-27 EPL 기준 초기 데이터
LEE,Leeds United,EPL,2026-27,England,Leeds,TRUE,2026-27 EPL 기준 초기 데이터
LIV,Liverpool,EPL,2026-27,England,Liverpool,TRUE,2026-27 EPL 기준 초기 데이터
MCI,Manchester City,EPL,2026-27,England,Manchester,TRUE,2026-27 EPL 기준 초기 데이터
MUN,Manchester United,EPL,2026-27,England,Manchester,TRUE,2026-27 EPL 기준 초기 데이터
NEW,Newcastle United,EPL,2026-27,England,Newcastle upon Tyne,TRUE,2026-27 EPL 기준 초기 데이터
NFO,Nottingham Forest,EPL,2026-27,England,Nottingham,TRUE,2026-27 EPL 기준 초기 데이터
SUN,Sunderland,EPL,2026-27,England,Sunderland,TRUE,2026-27 EPL 기준 초기 데이터
TOT,Tottenham Hotspur,EPL,2026-27,England,London,TRUE,2026-27 EPL 기준 초기 데이터
```

### Imported Record Preview

```text
city=London, is_active=TRUE, league=EPL, note=2026-27 EPL 기준 초기 데이터, season=2026-27, club_id=ARS, club_name=Arsenal, country=England
city=Birmingham, is_active=TRUE, league=EPL, note=2026-27 EPL 기준 초기 데이터, season=2026-27, club_id=AVL, club_name=Aston Villa, country=England
city=Bournemouth, is_active=TRUE, league=EPL, note=2026-27 EPL 기준 초기 데이터, season=2026-27, club_id=BOU, club_name=Bournemouth, country=England
```

## Target: emotional-dna

- Source Type: CSV
- Source Location: https://docs.google.com/spreadsheets/d/e/2PACX-1vTzTKY1ayi89TfAbUft6_jX38auEvg50dDvfhXoqiEe9oZ1abJkIQXkVSecFV1_8EqCC-HSEVBMTlDn/pub?gid=1950338110&single=true&output=csv
- Columns: club_id, club_name, season, club_prestige, fan_culture, underdog, growth, star_power, drama, local_identity, popularity, source_type, note
- Record Count: 20

### Raw Payload

```text
club_id,club_name,season,club_prestige,fan_culture,underdog,growth,star_power,drama,local_identity,popularity,source_type,note
ARS,Arsenal,2026-27,5,4,2,4,4,4,3,5,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
AVL,Aston Villa,2026-27,3,4,3,4,3,3,4,3,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
BOU,Bournemouth,2026-27,1,2,5,3,1,2,3,1,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
BRE,Brentford,2026-27,2,3,5,3,2,2,3,2,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
BHA,Brighton & Hove Albion,2026-27,2,3,5,5,2,3,3,2,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
CHE,Chelsea,2026-27,4,3,1,3,4,4,2,4,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
COV,Coventry City,2026-27,2,4,5,5,2,5,5,2,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
CRY,Crystal Palace,2026-27,2,4,4,3,2,3,4,2,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
EVE,Everton,2026-27,4,5,3,2,2,4,5,3,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
FUL,Fulham,2026-27,2,2,3,3,2,2,2,2,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
HUL,Hull City,2026-27,1,3,5,5,1,3,4,1,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
IPS,Ipswich Town,2026-27,2,4,5,4,2,3,5,2,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
LEE,Leeds United,2026-27,4,5,4,4,3,5,5,4,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
LIV,Liverpool,2026-27,5,5,2,3,5,5,4,5,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
MCI,Manchester City,2026-27,5,3,1,2,5,2,2,5,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
MUN,Manchester United,2026-27,5,5,1,3,5,5,4,5,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
NEW,Newcastle United,2026-27,4,5,3,4,4,4,5,4,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
NFO,Nottingham Forest,2026-27,4,4,4,3,2,4,5,3,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
SUN,Sunderland,2026-27,3,5,5,5,2,5,5,3,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
TOT,Tottenham Hotspur,2026-27,4,4,3,3,4,5,4,4,MANUAL_ANCHOR,초기 주관 채점. 추후 커뮤니티 의견 반영 가능
```

### Imported Record Preview

```text
season=2026-27, fan_culture=4, growth=4, club_name=Arsenal, note=초기 주관 채점. 추후 커뮤니티 의견 반영 가능, underdog=2, drama=4, local_identity=3, star_power=4, source_type=MANUAL_ANCHOR, club_id=ARS, popularity=5, club_prestige=5
season=2026-27, fan_culture=4, growth=4, club_name=Aston Villa, note=초기 주관 채점. 추후 커뮤니티 의견 반영 가능, underdog=3, drama=3, local_identity=4, star_power=3, source_type=MANUAL_ANCHOR, club_id=AVL, popularity=3, club_prestige=3
season=2026-27, fan_culture=2, growth=3, club_name=Bournemouth, note=초기 주관 채점. 추후 커뮤니티 의견 반영 가능, underdog=5, drama=2, local_identity=3, star_power=1, source_type=MANUAL_ANCHOR, club_id=BOU, popularity=1, club_prestige=1
```

## Target: playstyle-dna

- Source Type: CSV
- Source Location: https://docs.google.com/spreadsheets/d/e/2PACX-1vTzTKY1ayi89TfAbUft6_jX38auEvg50dDvfhXoqiEe9oZ1abJkIQXkVSecFV1_8EqCC-HSEVBMTlDn/pub?gid=1969219176&single=true&output=csv
- Columns: club_id, club_name, season, possession, directness, pressing, organization, creativity, transition_speed, source_type, raw_source, note
- Record Count: 20

### Raw Payload

```text
club_id,club_name,season,possession,directness,pressing,organization,creativity,transition_speed,source_type,raw_source,note
ARS,Arsenal,2026-27,5,3,4,5,3,3,MANUAL_MOCK,Eye Test,목업 데이터
AVL,Aston Villa,2026-27,3,4,3,3,4,4,MANUAL_MOCK,Eye Test,목업 데이터
BOU,Bournemouth,2026-27,2,4,3,2,2,4,MANUAL_MOCK,Eye Test,목업 데이터
BRE,Brentford,2026-27,2,5,3,3,3,5,MANUAL_MOCK,Eye Test,목업 데이터
BHA,Brighton & Hove Albion,2026-27,4,3,4,5,4,3,MANUAL_MOCK,Eye Test,목업 데이터
CHE,Chelsea,2026-27,4,3,4,3,4,3,MANUAL_MOCK,Eye Test,목업 데이터
COV,Coventry City,2026-27,2,4,3,3,3,4,MANUAL_MOCK,Eye Test,목업 데이터
CRY,Crystal Palace,2026-27,2,4,3,2,3,4,MANUAL_MOCK,Eye Test,목업 데이터
EVE,Everton,2026-27,2,3,3,3,2,2,MANUAL_MOCK,Eye Test,목업 데이터
FUL,Fulham,2026-27,3,3,2,3,3,3,MANUAL_MOCK,Eye Test,목업 데이터
HUL,Hull City,2026-27,2,4,2,2,2,4,MANUAL_MOCK,Eye Test,목업 데이터
IPS,Ipswich Town,2026-27,2,4,3,2,2,4,MANUAL_MOCK,Eye Test,목업 데이터
LEE,Leeds United,2026-27,3,5,5,3,3,5,MANUAL_MOCK,Eye Test,목업 데이터
LIV,Liverpool,2026-27,4,4,5,4,4,4,MANUAL_MOCK,Eye Test,목업 데이터
MCI,Manchester City,2026-27,5,2,5,5,4,2,MANUAL_MOCK,Eye Test,목업 데이터
MUN,Manchester United,2026-27,3,4,3,2,4,4,MANUAL_MOCK,Eye Test,목업 데이터
NEW,Newcastle United,2026-27,3,5,5,3,3,5,MANUAL_MOCK,Eye Test,목업 데이터
NFO,Nottingham Forest,2026-27,2,5,3,2,2,5,MANUAL_MOCK,Eye Test,목업 데이터
SUN,Sunderland,2026-27,2,4,4,2,2,4,MANUAL_MOCK,Eye Test,목업 데이터
TOT,Tottenham Hotspur,2026-27,3,5,3,3,4,5,MANUAL_MOCK,Eye Test,목업 데이터
```

### Imported Record Preview

```text
source_type=MANUAL_MOCK, directness=3, note=목업 데이터, club_id=ARS, creativity=3, possession=5, season=2026-27, club_name=Arsenal, organization=5, transition_speed=3, pressing=4, raw_source=Eye Test
source_type=MANUAL_MOCK, directness=4, note=목업 데이터, club_id=AVL, creativity=4, possession=3, season=2026-27, club_name=Aston Villa, organization=3, transition_speed=4, pressing=3, raw_source=Eye Test
source_type=MANUAL_MOCK, directness=4, note=목업 데이터, club_id=BOU, creativity=2, possession=2, season=2026-27, club_name=Bournemouth, organization=2, transition_speed=4, pressing=3, raw_source=Eye Test
```

## Target: club-status

- Source Type: CSV
- Source Location: https://docs.google.com/spreadsheets/d/e/2PACX-1vTzTKY1ayi89TfAbUft6_jX38auEvg50dDvfhXoqiEe9oZ1abJkIQXkVSecFV1_8EqCC-HSEVBMTlDn/pub?gid=1858033797&single=true&output=csv
- Columns: club_id, club_name, season, competition_tier, trend, source_type, note
- Record Count: 20

### Raw Payload

```text
club_id,club_name,season,competition_tier,trend,source_type,note
ARS,Arsenal,2026-27,우승후보,안정기,MANUAL,초기 설명용 상태값
AVL,Aston Villa,2026-27,도전자,상승세,MANUAL,초기 설명용 상태값
BOU,Bournemouth,2026-27,생존경쟁,리빌딩,MANUAL,초기 설명용 상태값
BRE,Brentford,2026-27,생존경쟁,안정기,MANUAL,초기 설명용 상태값
BHA,Brighton & Hove Albion,2026-27,도전자,상승세,MANUAL,초기 설명용 상태값
CHE,Chelsea,2026-27,우승후보,리빌딩,MANUAL,초기 설명용 상태값
COV,Coventry City,2026-27,생존경쟁,상승세,MANUAL,초기 설명용 상태값
CRY,Crystal Palace,2026-27,중위권,안정기,MANUAL,초기 설명용 상태값
EVE,Everton,2026-27,생존경쟁,리빌딩,MANUAL,초기 설명용 상태값
FUL,Fulham,2026-27,중위권,안정기,MANUAL,초기 설명용 상태값
HUL,Hull City,2026-27,생존경쟁,상승세,MANUAL,초기 설명용 상태값
IPS,Ipswich Town,2026-27,생존경쟁,상승세,MANUAL,초기 설명용 상태값
LEE,Leeds United,2026-27,중위권,상승세,MANUAL,초기 설명용 상태값
LIV,Liverpool,2026-27,우승후보,안정기,MANUAL,초기 설명용 상태값
MCI,Manchester City,2026-27,왕조,안정기,MANUAL,초기 설명용 상태값
MUN,Manchester United,2026-27,도전자,리빌딩,MANUAL,초기 설명용 상태값
NEW,Newcastle United,2026-27,도전자,상승세,MANUAL,초기 설명용 상태값
NFO,Nottingham Forest,2026-27,중위권,안정기,MANUAL,초기 설명용 상태값
SUN,Sunderland,2026-27,생존경쟁,상승세,MANUAL,초기 설명용 상태값
TOT,Tottenham Hotspur,2026-27,도전자,리빌딩,MANUAL,초기 설명용 상태값
```

### Imported Record Preview

```text
note=초기 설명용 상태값, trend=안정기, season=2026-27, source_type=MANUAL, club_name=Arsenal, competition_tier=우승후보, club_id=ARS
note=초기 설명용 상태값, trend=상승세, season=2026-27, source_type=MANUAL, club_name=Aston Villa, competition_tier=도전자, club_id=AVL
note=초기 설명용 상태값, trend=리빌딩, season=2026-27, source_type=MANUAL, club_name=Bournemouth, competition_tier=생존경쟁, club_id=BOU
```

## Target: club-tags

- Source Type: CSV
- Source Location: https://docs.google.com/spreadsheets/d/e/2PACX-1vTzTKY1ayi89TfAbUft6_jX38auEvg50dDvfhXoqiEe9oZ1abJkIQXkVSecFV1_8EqCC-HSEVBMTlDn/pub?gid=2123170883&single=true&output=csv
- Columns: club_id, club_name, tag, display_order, is_active, note
- Record Count: 80

### Raw Payload

```text
club_id,club_name,tag,display_order,is_active,note
ARS,Arsenal,#명문클럽,1,TRUE,초기 UI 태그 후보
ARS,Arsenal,#북런던,2,TRUE,초기 UI 태그 후보
ARS,Arsenal,#아르테타프로젝트,3,TRUE,초기 UI 태그 후보
ARS,Arsenal,#아름다운축구,4,TRUE,초기 UI 태그 후보
AVL,Aston Villa,#빌라파크,1,TRUE,초기 UI 태그 후보
AVL,Aston Villa,#전통클럽,2,TRUE,초기 UI 태그 후보
AVL,Aston Villa,#도전자,3,TRUE,초기 UI 태그 후보
AVL,Aston Villa,#상승세,4,TRUE,초기 UI 태그 후보
BOU,Bournemouth,#언더독,1,TRUE,초기 UI 태그 후보
BOU,Bournemouth,#작은경기장,2,TRUE,초기 UI 태그 후보
BOU,Bournemouth,#생존경쟁,3,TRUE,초기 UI 태그 후보
BOU,Bournemouth,#남부클럽,4,TRUE,초기 UI 태그 후보
BRE,Brentford,#언더독,1,TRUE,초기 UI 태그 후보
BRE,Brentford,#스마트클럽,2,TRUE,초기 UI 태그 후보
BRE,Brentford,#서런던,3,TRUE,초기 UI 태그 후보
BRE,Brentford,#데이터축구,4,TRUE,초기 UI 태그 후보
BHA,Brighton & Hove Albion,#언더독,1,TRUE,초기 UI 태그 후보
BHA,Brighton & Hove Albion,#선수육성맛집,2,TRUE,초기 UI 태그 후보
BHA,Brighton & Hove Albion,#성장중,3,TRUE,초기 UI 태그 후보
BHA,Brighton & Hove Albion,#데이터축구,4,TRUE,초기 UI 태그 후보
CHE,Chelsea,#런던빅클럽,1,TRUE,초기 UI 태그 후보
CHE,Chelsea,#스타군단,2,TRUE,초기 UI 태그 후보
CHE,Chelsea,#블루스,3,TRUE,초기 UI 태그 후보
CHE,Chelsea,#변화의팀,4,TRUE,초기 UI 태그 후보
COV,Coventry City,#언더독,1,TRUE,초기 UI 태그 후보
COV,Coventry City,#극적서사,2,TRUE,초기 UI 태그 후보
COV,Coventry City,#복귀스토리,3,TRUE,초기 UI 태그 후보
COV,Coventry City,#스카이블루,4,TRUE,초기 UI 태그 후보
CRY,Crystal Palace,#셀허스트파크,1,TRUE,초기 UI 태그 후보
CRY,Crystal Palace,#남런던,2,TRUE,초기 UI 태그 후보
CRY,Crystal Palace,#강한홈분위기,3,TRUE,초기 UI 태그 후보
CRY,Crystal Palace,#언더독,4,TRUE,초기 UI 태그 후보
EVE,Everton,#구디슨의기억,1,TRUE,초기 UI 태그 후보
EVE,Everton,#머지사이드,2,TRUE,초기 UI 태그 후보
EVE,Everton,#전통클럽,3,TRUE,초기 UI 태그 후보
EVE,Everton,#충성팬덤,4,TRUE,초기 UI 태그 후보
FUL,Fulham,#크레이븐코티지,1,TRUE,초기 UI 태그 후보
FUL,Fulham,#서런던,2,TRUE,초기 UI 태그 후보
FUL,Fulham,#차분한클럽,3,TRUE,초기 UI 태그 후보
FUL,Fulham,#입문친화,4,TRUE,초기 UI 태그 후보
HUL,Hull City,#언더독,1,TRUE,초기 UI 태그 후보
HUL,Hull City,#승격팀,2,TRUE,초기 UI 태그 후보
HUL,Hull City,#헐시티,3,TRUE,초기 UI 태그 후보
HUL,Hull City,#도전자,4,TRUE,초기 UI 태그 후보
IPS,Ipswich Town,#포트먼로드,1,TRUE,초기 UI 태그 후보
IPS,Ipswich Town,#승격팀,2,TRUE,초기 UI 태그 후보
IPS,Ipswich Town,#지역밀착,3,TRUE,초기 UI 태그 후보
IPS,Ipswich Town,#언더독,4,TRUE,초기 UI 태그 후보
LEE,Leeds United,#엘런드로드,1,TRUE,초기 UI 태그 후보
LEE,Leeds United,#강성팬덤,2,TRUE,초기 UI 태그 후보
LEE,Leeds United,#요크셔,3,TRUE,초기 UI 태그 후보
LEE,Leeds United,#드라마,4,TRUE,초기 UI 태그 후보
LIV,Liverpool,#YNWA,1,TRUE,초기 UI 태그 후보
LIV,Liverpool,#안필드,2,TRUE,초기 UI 태그 후보
LIV,Liverpool,#TheKop,3,TRUE,초기 UI 태그 후보
LIV,Liverpool,#역전의명수,4,TRUE,초기 UI 태그 후보
MCI,Manchester City,#왕조,1,TRUE,초기 UI 태그 후보
MCI,Manchester City,#펩시티,2,TRUE,초기 UI 태그 후보
MCI,Manchester City,#점유율축구,3,TRUE,초기 UI 태그 후보
MCI,Manchester City,#스타군단,4,TRUE,초기 UI 태그 후보
MUN,Manchester United,#올드트래포드,1,TRUE,초기 UI 태그 후보
MUN,Manchester United,#레드데블스,2,TRUE,초기 UI 태그 후보
MUN,Manchester United,#글로벌클럽,3,TRUE,초기 UI 태그 후보
MUN,Manchester United,#드라마,4,TRUE,초기 UI 태그 후보
NEW,Newcastle United,#세인트제임스파크,1,TRUE,초기 UI 태그 후보
NEW,Newcastle United,#원클럽원도시,2,TRUE,초기 UI 태그 후보
NEW,Newcastle United,#광적인팬덤,3,TRUE,초기 UI 태그 후보
NEW,Newcastle United,#북동부의자존심,4,TRUE,초기 UI 태그 후보
NFO,Nottingham Forest,#유러피언컵,1,TRUE,초기 UI 태그 후보
NFO,Nottingham Forest,#시티그라운드,2,TRUE,초기 UI 태그 후보
NFO,Nottingham Forest,#전통클럽,3,TRUE,초기 UI 태그 후보
NFO,Nottingham Forest,#로컬팬덤,4,TRUE,초기 UI 태그 후보
SUN,Sunderland,#스타디움오브라이트,1,TRUE,초기 UI 태그 후보
SUN,Sunderland,#북동부더비,2,TRUE,초기 UI 태그 후보
SUN,Sunderland,#충성팬덤,3,TRUE,초기 UI 태그 후보
SUN,Sunderland,#언더독,4,TRUE,초기 UI 태그 후보
TOT,Tottenham Hotspur,#북런던,1,TRUE,초기 UI 태그 후보
TOT,Tottenham Hotspur,#손흥민,2,TRUE,초기 UI 태그 후보
TOT,Tottenham Hotspur,#드라마,3,TRUE,초기 UI 태그 후보
TOT,Tottenham Hotspur,#공격적이미지,4,TRUE,초기 UI 태그 후보
```

### Imported Record Preview

```text
is_active=TRUE, club_id=ARS, tag=#명문클럽, display_order=1, club_name=Arsenal, note=초기 UI 태그 후보
is_active=TRUE, club_id=ARS, tag=#북런던, display_order=2, club_name=Arsenal, note=초기 UI 태그 후보
is_active=TRUE, club_id=ARS, tag=#아르테타프로젝트, display_order=3, club_name=Arsenal, note=초기 UI 태그 후보
```

## Target: dna-rubric

- Source Type: CSV
- Source Location: https://docs.google.com/spreadsheets/d/e/2PACX-1vTzTKY1ayi89TfAbUft6_jX38auEvg50dDvfhXoqiEe9oZ1abJkIQXkVSecFV1_8EqCC-HSEVBMTlDn/pub?gid=300000001&single=true&output=csv
- Columns: category, metric_key, metric_name, score, anchor_club, description
- Record Count: 30

### Raw Payload

```text
category,metric_key,metric_name,score,anchor_club,description
감성,club_prestige,클럽 위상,5,Liverpool / Manchester United,세계 축구 역사에서 중요한 위치를 가진 클럽
감성,club_prestige,클럽 위상,3,Tottenham / Newcastle,EPL에서 확실한 존재감이 있는 클럽
감성,club_prestige,클럽 위상,1,Bournemouth / Hull City,EPL 내 역사적 영향력이 제한적인 클럽
감성,fan_culture,팬문화,5,Liverpool / Newcastle,팬문화 자체가 클럽 정체성의 핵심
감성,fan_culture,팬문화,3,Aston Villa / Brighton,홈 팬덤과 응원 문화가 뚜렷한 수준
감성,fan_culture,팬문화,1,Bournemouth / Fulham,팬문화가 클럽 이미지의 핵심은 아님
감성,underdog,언더독,5,Brighton / Brentford / Coventry,강자에 맞서는 도전자 정체성이 핵심
감성,underdog,언더독,3,Aston Villa / Tottenham,상황에 따라 도전자 이미지가 있음
감성,underdog,언더독,1,Manchester City / Manchester United,강자 또는 지배자 이미지가 강함
감성,growth,성장,5,Brighton / Coventry / Sunderland,성장 서사가 클럽의 핵심 매력
감성,growth,성장,3,Newcastle / Tottenham,평균적인 발전 가능성 또는 혼재된 기대감
감성,growth,성장,1,Manchester City,성장보다 이미 완성된 강팀 이미지
감성,star_power,스타성,5,Manchester City / Manchester United / Liverpool,월드클래스 스타성이 클럽의 핵심 매력
감성,star_power,스타성,3,Arsenal / Aston Villa,리그 팬들이 알 만한 선수 다수
감성,star_power,스타성,1,Bournemouth / Hull City,스타 선수나 화제성이 약함
감성,drama,드라마,5,Liverpool / Manchester United / Leeds,극적인 서사와 명장면이 반복적으로 회자됨
감성,drama,드라마,3,Aston Villa / Brighton,평균적인 수준의 스토리 보유
감성,drama,드라마,1,Manchester City / Fulham,드라마적 이미지가 상대적으로 약함
감성,local_identity,지역성,5,Newcastle / Everton / Sunderland,도시/지역과 클럽이 거의 하나처럼 인식됨
감성,local_identity,지역성,3,Arsenal / Brentford / Brighton,지역 클럽 이미지가 뚜렷함
감성,local_identity,지역성,1,Manchester City / Chelsea,지역 정체성이 상대적으로 약하거나 글로벌 이미지가 우세
감성,popularity,대중성,5,Manchester United / Liverpool / Arsenal,국내외 입문자도 쉽게 접할 수 있는 클럽
감성,popularity,대중성,3,Aston Villa / Everton / Nottingham Forest,EPL 팬에게 익숙한 수준
감성,popularity,대중성,1,Bournemouth / Hull City,입문자가 접하기 어려운 수준
플레이스타일,possession,점유율,5,API/수동,리그 최상위권 점유율 또는 경기를 지배하는 스타일
플레이스타일,directness,직접성,5,API/수동,전방 전개가 빠르고 직선적인 스타일
플레이스타일,pressing,압박,5,API/수동,공을 잃은 뒤 적극적으로 되찾으려는 스타일
플레이스타일,organization,조직성,5,API/수동,시스템과 약속된 움직임이 핵심인 스타일
플레이스타일,creativity,창의성,5,API/수동,개인 창의성과 예측 불가능성이 큰 스타일
플레이스타일,transition_speed,전환속도,5,API/수동,공수 전환이 빠르고 즉시 상대를 공략하는 스타일
```

### Imported Record Preview

```text
metric_name=클럽 위상, score=5, description=세계 축구 역사에서 중요한 위치를 가진 클럽, metric_key=club_prestige, anchor_club=Liverpool / Manchester United, category=감성
metric_name=클럽 위상, score=3, description=EPL에서 확실한 존재감이 있는 클럽, metric_key=club_prestige, anchor_club=Tottenham / Newcastle, category=감성
metric_name=클럽 위상, score=1, description=EPL 내 역사적 영향력이 제한적인 클럽, metric_key=club_prestige, anchor_club=Bournemouth / Hull City, category=감성
```
