# コードの書き方メモ

Javaコードを書く場合の書き方を以下に記述する。

## オブジェクトのディープコピー

モデルとエンティティなどの複雑なオブジェクトのコピーは、`BeanUtils.copyProperties` を使用する。  
引数に指定されたオブジェクトの間で、同名の項目があったらコピーしてくれる。  
ただし、指定するオブジェクトはどちらもPOJO（つまり決まった書式のSetter,Getterがある形式）である必要がある。アノテーションで`@Data`とつけると、lombokがうまくやってくれる。  

```
// 以下のような場合は、empNameのみ、コピーされる。
EmployeeRes res = new EmployeeRes();
Employee emp = nomalRepos.findOne(id);
BeanUtils.copyProperties(emp,res);
```
```
@Data
@Entity
@Table(name = "employee")
public class Employee implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(nullable = false)
    private String empName;

    @NotNull
    @Column(nullable = false)
    private Boolean isFound;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="employee", targetEntity=EmployeeSection.class)
    private List<EmployeeSection> employeeSectionz;


}
```
```
@Data
public class EmployeeRes {

	private String empName;
}
```
## トランザクションの指定

コントローラクラスの先頭に`@Transactional`をつけること。  

## SQLの書き方

ネイティブSQLを実行するには、アノテーション`@Repository`をつけた**クラス**を作成し、`EntityManager`を`@Autowired`で注入する。
```
@Slf4j // ログ出力に必要
@Repository
public class PracticeNativeSqlRepository {

	// ネイティブSQLを使用するためにEntityManagerを直接使う
	@Autowired
	EntityManager entityManager;

    ・
    ・
    ・
```

その上で、`EntityManager`の`createNativeQuery`メソッドを使う。
```
@SuppressWarnings("unchecked")// getResultListの戻り値が総称型の情報を持っていないのでビルドすると警告が出力される。それを抑止。
List<EmployeeWithSectionNameRes> result = entityManager
      .createNativeQuery(
        "select se.id,e.emp_name,s.section_name from employee e,section s,section_employee se where s.id = se.section_id and e.id = se.employee_id and  s.id=:SEC_ID",
        EmployeeWithSectionNameRes.class)
          .setParameter("SEC_ID", sectionId)
          .getResultList();
```
### 取り出しEntityの作り方。使い方。

ネイティブSQLで取得した結果を格納するクラスは、独自に作成する。  
ポイントは…
* `@Entity`をつける。  
* ただし、テーブルの物理名称は不要。  
* ユニークなキーとなる項目を設けて`@Id`をつける。  
  `@Id`をつける項目は、基本的にはテーブルの持っているサロゲートキーを当てる。  
  （このルールに当てはまらないケースがあれば要相談）
* select文のスネークケースとクラスメンバのキャメルケースは、フレームワークにより自動的にマッピングされる。  

```
@Data                               // lombokで自動的にSetter,Gettetを生成する。
@AllArgsConstructor                 // lombokで引数付のコンストラクタを生成する。
@NoArgsConstructor                  // lombokで引数なしのコンストラクタを生成する。
@Entity                             // このクラスはエンティティであるという宣言。
public class EmployeeWithSectionNameRes implements Serializable {

	@Id
  private int id;

	private String empName;

	private String sectionName;
}
```
createNativeQueryメソッドの第２引数に作成したクラスを指定すると、select 文の取得結果をマッピングしてくれる。  

### 書き込みEntityの作り方。使い方。

