package com.ncu.outpatient.controller;

import com.ncu.common.utils.IdGenerator;
import com.ncu.common.utils.impl.OutPatientIdGr;
import com.ncu.outpatient.service.DepartmentService;
import com.ncu.outpatient.service.OutPatientService;
import com.ncu.pojo.common.OutPatient;
import com.ncu.pojo.common.Result;
import com.ncu.pojo.common.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author : 城南有梦
 * @date : 2020-07-12 08:05:09
 * @description:
 * 门诊Controller
 */
@RestController
@RequestMapping(value = "/api")
public class OutPatientController {
    @Autowired
    private OutPatientService outPatientService;
    @Autowired
    private DepartmentService departmentService;

    /**
     * 病人门诊挂号
     * @param outPatient
     * @return
     */
    @PostMapping(value = "/outpatients")
    public Result<String> makeAppointment(@RequestBody OutPatient outPatient){
        Result<String> result = new Result<>();
        IdGenerator idGenerator = new OutPatientIdGr();
        //生成用户挂号id,并写入outPatient对象
        //获取科室编号
        String code = departmentService.findById(outPatient.getDepartId()).getDepartCode();
        outPatient.setOutpatientId(idGenerator.generateId(code));
        String id = outPatient.getOutpatientId();
        //设置状态为挂号
        outPatient.setStatus("0");
        //设置挂号时间
        outPatient.setRegisterTime(new Date());
        if(outPatientService.makeAppointment(outPatient)!=0){
            //挂号成功 返回用户所挂的号
            result.setData(id);
        }else{
            //挂号失败
            result.setCode(StatusCode.ERROR);
            result.setFlag(false);
            result.setMessage("挂号失败");
            result.setData(null);
        }
        return result;
    }

    /**
     * 用户退号
     * @param outPatient
     * @param id
     * @return
     */
    @RequestMapping(value = "/outpatients/{id}",method = RequestMethod.PUT)
    public Result<String> giveUpNumber(@RequestBody OutPatient outPatient,@PathVariable("id") String id){
        Result<String> result = new Result<>();
        //设置id
        outPatient.setOutpatientId(id);
        if(outPatientService.giveUpNumber(outPatient)!=0){
            //更新成功
            result.setData("退号成功");
        }else{
            //更新失败
            result.setCode(StatusCode.ERROR);
            result.setFlag(false);
            result.setMessage("退号失败");
            result.setData(null);
        }
        return result;
    }
}
