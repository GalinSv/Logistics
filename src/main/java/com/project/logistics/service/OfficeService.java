package com.project.logistics.service;

import com.project.logistics.data.entity.Office;
import java.util.List;

public interface OfficeService {

    List<Office> getAll();

    Office create(Office office);
}
