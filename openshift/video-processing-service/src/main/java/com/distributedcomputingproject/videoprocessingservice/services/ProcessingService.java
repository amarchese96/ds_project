package com.distributedcomputingproject.videoprocessingservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProcessingService {

    @Autowired
    KafkaProducerService kafkaProducerService;

    public void processVideo(Integer id){
        try {
            Path outPath = Paths.get("/storage/var/videofiles/" + id + "/video.mpd");
            Files.createDirectories(outPath.getParent());
            String command = "ffmpeg -i /storage/var/video/" + id + "/video.mp4 -map 0:v:0 -map 0:a\\?:0 -map 0:v:0 -map 0:a\\?:0 -map 0:v:0 -map 0:a\\?:0 -map 0:v:0 -map 0:a\\?:0 -map 0:v:0 -map 0:a\\?:0 -map 0:v:0 -map 0:a\\?:0 -b:v:0 350k -c:v:0 libx264 -filter:v:0 \"scale=320:-1\" -b:v:1 1000k -c:v:1 libx264 -filter:v:1 \"scale=640:-1\" -b:v:2 3000k -c:v:2 libx264 -filter:v:2 \"scale=1280:-1\" -b:v:3 245k -c:v:3 libvpx-vp9 -filter:v:3 \"scale=320:-1\" -b:v:4 700k -c:v:4 libvpx-vp9 -filter:v:4 \"scale=640:-1\" -b:v:5 2100k -c:v:5 libvpx-vp9 -filter:v:5 \"scale=1280:-1\" -use_timeline 1 -use_template 1 -window_size 6 -adaptation_sets \"id=0,streams=v id=1,streams=a\" -f dash " + outPath;
            Files.write(Paths.get("./script.sh"), command.getBytes());
            System.out.println("Executing command");
            Process process = Runtime.getRuntime().exec("sh ./script.sh");
            int statusCode = process.waitFor();
            System.out.println("Process exit code: " + statusCode);
            System.out.println("Result:");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            if(statusCode != 1){
                System.out.println("Video processed successfully");
                kafkaProducerService.sendProcessedMessage(id);
            }
            else{
                System.out.println("Error in processing video");
                kafkaProducerService.sendProcessingFailedMessage(id);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Error in processing video");
            kafkaProducerService.sendProcessingFailedMessage(id);
        }
    }
}
