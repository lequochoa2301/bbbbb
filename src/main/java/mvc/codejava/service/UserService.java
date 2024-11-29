package mvc.codejava.service;

import mvc.codejava.entity.User;
import mvc.codejava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for username: " + username));
    }

    public void save(User user) {
        // Kiểm tra nếu mật khẩu không rỗng thì mã hóa trước khi lưu
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(encodePassword(user.getPassword()));
        }
        userRepository.save(user);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean isUsernameExist(String username) {
        // Kiểm tra xem người dùng với email này đã tồn tại trong cơ sở dữ liệu hay chưa
        return userRepository.findByUsername(username).isPresent();
    }

    public void updateUserDetails(User updatedUser, User currentUser) {
        // Không cho phép thay đổi email
        updatedUser.setEmail(currentUser.getEmail());
        // Không cho phép thay đổi email
        updatedUser.setEnabled(currentUser.isEnabled());
        // Cập nhật họ và tên
        if (updatedUser.getFullName() != null && !updatedUser.getFullName().isEmpty()) {
            currentUser.setFullName(updatedUser.getFullName());
        }

        if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
            currentUser.setUsername(updatedUser.getUsername());
        }

        // Cập nhật mật khẩu nếu có
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            currentUser.setPassword(encodePassword(updatedUser.getPassword()));
        }

        // Lưu thông tin người dùng đã cập nhật vào cơ sở dữ liệu
        userRepository.save(currentUser);

        // Gửi email thông báo sau khi cập nhật thành công
        emailService.sendAccountUpdateEmail(currentUser.getEmail());
    }
}


