# 内部解説

フォルダ構成は以下のとおり。

```
├─engschedule
│   ├─build
│   │  └─libs ## ビルド成果物の出力場所
│   ├─gradle
│   │  └─wrapper
│   └─src
│       ├─main
│       │  ├─java
│       │  │  └─jp
│       │  │      └─co
│       │  │          └─tskg
│       │  │              ├─controller
│       │  │              ├─entity
│       │  │              ├─model
│       │  │              ├─repository
│       │  │              ├─rest
│       │  │              │  └─repository
│       │  │              └─service
│       │  └─resources
│       │      └─static ## htmlの場所。
│       │          ├─css
│       │          ├─images
│       │          └─js
│       └─test　## テストコードの場所
└─sample_data
    └─practicedb.backup ## テストデータのバックアップ
```

## テストデータについて

一度、Eclipseなどで実行すると、ローカルのPostgreSQLのpracticedbデータベースに複数のテーブルが作成されます。  
テストデータとして、以下をpgAdminなどで入力してください。

`sample_data\practicedb.backup`にバックアップを用意しました。  
pgAdminなどでリストアしてください。

### employyテーブル  

| id | emp_name     | is_found |
|:---|:-------------|:---------|
| 1  | 勝どき一太郎 | true     |
| 2  | 勝どき二太郎 | false    |
| 3  | 勝どき三太郎 | false    |
| 4  | 勝どき四太郎 | false    |

### sectionテーブル

| id | section_name |
|:---|:-------------|
| 1  | 管理部       |
| 2  | 営業部       |


### section_employeeテーブル


| id | employee_id | section_id |
|:---|:------------|:-----------|
| 1  | 1           | 1          |
| 2  | 2           | 1          |
| 3  | 3           | 2          |


## 関連リンク

* RESTful APIの仕様 [Hypertext Application Language](http://stateless.co/hal_specification.html)
* バックエンドのフレームワーク [Spring Boot](https://projects.spring.io/spring-boot/)
  * [Spring Data REST リファレンス](http://docs.spring.io/spring-data/rest/docs/current/reference/html/)
* フロントエンドのフレームワーク[React](https://facebook.github.io/react/)
