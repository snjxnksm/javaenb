package jp.co.example.common;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;


/**
 * 独自Repository実装
 *
 */
public class AppRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements AppRepository<T, ID> {

    private JpaEntityInformation<T, ID> entityInformation;
    @SuppressWarnings("unused")
	private final EntityManager entityManager;

    Method versionMethod;

    public AppRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);

        this.entityInformation = entityInformation;
        this.entityManager = entityManager;

        try {
        	// lockVersionがEntityに設けてあるか
            versionMethod = entityInformation.getJavaType().getMethod("getLockVersion");
        } catch (NoSuchMethodException | SecurityException e) {
        	// nop
        }
	}

    /**
     * 楽観的排他処理を使う場合のレコード取得
     *
     * @param id サロゲートキー
     * @param lockVersion 楽観ロック用バージョン値
     * @return
     */
    public T findOneWithValidVersion(ID id, Integer lockVersion) {

        if (versionMethod == null) {
        	// EntityがVersionを持っていない場合の処理
            throw new AppException(HttpStatus.NOT_IMPLEMENTED.value(),
	            		 String.format(
		                    		AppLogMessageConstants.DOSE_NOT_FOUNT_VERSION_FILED.getMessage(),
		                            entityInformation.getJavaType().getName()),
	            		new UnsupportedOperationException()
            		);
        }

        // 更新対象レコードを１件取得する。
        T entity = findOne(id);

        if (entity != null && lockVersion != null) {
            Integer currentVersion;
            try {
            	// 楽観排他用Versionの値を取得
                currentVersion = (Integer) versionMethod.invoke(entity);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            	// EntityからVersionが取得できなかった場合の処理
                throw new IllegalStateException(e);
            }
            if (!lockVersion.equals(currentVersion)) {
            	// 排他！
                throw new AppException(HttpStatus.CONFLICT.value(),
                		AppLogMessageConstants.RESOURCE_CONFLICT.getMessage(),
	            		new ObjectOptimisticLockingFailureException(
	                            entityInformation.getJavaType().getName(), id)
           		);
            }
        }

        return entity;
    }
}
