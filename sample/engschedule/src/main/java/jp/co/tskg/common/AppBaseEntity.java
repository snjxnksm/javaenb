package jp.co.example.common;

import java.time.LocalDateTime;

import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;

/**
 * 各テーブルで必ず持つ必要のある項目を並べる
 *
 * @author user01
 *
 */
@Data
@MappedSuperclass
public abstract class AppBaseEntity {

    @Id
    @GeneratedValue
    private int id;

    @Version
    private int lockVersion;

    // 日時に対しては、DBはTimestamp型でつくる。
    // JavaのEntityには、TimestampとLocalDateTimeのコンバータを用意する。
    // Java内部ではLocalDateTimeとして処理する。

    @CreatedDate
    @Convert(converter = AppLocalDateTimeToTimestampConverter.class)
    private LocalDateTime  createdAt;

    @LastModifiedDate
    @Convert(converter = AppLocalDateTimeToTimestampConverter.class)
    private LocalDateTime  updatedAt;

//    //TODO:
//    /** 登録者 */
//    private String creatorCd;
//    /** 登録プログラムID */
//    private String creatorPgm;
//    /** 更新者 */
//    private String updaterCd;
//    /** 更新プログラムID */
//    private String updaterPgm;

}
