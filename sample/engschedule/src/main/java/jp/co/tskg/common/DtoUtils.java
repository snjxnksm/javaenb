package jp.co.example.common;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.BeanUtils;

public class DtoUtils {

	/**
	 * DTOリストを別のDTOリストに移送します。
	 * DBから取得したDTOリストの内容をResultDTOリストに移送する際などに使用する。
	 * レコード毎の処理がいらない場合はこちら。
	 *
	 * @param srcDtoList DB結果DTO
	 * @param destDtoClass ResultDTOクラス
	 * @return 移送されたResultDTOリスト
	 */
	public static <T, E> List<E> copyDtoList(List<T> srcDtoList, Class<E> destDtoClass) {
		return copyDtoList(srcDtoList, destDtoClass, entry -> {});
	}

	/**
	 * DTOリストを別のDTOリストに移送します。
	 * DBから取得したDTOリストの内容をResultDTOリストに移送する際などに使用する。
	 *
	 * @param srcDtoList 移送元DTO（DBから取得したDTOなど）
	 * @param destDtoClass 移送先DTOクラス（ResultDTOなど）
	 * @param handler 移送先DTOに対し、DBとは別に値を設定するなど処理を加える場合に使用する。ラムダ式が使用できる。
	 * @return 移送されたDTOリスト
	 */
	public static <T, E> List<E> copyDtoList(List<T> srcDtoList, Class<E> destDtoClass, Consumer<E> handler) {
    	List<E> list = new ArrayList<E>();
		srcDtoList.forEach(dto -> {
			E result;
			try {
				result = destDtoClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new IllegalArgumentException(e);
			}
			BeanUtils.copyProperties(dto, result);
			handler.accept(result);
			list.add(result);
		});
		return list;
	}

}
