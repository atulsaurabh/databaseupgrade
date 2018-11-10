package com.technoride.server.databaseupgrade.client;

import com.technoride.server.databaseupgrade.dto.Parameter;
import com.technoride.server.databaseupgrade.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


public class RestClient
{

    @Autowired
    private FileUtil fileUtil;

    public RestClient()
    {

    }

    private String getBaseURI()
    {
        Map<String,Parameter> parameterMap = fileUtil.populateProperty();
        Parameter serverAddress = parameterMap.get("REST_SERVER_IP_ADDRESS");
        Parameter serverport = parameterMap.get("REST_SERVER_PORT");
        String url = "http://"+serverAddress.getValue()+":"+serverport.getValue();
        return url;
    }

    public long getVersion()
    {
        String versionURL = getBaseURI()+PublicURI.VERSION_CONTROLLER_URI;
        RestTemplate template= new RestTemplate();
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        long version = template.getForObject(versionURL,Long.class);
        return version;
    }


    public Resource getFile()
    {
        String fileURL = getBaseURI()+PublicURI.CURRENT_FILE_CONTROLLER_URI;
        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        ResponseEntity<Resource> responseEntity = template.getForEntity(fileURL,Resource.class);
        Resource rs = responseEntity.getBody();
        return rs;
    }


}
