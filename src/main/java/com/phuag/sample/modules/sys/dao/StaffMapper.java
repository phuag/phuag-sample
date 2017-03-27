package com.phuag.sample.modules.sys.dao;

import com.phuag.sample.common.persistence.CrudDao;
import com.phuag.sample.modules.sys.model.Staff;
import org.springframework.stereotype.Repository;

@Repository(value="staffMapper")
public interface StaffMapper extends CrudDao<Staff> {

}