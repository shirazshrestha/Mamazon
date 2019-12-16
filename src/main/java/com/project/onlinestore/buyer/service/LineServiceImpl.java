package com.project.onlinestore.buyer.service;

import com.project.onlinestore.buyer.domain.Line;
import com.project.onlinestore.buyer.repository.LineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class LineServiceImpl implements LineService {

    @Autowired
    LineRepository lineRepository;

    @Override
    public Line getLineById(Long id) {
        return lineRepository.findById(id).get();
    }

    @Override
    public void updateQty(int qty, Long lineId) {
        Line line = this.lineRepository.findById(lineId).get();
        line.setQty(qty);
        lineRepository.save(line);
    }
}
