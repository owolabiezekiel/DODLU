package com.dodlu.core.persistence.savings.repository;

import com.dodlu.core.persistence.savings.entity.GroupSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupSettingRepository extends JpaRepository<GroupSetting, String> {}
