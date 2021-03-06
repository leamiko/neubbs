package org.neusoft.neubbs.controller.api;

import org.neusoft.neubbs.constant.ajax.AjaxRequestStatus;
import org.neusoft.neubbs.constant.api.ApiMessage;
import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.controller.annotation.AccountActivation;
import org.neusoft.neubbs.controller.annotation.LoginAuthorization;
import org.neusoft.neubbs.dto.PageJsonDTO;
import org.neusoft.neubbs.entity.UserDO;
import org.neusoft.neubbs.service.IFileTreatService;
import org.neusoft.neubbs.service.IFtpService;
import org.neusoft.neubbs.service.IHttpService;
import org.neusoft.neubbs.service.ISecretService;
import org.neusoft.neubbs.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * 文件 api
 *      + 上传用户头像
 *
 * @author Suvan
 */
@RestController
@RequestMapping("/api/file")
public class FileController {

    private final IUserService userService;
    private final IFileTreatService fileService;
    private final IHttpService httpService;
    private final ISecretService secretService;
    private final IFtpService ftpService;

    @Autowired
    public FileController(IUserService userService, IFileTreatService fileService,
                          IHttpService httpService, ISecretService secretService,
                          IFtpService ftpService) {
        this.userService = userService;
        this.fileService = fileService;
        this.httpService = httpService;
        this.secretService = secretService;
        this.ftpService = ftpService;
    }

    /**
     * 上传用户头像
     *      - 新上传的会覆盖旧的
     *      - 上传至 ftp
     *
     * @param multipartFile 用户上传的文件对象
     * @return PageJsonDTO 响应JSON传输对象
     */
    @LoginAuthorization @AccountActivation
    @RequestMapping(value = "/avator", method = RequestMethod.POST)
    public PageJsonDTO uploadUserAvatars(@RequestParam("avatorImage")MultipartFile multipartFile) {

        fileService.checkUploadUserAvatorImageFileNorm(multipartFile);

        //compress picture, function no finish
//      MultipartFile compressedFile = fileService.compressFile(multipartFile)

        UserDO cookieUser = secretService.jwtVerifyTokenByTokenByKey(
                httpService.getAuthenticationCookieValue(), SetConst.JWT_TOKEN_SECRET_KEY
        );

        String serverUserAvatorImageName = ftpService.generateServerUserAvatorFileName(multipartFile);
        ftpService.uploadUserAvatorImage(
                ftpService.getServerPersonalUserAvatorDirectoryPath(cookieUser),
                serverUserAvatorImageName,
                multipartFile
        );

        userService.alterUserAvatorImage(cookieUser.getName(), serverUserAvatorImageName);

        return new PageJsonDTO(AjaxRequestStatus.SUCCESS, ApiMessage.UPLOAD_SUCCESS);
    }
}
