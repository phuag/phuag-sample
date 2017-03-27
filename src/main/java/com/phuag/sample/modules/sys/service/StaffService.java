package com.phuag.sample.modules.sys.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.phuag.sample.modules.sys.dao.StaffMapper;
import com.phuag.sample.modules.sys.model.Staff;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2015/11/17 0017.
 */
@Service
@Transactional
public class StaffService {

    @Resource
    private StaffMapper staffMapper;

    public int insertStaff(Staff staff) {
        staff.preInsert();
        return staffMapper.insert(staff);
    }

    public Staff getStaffById(String staffId) {
        return staffMapper.selectByPrimaryKey(staffId);
    }

    public PageInfo<Staff> getAllStaff(Pageable page) {
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        //TODO need to add sort function ,but the pagerhelper is not support well.
        //PageHelper.orderBy("STAFF_NAME desc");
        List<Staff> staffs = staffMapper.selectAll();
        return new PageInfo<Staff>(staffs);
    }


}
