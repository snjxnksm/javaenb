package jp.co.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jp.co.example.model.CountChgRecordRes;
import jp.co.example.model.EmployeeReq;
import jp.co.example.model.EmployeeRes;
import jp.co.example.model.EmployeeWithSectionNameRes;
import jp.co.example.service.EmployeeService;

@RestController
@Transactional
@RequestMapping("/test/employees")
public class EmployeeController {
    @Autowired    //
    EmployeeService employeeService;    //

    // 従業員全件取得
    @RequestMapping(method=RequestMethod.GET)    //
    public List<EmployeeRes> getEmployee() {
        return employeeService.findAll();
    }

    // 従業員一件取得GET
    @RequestMapping(method=RequestMethod.POST, value="id/{id}")    //
    public EmployeeRes getEmployeeGET(@PathVariable int id) {
        return employeeService.findOne(id);
    }

    // 従業員一件取得POST
    @RequestMapping(method=RequestMethod.POST, value="get")    //
    public EmployeeRes getEmployeePOST(@RequestBody EmployeeReq req) {
        return employeeService.findOne(req.getId());
    }

    // 従業員一件作成
    @RequestMapping(method=RequestMethod.POST)    //
    @ResponseStatus(HttpStatus.CREATED)    //
    public EmployeeRes postEmployee(@RequestBody EmployeeReq req) {
        return employeeService.create(req);
    }

    // 従業員一件更新
    @RequestMapping(method=RequestMethod.PUT, value="{id}")    //
    public EmployeeRes putEmployee(@PathVariable int id,
    		@RequestBody EmployeeReq req) {
        return employeeService.update(req,id);
    }

    // 従業員一件削除
    @RequestMapping(method=RequestMethod.DELETE, value="{id}")    //
    @ResponseStatus(HttpStatus.NO_CONTENT)    //
    public void deleteEmployee(@PathVariable int id) {
        employeeService.delete(id);
    }

    // 部署IDを指定して、部署名と従業員名のペアを作る
    @RequestMapping(method=RequestMethod.GET, value="section/{id}")    //
    public  List<EmployeeRes> getEmployeesFromSedtionId(@PathVariable int id) {
        return employeeService.getEmployeesFromSedtionId(id);
    }

    // ネイティブSQLで問い合わせ(JpaRepositoryを使わない版)
    @RequestMapping(method=RequestMethod.GET, value="jpanotuse/{sec_id}")    //
    public List<EmployeeWithSectionNameRes> getjpanotuse(@PathVariable Long sec_id) {
        return employeeService.getJpaNotUse(sec_id);
    }

    // ネイティブSQLでデータ更新(JpaRepositoryを使わない版)
    @RequestMapping(method=RequestMethod.POST, value="jpanotuse/{sec_id}")    //
    public CountChgRecordRes postjpanotuse(@PathVariable Long sec_id, @RequestBody EmployeeReq req) {
        return employeeService.postJpaNotUse(req,sec_id);
    }
}
