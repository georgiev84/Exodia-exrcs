package exodia.service;

import exodia.domain.entities.User;
import exodia.repository.UserRepository;
import exodia.domain.models.service.UserServiceModel;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }



    @Override
    public boolean registerUser(UserServiceModel userServiceModel) {
        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(DigestUtils.shaHex(user.getPassword()));

        try{
            this.userRepository.saveAndFlush(user);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public UserServiceModel loginUser(UserServiceModel userServiceModel) {
//        User user = this.userRepository.findByUsername(userServiceModel.getUsername()).orElse(null);
//        if(user == null || !user.getPassword().equals(DigestUtils.shaHex(userServiceModel.getPassword()))){
//            return null;
//        }
//        return this.modelMapper.map(user, UserServiceModel.class);
//
        return this.userRepository.findByUsername(userServiceModel.getUsername())
                .filter(u -> u.getPassword().equals(DigestUtils.shaHex(userServiceModel.getPassword())))
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .orElse(null);
    }
}
