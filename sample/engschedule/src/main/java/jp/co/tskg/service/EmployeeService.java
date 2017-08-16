package jp.co.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jp.co.example.common.AppException;
import jp.co.example.entity.Employee;
import jp.co.example.model.CountChgRecordRes;
import jp.co.example.model.EmployeeReq;
import jp.co.example.model.EmployeeRes;
import jp.co.example.model.EmployeeWithSectionNameRes;
import jp.co.example.repository.EmployeeRepository;
import jp.co.example.repository.PracticeNativeSqlRepository;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository nomalRepos;    // 一般的なSpringJPAの作り方。

    // 従業員全件取得
    public List<EmployeeRes> findAll() {    //
    	List<EmployeeRes> list = new ArrayList<EmployeeRes>();
    	Iterable<Employee> itr = nomalRepos.findAll();
    	for(Employee rec: itr){
    		EmployeeRes res = new EmployeeRes();
    		BeanUtils.copyProperties(rec, res);
    		list.add(res);
    	}
        return list;
    }

    // 従業員一件取得
    public EmployeeRes findOne(int id) {    //
    	EmployeeRes res = new EmployeeRes();
    	Employee emp = nomalRepos.findOne(id);
    	if (emp == null) {
    		// たとえば、idにDBにない値を入れるとレコードを取得できない
    		// その場合に、独自エラーをあげる
    		throw new AppException(HttpStatus.NOT_FOUND.value(),"employee not fund. ");
    	}
		BeanUtils.copyProperties(emp,res);
        return res;
    }

    // 従業員一件作成
    public EmployeeRes create(EmployeeReq req) {    //
    	Employee rec = new Employee();
    	BeanUtils.copyProperties(req, rec);
    	rec.setIsFound(false);// 足りない部分は追加
    	EmployeeRes res = new EmployeeRes();
    	BeanUtils.copyProperties(nomalRepos.save(rec),res);
        return res;
    }

    // 従業員一件更新
    public EmployeeRes update(EmployeeReq req,int id) {    //
    	// versionを使った排他制御をする場合は
    	// 更新すべきレコードを以下のメソッドで特定すること
    	Employee rec = nomalRepos.findOneWithValidVersion(req.getId(), req.getLockVersion());
    	BeanUtils.copyProperties(req, rec);
    	Employee emp = nomalRepos.saveAndFlush(rec);

    	EmployeeRes res = new EmployeeRes();
    	BeanUtils.copyProperties(emp,res);
        return res;
    }

    // 従業員一件削除
    public void delete(int id) {    //
        nomalRepos.delete(id);
    }

    /**
     * 部署IDで抽出
     *
     * @param id
     * @return
     */
	public List<EmployeeRes> getEmployeesFromSedtionId(int id) {
    	ArrayList<EmployeeRes> list = new ArrayList<EmployeeRes>();
    	List<Employee> itr = nomalRepos.findBySectionId(id);
    	for(Employee rec: itr){
    		EmployeeRes res = new EmployeeRes();
    		BeanUtils.copyProperties(rec, res);
    		list.add(res);
    	}
        return list;
	}


    @Autowired
    PracticeNativeSqlRepository jpaRepoNotUseRepository; // ネイティブSQLを自分で書く方。
    // ネイティブSQLで検索。
	public List<EmployeeWithSectionNameRes> getJpaNotUse(Long sec_id) {
		List<EmployeeWithSectionNameRes> res =jpaRepoNotUseRepository.findBySectionIdWithSectionName(sec_id);
		return res;
	}
	// ネイティブSQLでUpdate
	public CountChgRecordRes postJpaNotUse(EmployeeReq req, Long sec_id) {
		CountChgRecordRes res = new CountChgRecordRes(jpaRepoNotUseRepository.updateBySectionIdWithSectionName(req,sec_id));
		return res;
	}

	public EmployeeRes findList(Object empIds) {
		return null;
	}

}