書き込み用のエンティティは、各テーブルで基本的な項目を集約したクラスを`AppBaseEntity`として共通基盤に用意するので、それを継承すること。  
`AppBaseEntity`を継承することとアノテーション`@EqualsAndHashCode`を指定する以外は、一般的なSpringJPAの作り方を踏襲する。  
```
@Data                               // lombokで自動的にSetter,Gettetを生成する。
@EqualsAndHashCode(callSuper=true)  // 親クラスAppBaseEntityのequalsとhashCodeを呼び出すように設定する。
@Entity                             // このクラスはエンティティであるという宣言。
@Table(name = "employee")           // テーブルの物理名称を指定する。
public class Employee extends AppBaseEntity implements Serializable {

    // 以下には、AppBaseEntityにある項目以外を記述する。

    @NotNull                        // Bean Validation (Javaが行うバリデーション。SQLとは直接の関係はない)
    @Size(min = 1, max = 50)        // Bean Validation (SQLが実行される前に指定された検査を実行される)
    private String empName;

    @NotNull
    private Boolean isFound;
}
```

このエンティティにアクセスするには、`SpringJPA`の書き方で`@Repository`をつけたうえで、`AppRepository`を継承した**インターフェース**を作成する。  
`AppRepository`の総称型引数は、エンティティクラスと、エンティティに設定されたIdの型を記述する。  
以下のように空のインターフェースを作っただけでも、findAllやsaveなどの基本的なメソッドはフレームワークにより用意される。  
```
@Repository
public interface EmployeeRepository extends AppRepository<Employee, Inteere> {

}
```

`Repository`の機能を使用する場合は、サービスクラスに作成した**インターフェース**を`@Autowired`で注入する。  
そうするとインターフェースではなく、フレームワークが用意した実装クラスが注入される。  
```
@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository nomalRepos;    // 一般的なSpringJPAの作り方。


        ・
        ・
        ・
```

#### 書き込みEntityにおける、楽観的排他ロックについて

`AppRepository`は、一般的な`JpaRepository`に楽観的排他ロックの機能を追加したものである。  
楽観的排他ロックを使うには、更新すべきレコードを特定する際にlockVersionを指定して、ロックがかかっているかどうかを確認する。  
```
  @Autowired
  EmployeeRepository nomalRepos;    // AppRepositoryを継承したものを注入。

  // 従業員一件更新
  // EmployeeReqには、lockVersionを保持すること。
  public EmployeeRes update(EmployeeReq req,int id) {    //
    // versionを使った排他制御をする場合は
    // 更新すべきレコードを以下のメソッドで特定すること
    // もし、この時点で引数に渡したlockVersionとDB内のlockVersionに相違があった場合は
    // 例外があがる。
    Employee rec = nomalRepos.findOneWithValidVersion(req.getId(), req.getLockVersion());
    // lockVersionに相違がない場合（つまりレコードの書き込みが可能な場合）は、以降の処理が走る。
    BeanUtils.copyProperties(req, rec);
    Employee emp = nomalRepos.saveAndFlush(rec);

    EmployeeRes res = new EmployeeRes();
    BeanUtils.copyProperties(emp,res);
    return res;
  }
```

## 例外の投げ方

共通基盤に用意された`AppException`を投げる。  
引数に、httpステータスとメッセージを記述する。  
```
@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository nomalRepos;    // 一般的なSpringJPAの作り方。
    // 従業員一件取得
    public EmployeeRes findOne(Long id) {
      EmployeeRes res = new EmployeeRes();
      Employee emp = nomalRepos.findOne(id); // フレームワークにより用意されたメソッド
      if (emp == null) {
        // たとえば、idにDBにない値を入れるとレコードを取得できない
        // その場合に、独自エラーをあげる
        throw new AppException(HttpStatus.NOT_FOUND.value(),"employee not fund. ");
      }
      BeanUtils.copyProperties(emp,res);
      return res;
    }

        ・
        ・
        ・
```

## ログの出し方

クラスにアノテーション`@Slf4j`をつけた上で、以下のように記述する。
```
// ログ出力サンプル。
// アノテーションに、「@Slf4j」を書くこと
if (0 == chgCnt) {
  log.info("Data has not been updated.");
}
```

## テストのやり方

APIをテストする場合のツール
* Chrome application [Restlet Client DHC](https://chrome.google.com/webstore/detail/restlet-client-dhc/aejoelaoggembcahagimdiliamlcdmfm)
