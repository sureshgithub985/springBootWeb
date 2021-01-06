package com.ey.core.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.ey.core.model.GTProfile;
import com.ey.core.service.GTProfileService;
import com.ey.core.util.XMLConvertor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value = "/wave-prov/wave")
@RestController
public class GTProfileController {

	@Autowired
	private GTProfileService gtprofileService;

	@Autowired
	private XMLConvertor xc;

	@Autowired
	private HttpServletRequest request;

	@PostMapping(value = "/gprofiles", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Void> createGroupProfile(@RequestBody GTProfile gprofile, UriComponentsBuilder uriBuilder) {

		System.out.println(" Create GroupProfile Controller " + request.getContentType());

		HttpHeaders headers = addHeaders(request);

		if (gprofile != null) {
			gtprofileService.addGprofile(gprofile);

			headers.setLocation(
					uriBuilder.path("/wave-prov/wave/gprofiles/{id}").buildAndExpand(gprofile.getId()).toUri());

			return new ResponseEntity<>(headers, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	private HttpHeaders addHeaders(HttpServletRequest request) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", request.getContentType());
		if (request.getMethod().equalsIgnoreCase("GET"))
			headers.add("Accept", request.getHeader("Accept"));

		return headers;
	}

	@GetMapping(value = "/gprofiles", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllGTProfiles() {

		List<GTProfile> gprofileList = gtprofileService.getAllGprofiles();

		HttpHeaders headers = addHeaders(request);

		if (request.getContentType().equals("application/xml")) {
			StringBuilder sb = new StringBuilder();
			sb.append("<collection>");
			for (GTProfile gprofile : gprofileList) {
				sb.append("\n");
				String inXml = xc.toXml(gprofile);
				sb.append(inXml);
			}
			sb.append("\n").append("</collection>");
			Object result = sb.toString();

			return new ResponseEntity<>(result, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(gprofileList, headers, HttpStatus.OK);
		}

	}

	@GetMapping(value = "/gprofiles/{id}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getCustomer(@PathVariable("id") int id) {

		GTProfile gprofile = gtprofileService.getGprofileById(id);

		HttpHeaders headers = addHeaders(request);

		if (request.getContentType().equals("application/xml")) {
			String inXml = xc.toXml(gprofile);
			return new ResponseEntity<>(inXml, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(gprofile, headers, HttpStatus.OK);
		}

	}

	@DeleteMapping(value = "/gprofiles/{id}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<GTProfile> deleteGroupProfile(@PathVariable("id") Integer id) {

		log.debug(" Delete GTProfile Controller ");

		HttpHeaders headers = addHeaders(request);

		if (id != null) {
			gtprofileService.deleteGroupProfileById(id);

			return new ResponseEntity<>(headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping(value = "/gprofiles/{id}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Void> updateEnterprise(@RequestBody GTProfile gprofile, @PathVariable("id") Integer id) {

		log.debug(" Update Enterprise Controller ");
		HttpHeaders headers = addHeaders(request);

		if (id != null && gprofile != null) {
			gtprofileService.updateGprofile(id, gprofile);

			return new ResponseEntity<>(headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
