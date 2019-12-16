package com.project.onlinestore.buyer.service;

import com.project.onlinestore.buyer.domain.Line;

public interface LineService {
    Line getLineById(Long id);
    void updateQty(int qty, Long lineId);
}
