package com.iqbal.sekawan.technical_test.service;

import com.iqbal.sekawan.technical_test.model.Roles;
import com.iqbal.sekawan.technical_test.statval.ERole;

public interface IRoleService {

    Roles getOrSave(ERole role);
}
