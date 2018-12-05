package com.etalk.crm.controller;

import com.etalk.crm.utils.OssGetObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jordan
 */
@Controller
@RequestMapping(value = "/train",produces ="text/json;charset=UTF-8")
public class TrainingController {
    protected static final Logger logger= LogManager.getLogger(TrainingController.class);

    @RequestMapping("/etalk")
    public String main(Model model,String path){
        String rootPath="video/train/etalk/";
        List<String> listFiles=OssGetObject.getFiles(rootPath+(StringUtils.isEmpty(path)?"":path));
        //logger.info(listFiles.toString());
        if (listFiles!=null){
            List<Map<String,String>> listFileMap=new ArrayList<>();
            Map<String,String> fileMap;
            String file;
            for (int i=0;i<listFiles.size();i++){
                file=listFiles.get(i);
                fileMap=new HashMap<>();
                if (file.lastIndexOf(".")>-1) {
                    fileMap.put("name", file.substring(file.lastIndexOf("/") + 1, file.lastIndexOf(".")));
                    fileMap.put("type", file.substring(file.lastIndexOf(".") + 1).toLowerCase());
                    fileMap.put("path", file);
                }else {
                    String fileName = file.substring(0, file.length() - 1).replace(rootPath,"/");
                    if (i==0){
                        if (!file.equalsIgnoreCase(rootPath)) {
                            fileMap.put("name", "返回上一级");
                            fileMap.put("type", "goback");
                            fileMap.put("path", fileName == "" ? "" : fileName.substring(0, fileName.lastIndexOf("/")));
                        }
                    }else {

                        fileMap.put("name", fileName.substring(fileName.lastIndexOf("/") + 1));
                        fileMap.put("type", "folder");
                        fileMap.put("path", fileName.replace(rootPath,""));
                    }
                }
                if (!fileMap.isEmpty()) {
                    listFileMap.add(fileMap);
                }
            }
            model.addAttribute("listFiles",listFileMap);
	        //logger.info(listFileMap.toString());
        }

        if (!StringUtils.isEmpty(path)){
            int startNum=path.lastIndexOf("/");
            if (startNum>-1 && startNum<(path.length()-1)) {
                model.addAttribute("goBack", rootPath + path.substring(0, startNum));
            }
        }
        //logger.info(model.asMap().toString());
        return "training/training_list";
    }

    @RequestMapping("/play")
    public String play(Model model,@RequestParam("videoPath") String path){
        model.addAttribute("videoPath",path);
        return "training/play";
    }
}
