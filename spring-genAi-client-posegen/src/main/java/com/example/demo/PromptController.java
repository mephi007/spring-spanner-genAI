package com.example.demo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import jakarta.xml.bind.DatatypeConverter;

@RestController
public class PromptController {

    @Value("${get_pose_list_api}")
    private String get_pose_list;

    @Value("${token}")
    private String token;

    @Value("${genai_api}")
    private String genai_api;

    RestTemplate restTemplate = new RestTemplate();

    private static final Logger logger = Logger.getLogger(PromptController.class.getName());

    @GetMapping("/")
    public ModelAndView extractLabels(ModelMap map, Prompt prompt){
        List<String> promptList = getList();
        map.addAttribute("poselist", promptList);
        return new ModelAndView("index", map);
    }

    @GetMapping("/getimage")
    public ModelAndView getImage(Prompt prompt, ModelMap map){
        String promptRequest = "{'instances': [ { 'prompt': '" + prompt.getPrompt() + "' } ],'parameters': { 'sampleCount': 1} }";
        logger.warning(String.format("PROMPT REQUEST! %s", promptRequest));
        String image = callImagen(promptRequest);
        map.addAttribute("imagestring", image);
        return new ModelAndView("getimage", map);
    }

    public File callImageBuilder(String base64){
        byte[] data = DatatypeConverter.parseBase64Binary(base64);
        String path = "/src/main/resources/templates/" +"pose.jpg";
        File file = new File(path);
        try(OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            outputStream.write(data);
        } catch (Exception e) {
            // TODO: handle exception
            logger.warning("exception in writing image " + e);
        }
        return file;
    }

    public String callImagen(String promptRequest){
        
        // https://us-central1-aiplatform.googleapis.com/v1/projects/qualified-world-404707/locations/us-central1/publishers/google/models/imagegeneration:predict
        try{
            String jsonString = promptRequest;          
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer "+ token);
            headers.set("Content-Type", "application/json; charset=utf-8");
            HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> resultString = restTemplate.exchange(genai_api, HttpMethod.POST, entity, String.class);
            // System.out.println(resultString.getBody());
           String result = resultString.getBody();
            //process response
            JSONObject jsonObject = new JSONObject(result);
            String base64 = jsonObject.getJSONArray("predictions").getJSONObject(0).getString("bytesBase64Encoded");
            return base64;
        }catch(Exception e){
            logger.warning("exception in edit " + e);
            return null;
        }
    
    }

    public List<String> getList(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("parameters",headers);
        ResponseEntity<String> resultString = restTemplate.exchange(get_pose_list, HttpMethod.GET, entity, String.class);
        String responseString = resultString.getBody().toString();
        responseString = responseString.replace("[", "").replace("]", "").replace("},", "};");
        // String[] split = responseString.split(";");
        List<String> poses = Arrays.asList(responseString.split(";"));
        List<String> poseList =  poses.stream().map(
            (poseString)->{
                String[] pose = poseString.replace("{", "").replace("}", "").replace("\"", "").split(",");
                String name = pose[1].split(":")[1];
                String breath = pose[2].split(":")[1];
                String desc = pose[3].split(":")[1];
                return name;
            }).collect(Collectors.toList());
        return poseList;
    }
    
}
