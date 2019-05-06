package com.url.shorter.shorterurl.controller;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.url.shorter.shorterurl.exception.ClientException;
import com.url.shorter.shorterurl.model.ShortenUrl;
import com.url.shorter.shorterurl.util.URLValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShorterUrlController {

    private Map<String, ShortenUrl> shortenUrlList = new HashMap<>();

    @PostMapping(value = "/shortener", consumes = {"application/json"})
    public ResponseEntity<Object> getShortenUrl(@RequestBody @Valid ShortenUrl shortenUrl) {
        if (URLValidator.INSTANCE.validateURL(shortenUrl.getFullUrl())) {
            String randomChar = getRandomChars();
            setShortUrl(randomChar, shortenUrl);
            return new ResponseEntity<>(shortenUrl, HttpStatus.OK);
        } else
            throw new ClientException("Please enter a valid url");
    }

    @GetMapping(value = "/s/{randomstring}")
    public void getFullUrl(HttpServletResponse response, @PathVariable("randomstring") String randomString) throws IOException {
        response.sendRedirect(shortenUrlList.get(randomString).getFullUrl());
    }

    private void setShortUrl(String randomChar, ShortenUrl shortenUrl) {
        shortenUrl.setShortUrl("http://localhost:8080/s/" + randomChar);
        shortenUrlList.put(randomChar, shortenUrl);
    }

    private String getRandomChars() {
        String randomStr = "";
        String possibleChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < 5; i++)
            randomStr += possibleChars.charAt((int) Math.floor(Math.random() * possibleChars.length()));
        return randomStr;
    }

}