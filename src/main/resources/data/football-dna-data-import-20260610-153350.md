# Football DNA Data Import

- Imported At: 2026-06-10T15:33:50.586631
- Overall Success: false
- Artifact Strategy: single aggregated artifact

## Target Results

- `clubs` status=SUCCEEDED, sourceType=CSV, records=20
- `emotional-dna` status=SUCCEEDED, sourceType=CSV, records=20
- `playstyle-dna` status=SUCCEEDED, sourceType=CSV, records=20
- `club-status` status=FAILED, sourceType=CSV, records=0, failure=Failed to fetch published CSV for target 'club-status'.

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
country=England, club_name=Arsenal, club_id=ARS, season=2026-27, note=2026-27 EPL 기준 초기 데이터, league=EPL, is_active=TRUE, city=London
country=England, club_name=Aston Villa, club_id=AVL, season=2026-27, note=2026-27 EPL 기준 초기 데이터, league=EPL, is_active=TRUE, city=Birmingham
country=England, club_name=Bournemouth, club_id=BOU, season=2026-27, note=2026-27 EPL 기준 초기 데이터, league=EPL, is_active=TRUE, city=Bournemouth
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
popularity=5, club_id=ARS, source_type=MANUAL_ANCHOR, star_power=4, local_identity=3, drama=4, underdog=2, note=초기 주관 채점. 추후 커뮤니티 의견 반영 가능, club_name=Arsenal, growth=4, fan_culture=4, season=2026-27, club_prestige=5
popularity=3, club_id=AVL, source_type=MANUAL_ANCHOR, star_power=3, local_identity=4, drama=3, underdog=3, note=초기 주관 채점. 추후 커뮤니티 의견 반영 가능, club_name=Aston Villa, growth=4, fan_culture=4, season=2026-27, club_prestige=3
popularity=1, club_id=BOU, source_type=MANUAL_ANCHOR, star_power=1, local_identity=3, drama=2, underdog=5, note=초기 주관 채점. 추후 커뮤니티 의견 반영 가능, club_name=Bournemouth, growth=3, fan_culture=2, season=2026-27, club_prestige=1
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
raw_source=Eye Test, pressing=4, transition_speed=3, organization=5, club_name=Arsenal, season=2026-27, possession=5, creativity=3, club_id=ARS, note=목업 데이터, directness=3, source_type=MANUAL_MOCK
raw_source=Eye Test, pressing=3, transition_speed=4, organization=3, club_name=Aston Villa, season=2026-27, possession=3, creativity=4, club_id=AVL, note=목업 데이터, directness=4, source_type=MANUAL_MOCK
raw_source=Eye Test, pressing=3, transition_speed=4, organization=2, club_name=Bournemouth, season=2026-27, possession=2, creativity=2, club_id=BOU, note=목업 데이터, directness=4, source_type=MANUAL_MOCK
```
