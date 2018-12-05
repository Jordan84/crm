package com.etalk.crm.utils;

import java.io.*;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;

import com.aliyun.oss.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.BinaryUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

/**
 * This sample demonstrates how to upload/download an object to/from 
 * Aliyun OSS using the OSS SDK for Java.
 */
@Component
public class OssGetObject {
	protected static final Logger logger = LogManager.getLogger(OssGetObject.class);

	private static String appActive;
	/**
	 * 外网链接
	 */
    private static String endpointDev = "http://oss-cn-hongkong.aliyuncs.com";
	/**
	 * 内网连接
	 */
    private static String endpoint = "http://oss-cn-hongkong-internal.aliyuncs.com";
    private static String accessKeyId = "s3hlGZSUwlvnDVmF";
    private static String accessKeySecret = "gSJ861b26EBGuMuDML30WWrYJ9lq6W";    
    private static String bucketName = "materials-img-hk";
    private static OSSClient client = null;
    
    public int updateCfg(String path) throws IOException {
    	int num=0;
    	        
        try {
        	if (appActive=="pro") {
				client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			}else {
				client = new OSSClient(endpointDev, accessKeyId, accessKeySecret);
			}
            if (!client.doesBucketExist(bucketName)) {
                num=-1;
            }else{
            	boolean found = client.doesObjectExist(bucketName, path.lastIndexOf("/")==(path.length()-1)?path:(path+="/"));
            	if(found){
		            List<String> list=createCfg(path);
		            num=1;
		            while(list!=null && list.size()>0) {
		            	list.addAll(createCfg(list.get(0)));
		            	list.remove(0);
		            	num+=1;
		            }
            	}else{
            		num=-2;
            	}
            }
        } catch (OSSException oe) {
        	logger.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
        	logger.error("Error Message: " + oe.getErrorCode());
        	logger.error("Error Code:       " + oe.getErrorCode());
        	logger.error("Request ID:      " + oe.getRequestId());
        	logger.error("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
        	logger.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            logger.error("Error Message: " + ce.getMessage());
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            client.shutdown();
        }
		return num;
    }
    
    private static List<String> createCfg(String path) throws UnsupportedEncodingException {
    	List<String> list=new ArrayList<>();
    	List<Map<String,Object>> listMap=new ArrayList<>();
    	ObjectListing objectListing = null;
    	List<OSSObjectSummary> listObjectSummary=new ArrayList<>();
    	final int maxKeys = 500;
    	String nextMarker = null;
    	do {
    		//获取所有文件
	    	// 构造ListObjectsRequest请求
	        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
			//每次最大取文件数
	        listObjectsRequest.setMaxKeys(maxKeys);
	        listObjectsRequest.setMarker(nextMarker);
	        // 指定Object名称编码传输
	        listObjectsRequest.setEncodingType("url");
	        // "/" 为文件夹的分隔符
	        listObjectsRequest.setDelimiter("/");
	        // 列出fun目录下的所有文件和文件夹
	        listObjectsRequest.setPrefix(path);
	        
	        objectListing =  client.listObjects(listObjectsRequest);
	        
	        listObjectSummary.addAll(objectListing.getObjectSummaries());
	        if(objectListing.getNextMarker()!=null) {
	        	nextMarker = URLDecoder.decode(objectListing.getNextMarker(), "UTF-8");
	        }
    	}while (objectListing.isTruncated());
    	
        //文件排序
        fileNameSort(listObjectSummary);
       
    	Map<String,Object> bookMap=null;
		for(OSSObjectSummary objectSummary : listObjectSummary){			
			//如果是文件
			if(objectSummary.getKey().toLowerCase().lastIndexOf(".jpg")>0){
				bookMap=new HashMap<>();
				bookMap.put("t", "f");
				String strName=URLDecoder.decode(objectSummary.getKey(), "UTF-8");
				bookMap.put("n", strName.substring(strName.lastIndexOf("/")+1));
				bookMap.put("l", String.valueOf(objectSummary.getSize()));
				bookMap.put("m", String.valueOf(objectSummary.getLastModified().getTime()/1000));
				
				listMap.add(bookMap);
				
			}
		}
        // 遍历所有CommonPrefix
        for (String commonPrefix : objectListing.getCommonPrefixes()) {
        	bookMap=new HashMap<>();
			bookMap.put("t", "d");
			String strName=URLDecoder.decode(commonPrefix, "UTF-8");
			bookMap.put("n", strName.substring(strName.substring(0,strName.length()-1).lastIndexOf("/")+1));	        				
			listMap.add(bookMap);
			//子目录返回列表
            list.add(commonPrefix);
        }       
    	
        if(listMap.size()>0) {
        	String content = JSON.toJSONString(listMap);

        	// 创建上传Object的Metadata
        	ObjectMetadata meta = new ObjectMetadata();
        	// 设置上传文件长度
        	meta.setContentLength(content.length());
        	// 设置上传MD5校验
        	meta.setContentMD5(BinaryUtil.toBase64String(BinaryUtil.calculateMd5(content.getBytes())));
        	// 设置上传内容类型
        	meta.setContentType("text/json");
        	//设置文件编码格式
        	meta.setContentEncoding("UTF-8");
        	//设置最后修改时间
        	meta.setLastModified(new Date());
        	// 上传字符串
        	client.putObject(bucketName, path+"cfg.json", new ByteArrayInputStream(content.getBytes()),meta);
        }
        
        return list;
    }

	/**
	 * 获取目录下所有子目录
	 * @param path 路径
	 * @return 所有对象列表
	 * @throws UnsupportedEncodingException
	 */
    public static List<String> getDirList(String path) throws UnsupportedEncodingException {
    	List<String> list=new ArrayList<>();
    	try{
			if (appActive=="pro") {
				client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			}else {
				client = new OSSClient(endpointDev, accessKeyId, accessKeySecret);
			}
	        if (client.doesBucketExist(bucketName)) {
	        	boolean found = client.doesObjectExist(bucketName, path.lastIndexOf("/")==(path.length()-1)?path:(path+="/"));
	        	if(found){		    	
			    	list=searchDirList(path);
			    	if(list!=null && list.size()>0){
			    		for (int i = 0; i < list.size(); i++) {
							list.addAll(searchDirList(list.get(i)));
						}
			    	}
	        	}
	        }
    	} catch (OSSException oe) {
        	logger.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
        	logger.error("Error Message: " + oe.getErrorCode());
        	logger.error("Error Code:       " + oe.getErrorCode());
        	logger.error("Request ID:      " + oe.getRequestId());
        	logger.error("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
        	logger.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            logger.error("Error Message: " + ce.getMessage());
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            client.shutdown();
        }
        return list;
    }
    
    private static List<String> searchDirList(String path) throws UnsupportedEncodingException {
    	ObjectListing objectListing = null;
    	List<OSSObjectSummary> listObjectSummary=new ArrayList<>();
    	final int maxKeys = 200;
    	String nextMarker = null;
    	do {
    		//获取所有文件
	    	// 构造ListObjectsRequest请求
	        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
			//每次最大取文件数
	        listObjectsRequest.setMaxKeys(maxKeys);
	        listObjectsRequest.setMarker(nextMarker);
	        // 指定Object名称编码传输
	        //listObjectsRequest.setEncodingType("url");
	        // "/" 为文件夹的分隔符
	        listObjectsRequest.setDelimiter("/");
	        // 列出path目录下的所有文件和文件夹
	        listObjectsRequest.setPrefix(path);
	        
	        objectListing =  client.listObjects(listObjectsRequest);
	        
	        listObjectSummary.addAll(objectListing.getObjectSummaries());
	        if(objectListing.getNextMarker()!=null) {
	        	nextMarker = URLDecoder.decode(objectListing.getNextMarker(), "UTF-8");
	        }
    	}while (objectListing.isTruncated());
    	
    	return objectListing.getCommonPrefixes();
    	
    }

	/**
	 * 获取cfg文件数据
	 * @param path 文件路径
	 * @return 文件内容字符串
	 */
    public static String getCfgJson(String path){
    	
    	try{
			if (appActive=="pro") {
				client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			}else {
				client = new OSSClient(endpointDev, accessKeyId, accessKeySecret);
			}
	        if (client.doesBucketExist(bucketName)) {
	        	boolean found = client.doesObjectExist(bucketName, path);
	        	if(found){		    	
	        		OSSObject ossObject = client.getObject(bucketName, path);
	        		BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));	        		
	        		StringBuffer content=new StringBuffer();
	        		while (true) {
	        		    String line = reader.readLine();
	        		    if (line == null){ break;}
	        		    content.append(line);
	        		    //System.out.println("\n" + line);
	        		}
	        		reader.close();
	        		ossObject.close();
	        		return content.toString();
	        	}
	        }
    	} catch (OSSException oe) {
        	logger.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
        	logger.error("Error Message: " + oe.getErrorCode());
        	logger.error("Error Code:       " + oe.getErrorCode());
        	logger.error("Request ID:      " + oe.getRequestId());
        	logger.error("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
        	logger.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            logger.error("Error Message: " + ce.getMessage());
        } catch (IOException e) {
			// TODO Auto-generated catch block
        	logger.error(e);
		} finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            client.shutdown();
        }
        return "";
    }
    /**
	 * 文件按名称排序
	 * 参数：listfile 必须是ArrayList类型
     * @throws UnsupportedEncodingException 
	 */
	private static void fileNameSort(List<OSSObjectSummary> listObjectSummary) throws UnsupportedEncodingException{
		OSSObjectSummary tempfile=null;
		String tempName;
		listObjectSummary.remove(0);
		for(int i=0;i<listObjectSummary.size();i++){
			tempfile = listObjectSummary.get(i);
			tempName=URLDecoder.decode(tempfile.getKey(), "UTF-8");
			tempName = tempName.substring(tempName.lastIndexOf("/")+1,tempName.lastIndexOf(".")).toLowerCase();			
			//tempName = tempName.substring(tempName.indexOf("p")==-1?0:(tempName.indexOf("p")+1));
			tempName = tempName.replaceFirst("p", "");
			tempName = tempName.replace("@", ".");
			if(tempName.matches("^(\\-|\\+)?\\d+(\\.\\d+)?$")) {

				int removeId=0;
				for(int j=i+1;j<listObjectSummary.size();j++){
					String str2=URLDecoder.decode(listObjectSummary.get(j).getKey(), "UTF-8");
					str2 = str2.substring(str2.lastIndexOf("/")+1,str2.lastIndexOf(".")).toLowerCase();
					str2 = str2.replaceFirst("p", "");
					str2 = str2.replace("@", ".");

					if(str2.matches("^(\\-|\\+)?\\d+(\\.\\d+)?$")) {
						if(new BigDecimal(tempName).compareTo(new BigDecimal(str2))==1){
							tempName=str2;
							tempfile=listObjectSummary.get(j);
							removeId=j;
						}
						if(removeId>0 && j==listObjectSummary.size()-1){
							listObjectSummary.remove(removeId);
							listObjectSummary.add(i, tempfile);
						}
					}else {
						listObjectSummary.remove(j);
						--j;
					}
				}
			}else {
				listObjectSummary.remove(i);
				--i;
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	/**
	 * 获取某个路径下的所有文件
	 */
	public static List<String> getFiles(String path){
		if (appActive=="pro") {
			client = new OSSClient("http://oss-cn-shenzhen-internal.aliyuncs.com", accessKeyId, accessKeySecret);
		}else {
			client = new OSSClient("http://oss-cn-shenzhen.aliyuncs.com", accessKeyId, accessKeySecret);
		}
		//client = new OSSClient("http://oss-cn-shenzhen.aliyuncs.com", accessKeyId, accessKeySecret);

    	List<String> list=new ArrayList<>();
    	ObjectListing objectListing;
    	List<OSSObjectSummary> listObjectSummary=new ArrayList<>();
    	final int maxKeys = 500;
    	String nextMarker = null;
    	do {
    		//获取所有文件
	    	// 构造ListObjectsRequest请求
	        ListObjectsRequest listObjectsRequest = new ListObjectsRequest("web-resources-sz");
			//每次最大取文件数
	        listObjectsRequest.setMaxKeys(maxKeys);
	        listObjectsRequest.setMarker(nextMarker);
	        // 指定Object名称编码传输
	        listObjectsRequest.setEncodingType("url");
	        // "/" 为文件夹的分隔符
	        listObjectsRequest.setDelimiter("/");
	        // 列出目录下的所有文件和文件夹
	        listObjectsRequest.setPrefix(path.endsWith("/")?path:path+"/");
	        
	        objectListing =  client.listObjects(listObjectsRequest);
	        
	        listObjectSummary.addAll(objectListing.getObjectSummaries());
	        if(objectListing.getNextMarker()!=null) {
				try {
					nextMarker = URLDecoder.decode(objectListing.getNextMarker(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					logger.info(e.getMessage(),e);
				}
			}
    	}while (objectListing.isTruncated());
    	
		for(OSSObjectSummary objectSummary : listObjectSummary){			
			//如果是文件
			list.add(URLDecoder.decode(objectSummary.getKey()));
			
		}
		// 遍历所有CommonPrefix
		//System.out.println("CommonPrefixs:");
		for (String commonPrefix : objectListing.getCommonPrefixes()) {
			list.add(1, URLDecoder.decode(commonPrefix).replace("video/train/etalk/", ""));
		}
		return list;
	}

	/**
	 * 上传老师图像
	 * @param path 文件保存路径，包含文件名
	 * @param base64string 图片数据
	 * @return 上传结果
	 */
	public static boolean uploadHeadImg(String path,String base64string){
		// 创建OSSClient实例。
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes1;
        try {
            bytes1 = decoder.decodeBuffer(base64string);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            return false;
        }
		PutObjectResult result=ossClient.putObject("web-resources-hk", path, new ByteArrayInputStream(bytes1));
		// 关闭OSSClient。
		ossClient.shutdown();
		return result.getResponse().isSuccessful();
	}
	/**
	 * 删除老师图像
	 * @param path 文件保存路径，包含文件名
	 * @return 上传结果
	 */
	public static void deleteHeadImg(String path){
		// 创建OSSClient实例。
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		ossClient.deleteObject("web-resources-hk", path);
		// 关闭OSSClient。
		ossClient.shutdown();
	}

    public static void main(String[] args) throws IOException {
        /*
         * Constructs a client instance with your account for accessing OSS
         */
    	OSSClient client = new OSSClient("http://oss-cn-shenzhen.aliyuncs.com", accessKeyId, accessKeySecret);
        try {        	
            /*
             * Download an object from your bucket
             */
        	String key = "video/train/etalk/";
            System.out.println("Downloading an object");

            System.out.println(getFiles(key));

            
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce);
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            client.shutdown();
        }
    }
	@Value("${spring.profiles.active}")
	public void setAppActive(String appActive) {
		OssGetObject.appActive = appActive;
	}
}
