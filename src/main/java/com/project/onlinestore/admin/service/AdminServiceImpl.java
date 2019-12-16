package com.project.onlinestore.admin.service;

import com.project.onlinestore.admin.domain.Ad;
import com.project.onlinestore.email.EmailService;
import com.project.onlinestore.security.domain.User;
import com.project.onlinestore.security.respository.RoleRepository;
import com.project.onlinestore.security.respository.UserRepository;
import com.project.onlinestore.seller.domain.Seller;
import com.project.onlinestore.seller.service.SellerService;
import com.project.onlinestore.utils.SecurityConstants;
import com.project.onlinestore.utils.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService  {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    SellerService sellerService;

    @Autowired
    FileService fileService;

    @Override
    public List<User> getPendingSellers() {
        return userRepository.findByActiveAndRoleId(0,SecurityConstants.ROLE_SELLER);
    }

    public void acceptSeller(Long sellerId){
        User user = userRepository.findById(sellerId).get();
        user.setActive(1);
        userRepository.save(user);
        Seller seller = new Seller();

        seller.setUser(user);
        sellerService.save(seller);

        emailService.sendPendingAcceptanceEmail(user);
    }

    public void rejectSeller(Long sellerId){
        User seller = userRepository.findById(sellerId).get();
        userRepository.delete(seller);
        emailService.sendPendingRejectEmail(seller);
    }

    @Override
    public void changeAd(Ad ad, String rootDirectory) {
        fileService.writeFile(rootDirectory+"//link.txt",ad.getLink());
        fileService.transferImage(rootDirectory+"//0",ad.getImage(),"gif");
    }

}
