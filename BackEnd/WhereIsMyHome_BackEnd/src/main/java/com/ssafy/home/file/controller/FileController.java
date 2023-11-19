package com.ssafy.home.file.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.file.model.ImgInfoDto;
import com.ssafy.home.file.model.service.FileService;

@CrossOrigin(origins = { "*" }, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE} , maxAge = 6000)
@RestController
@RequestMapping("/file")
public class FileController {
	
	@Autowired
	FileService fileService;

	// 게시판 이미지 저장 경로(ref: application.properties)
	@Value("${file.path.board-images}")
	private String uploadImagePath;
	
	
	/**
	 * 파일 저장하기
	 */
//	@PostMapping(value = "/uploadimg", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//	public ResponseEntity<?> uploadImg(@RequestParam("imgInfos") List<MultipartFile> imgInfos) {
//		try {
//			List<ImgInfoDto> list = fileStore.storeImgs(imgInfos);
//			System.out.println(list);
//			
//			return new ResponseEntity<Void>(HttpStatus.OK);
//		} catch(Exception e) {
//			System.out.println("board uploadImg Controller Error");
//			e.printStackTrace();
//			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
	
	/**
	 * 게시판 imginfo 얻기
	 */
	@GetMapping("/getBoardImgInfo/{articleNo}")
	public ResponseEntity<?> getBoardImgInfo(@PathVariable("articleNo") int articleNo) {
		
		try {
			List<ImgInfoDto> list = fileService.getBoardImgInfo(articleNo);
			return new ResponseEntity<List<ImgInfoDto>>(list, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("file getBoardImgInfo Controller Error");
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * 파일 불러오기 
	 */
	@GetMapping("/getImg/{saveFolder}/{originalName}/{saveName}")
	public ResponseEntity<Object> getImg(@PathVariable("saveFolder") String saveFolder, 
											@PathVariable("originalName") String originalName, 
											@PathVariable("saveName") String saveName)  {
		
		String file = uploadImagePath + saveFolder + File.separator + saveName;
		
		try {
			Path filePath = Paths.get(file);
//			Resource resource = new InputStreamResource(Files.newInputStream(filePath));
			
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentDisposition(ContentDisposition.builder("attachment").filename(URLEncoder.encode(originalName, "UTF-8").replaceAll("//+", "%20")).build());
//			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			Resource resource = new FileSystemResource(file);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-type", Files.probeContentType(filePath));
			
			return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
			
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
		}
		
	} 
	
} 
