package org.neusoft.neubbs.service.impl;

import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.log.LogWarn;
import org.neusoft.neubbs.exception.FtpServiceErrorException;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IFtpService;
import org.neusoft.neubbs.utils.FtpUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * IFtpService 实现类
 *
 * @author Suvan
 */
@Service("ftpServiceImpl")
public class FtpServiceImpl implements IFtpService {

    @Override
    public void registerUserCreatePersonDirectory(UserDO user) {
        try {
            FtpUtil.createDirectory("/user/" + user.getId() + "-" + user.getName() + "/avator");
        } catch (IOException ioe) {
            throw new FtpServiceErrorException(ApiMessage.FTP_SERVICE_EXCEPTION).log(LogWarn.FTP_01);
        }
    }

    @Override
    public void uploadUserAvatorImage(String ftpPath, String fileName, MultipartFile multipartFile) {
        try {
            FtpUtil.uploadFile(ftpPath, fileName, multipartFile.getInputStream());
        } catch (IOException ioe) {
            throw new FtpServiceErrorException(ApiMessage.FTP_SERVICE_EXCEPTION).log(LogWarn.FTP_02);
        }
    }

    @Override
    public String getServerPersonalUserAvatorDirectoryPath(UserDO user) {
        return "/user/" + user.getId() + "-" + user.getName() + "/avator/";
    }

    @Override
    public String generateServerUserAvatorFileName(MultipartFile multipartFile) {
        return System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
    }
}
