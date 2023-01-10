package com.example.petbutler.utils;

import com.example.petbutler.config.ServerConfig;
import com.example.petbutler.utils.wrapper.FilePath;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileUploadUtils {

  private final ServerConfig serverConfig;

  public FilePath copyAndPasteThumbnailFile(MultipartFile file) {

    if (Objects.nonNull(file)) {

      String localRoot = serverConfig.getFileLocalRoot();
      String urlRoot   = serverConfig.getFileUrlRoot();

      try{

        return this.saveFile(file, file.getOriginalFilename(), localRoot, urlRoot);

      } catch(IOException e) {

        log.info(e.getMessage());

      }
    }

    return null;
  }

  public List<FilePath> copyAndPasteThumbnailFiles(MultipartFile[] files) {

    List<FilePath> filePaths = new ArrayList();

    if (Objects.nonNull(files)) {

      String localRoot = serverConfig.getFileLocalRoot();
      String urlRoot   = serverConfig.getFileUrlRoot();

      for (MultipartFile file : files) {

        try{

          if (Objects.nonNull(file)) {
            FilePath filePath
                = this.saveFile(file, file.getOriginalFilename(), localRoot, urlRoot);
            filePaths.add(filePath);
          } else {
            filePaths.add(new FilePath("",""));
          }

        } catch(IOException e) {

          log.info(e.getMessage());

        }
      }
    }

    return filePaths;
  }

  public FilePath saveFile(MultipartFile file,
      String originalPath, String localRootPath, String urlRootPath)
      throws IOException {

    // create new file path
    // - 2022 > 11 > 1 > UUID file name
    FilePath newFilePath = createNewFilePath(originalPath, localRootPath, urlRootPath);

    // copy original file to new local path
    FileCopyUtils.copy(file.getInputStream(),
        Files.newOutputStream(new File(newFilePath.getLocalPath()).toPath()));

    return newFilePath;

  }

  public FilePath createNewFilePath(String originalPath, String localRootPath, String urlRootPath){

    // make directory
    // - 2022 > 11 > 1 > UUID file name
    LocalDate today = LocalDate.now();
    int year  = today.getYear();
    int month = today.getMonthValue();
    int day   = today.getDayOfMonth();

    String yearDir = String.format("%s/%d", localRootPath, year);
    String monthDir = String.format("%s/%d/%d", localRootPath, year, month);
    String dayDir = String.format("%s/%d/%d/%d", localRootPath, year, month, day);
    String[] dirs = new String[]{yearDir, monthDir, dayDir};

    for (String dir : dirs) {
      File file = new File(dir); // dir path

      if (!file.isDirectory()) { // if not exists
        file.mkdir();            // make directory
      }
    }

    // create new file name via UUID & add original file extension
    String uuidFileName  = UUID.randomUUID().toString().replace("-","");
    String fileExtension = originalPath.substring(originalPath.lastIndexOf(".") + 1);
    String newFileName   = String.format("%s.%s", uuidFileName, fileExtension);

    // set url path via date
    String newFilePath = String.format("%s/%d/%d/%d/%s", localRootPath, year, month, day, newFileName);
    String newUrlPath  = String.format("%s/%d/%d/%d/%s", urlRootPath, year, month, day, newFileName);

    return new FilePath(newFilePath, newUrlPath);
  }

}
